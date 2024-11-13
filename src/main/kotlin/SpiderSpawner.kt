import kotlin.random.Random

class SpiderSpawner : Entity("Spider", "SpawnerOnSand.png"), Damageable {
    private fun spawnIfSpace() {
        val empty = selectAdjacentEmptyCell()
        if (empty != null) {
            Game.place(Spider(), empty.x, empty.y)
            Game.addText("Spider spawned at (${empty.x}, ${empty.y})")
        } else {
            Game.addText("No empty cell available")
        }
    }

    private val spawnProbability: Double = 0.75

    override fun tick() {
        val randomValue = Random.nextDouble()
        Game.addText("Random value for spawning: $randomValue")
        if (randomValue < spawnProbability) {
            spawnIfSpace()
        }
    }

    private var hardness: Int = 6

    override fun takeDamage(
        damage: Int,
    ) {
        val actualDamage = if (damage > hardness) hardness else damage
        hardness -= actualDamage
        val text = if (actualDamage == 1) "heart" else "hearts"
        Game.addText("$type took $actualDamage $text of damage.")
        if (hardness <= 0) {
            broken()
        }
    }

    private fun broken() {
        Game.addText("$type Spawner has been broken")
        Game.remove(this)
    }

}