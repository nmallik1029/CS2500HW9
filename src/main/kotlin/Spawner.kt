import kotlin.random.Random

/**

A spawner of any type of [Entity].*/
open class Spawner<T : Entity>(
    private val spawnType: String,
    private val spawn: () -> T,
    private val spawnProbability: Double,
    private val hardness: Int

) : Entity("$spawnType Spawner", "SpawnerOnSand.png"), Damageable {

    var hp = hardness

    private fun spawnIfSpace() {
        val empty = selectAdjacentEmptyCell()
        if (empty != null) {
            Game.place(spawn(), empty.x, empty.y)
            Game.addText("$spawnType spawned at (${empty.x}, ${empty.y})")
        } else {
            Game.addText("No empty cell available")
        }
    }


    override fun tick() {
        val randomValue = Random.nextDouble()
        Game.addText("Random value for spawning: $randomValue")
        if (randomValue < spawnProbability) {
            spawnIfSpace()
        }
    }

    override fun takeDamage(
        damage: Int,
    ) {
        val actualDamage = if (damage > hp) hp else damage
        hp -= actualDamage
        val text = if (actualDamage == 1) "heart" else "hearts"
        Game.addText("$spawnType took $actualDamage $text of damage.")
        if (hp <= 0) {
            broken()
        }
    }

    private fun broken() {
        Game.addText("$spawnType Spawner has been broken")
        Game.remove(this)
    }
}