import org.openrndr.draw.ColorBuffer
import org.openrndr.draw.loadImage

/**
 * An animate or inanimate object in the game.
 *
 * @constructor Constructs an entity with the given [type] and [imageFileName].
 */
abstract class Entity(
    protected val type: String,
    imageFileName: String,
) {
    val image: ColorBuffer = loadImage("data/images/$imageFileName")

    /**
     * Takes an action during its turn.
     */
    abstract fun tick()

    /**
     * Removes this entity from the game. It will no longer appear
     * on the screen, and its [tick] method will no longer be called.
     */
    open fun exit() {
        Game.remove(this)
    }

    override fun toString() = type

    /**
     * @return the position of an empty cell adjacent to this entity or
     * null if no such cell exists or if this entity is not on the board.
     */
    fun selectAdjacentEmptyCell(): Position? {
        val position = Game.getPosition(this) ?: return null
        val directions = listOf(
            Position(position.x + 1, position.y),  // Right
            Position(position.x - 1, position.y),  // Left
            Position(position.x, position.y + 1),  // Down
            Position(position.x, position.y - 1)   // Up
        )
        return directions.firstOrNull { Game.isEmpty(it.x, it.y) }
    }

}
