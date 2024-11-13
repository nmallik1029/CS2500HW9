import org.openrndr.draw.ColorBuffer
import kotlin.math.pow
import kotlin.random.Random

/**
 * A singleton (unique) object encapsulating the game state.
 */
object Game {
    private val blockGrid =
        Array(NUM_COLS) {
            Array<Block>(NUM_ROWS) {
                Sand()
            }
        }

    private val entityGrid =
        Array(NUM_COLS) {
            Array<Entity?>(NUM_ROWS) {
                null
            }
        }

    /**
     * The player of the game.
     */
    var player = Player()

    private val text = mutableListOf<String>()

    /**
     * Text to print at the bottom of the game screen.
     */
    val textToPrint
        get() = text as List<String>

    private val positions = mutableMapOf<Entity, Position>()

    /**
     * All entities that have been placed on the grid.
     */
    val placedEntities
        get() = positions.keys.toList()

    init {
//        placeAtRandom(player)
//        placeAtRandom(Spider())
//        placeAtRandom(Sheep())
//        placeAtRandom(Chicken())

        val randomX = (0 .. NUM_ROWS-1).random()
        val randomY = (0 .. NUM_COLS-1).random()
//        Game.place(SpiderSpawner(), randomX, randomY)

        Game.place(SpiderSpawner(), 1,0)
        Game.place(player,0,1)
//        // Place SpiderSpawner at (0, 0)
//        Game.place(SpiderSpawner(), 0, 0)
//
//        // Surround it with other entities to block it
//        Game.place(Spider(),1, 0)
//        Game.place(Spider(), 0, 1)
//        Game.place(Spider(), 1, 1)

        Game.place(ZombieSpawner(), randomX, randomY)
    }

    /**
     * Adds [string] to appear in the panel at the bottom of the game window.
     */
    fun addText(string: String) {
        text.add(string)
        println(string) // also display in IntelliJ's Run pane
        if (text.size > NUM_TEXT_LINES) {
            text.removeAt(0)
        }
    }

    /**
     * Clears the text in the panel at the bottom of the game window.
     */
    fun clearText() {
        text.clear()
    }

    /**
     * Tests whether the given [x] and [y] coordinates are in bounds.
     */
    fun isInBounds(
        x: Int,
        y: Int,
    ) = x in 0.until(NUM_COLS) && y in 0.until(NUM_ROWS)

    /**
     * Returns whether the cell with the specified [x] and [y] coordinates is
     * occupied by an [Entity] in the game. It is illegal to try to move
     * another [Entity] into an occupied cell. This returns `false` if
     * the coordinates are out of bounds.
     */
    fun isOccupied(
        x: Int,
        y: Int,
    ) = isInBounds(x, y) && entityGrid[x][y] != null

    /**
     * Returns whether the cell with the specified [x] and [y] coordinates is
     * unoccupied. See [isOccupied]. This returns `false` if the coordinates
     * are out of bounds.
     */
    fun isEmpty(
        x: Int,
        y: Int,
    ) = isInBounds(x, y) && !isOccupied(x, y)

    /**
     * Places the [entity] at the specified [x] and [y] coordinates, throwing [IllegalArgumentException]
     * if that position [isOccupied] or out of bounds.
     */
    fun place(
        entity: Entity,
        x: Int,
        y: Int,
    ) {
        require(isEmpty(x, y)) {
            "You cannot place the $entity at ($x, $y), which is already occupied by a ${getEntity(x, y)}."
        }
        require(isInBounds(x, y)) {
            "The location ($x, $y) is out of bounds."
        }
        if (entity in positions) {
            remove(entity)
        }
        entityGrid[x][y] = entity
        positions[entity] = Position(x, y)
    }

    /**
     * Places the [entity] at a random position that [isEmpty].
     */
    fun placeAtRandom(entity: Entity) {
        var x: Int
        var y: Int

        do {
            x = Random.nextInt(0, NUM_COLS)
            y = Random.nextInt(0, NUM_ROWS)
        } while (isOccupied(x, y))
        place(entity, x, y)
    }

    /**
     * Removes the [entity] from the game, so it no longer appears on
     * the grid and its [Entity.tick] method is no longer called.
     * This throws [IllegalArgumentException] if had not been on the grid.
     */
    fun remove(entity: Entity) {
        require(entity in positions) {
            "The $entity was not on the grid."
        }
        val position = positions[entity]
        if (position == null) {
            throw java.lang.IllegalArgumentException("The $entity was not on the grid.")
        }
        entityGrid[position.x][position.y] = null
        positions.remove(entity)
    }

    /**
     * Gets whatever entity is at the specified [x] and [y] coordinates,
     * or `null` if it is unoccupied or the position is out of bounds.
     */
    fun getEntity(
        x: Int,
        y: Int,
    ): Entity? =
        if (x in 0..NUM_COLS && y in 0..NUM_ROWS) {
            entityGrid[x][y]
        } else {
            null
        }

    /**
     * Gets whatever block is at the specified [x] and [y] coordinates
     * or `null` if the position out of bounds.
     */
    fun getBlock(
        x: Int,
        y: Int,
    ): Block? =
        if (x in 0..NUM_COLS && y in 0..NUM_ROWS) {
            blockGrid[x][y]
        } else {
            null
        }

    /**
     * Gets the image at the specified [x] and [y] coordinates or
     * `null` if the position is out of bounds.
     */
    fun getImage(
        x: Int,
        y: Int,
    ): ColorBuffer? =
        if (x in 0..NUM_COLS && y in 0..NUM_ROWS) {
            entityGrid[x][y]?.image ?: blockGrid[x][y].image
        } else {
            null
        }

    /**
     * Calculates the Euclidean distance between [entity1] and [entity2],
     * throwing [IllegalArgumentException] if either has not been placed.
     */
    fun calculateDistance(
        entity1: Entity,
        entity2: Entity,
    ): Double {
        val position1 = getPosition(entity1)
        val position2 = getPosition(entity2)
        require(position1 != null && position2 != null)
        val distance =
            Math.sqrt(
                (position1.x - position2.x).toDouble().pow(2.0) +
                    (position1.y - position2.y).toDouble().pow(2.0),
            )
        return distance
    }

    /**
     * Returns the position of the given [entity], or `null` if it has
     * not been placed.
     */
    fun getPosition(entity: Entity): Position? = positions[entity]

    /**
     * Calls the [Entity.tick] method of all entities on the board,
     * starting with the [Player].
     */
    fun tick() {
        player.tick()
        for (entity in placedEntities) {
            if (entity !is Player && player.isAlive) {
                entity.tick()
            }
        }
    }
}
