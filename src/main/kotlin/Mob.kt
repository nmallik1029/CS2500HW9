import ddf.minim.Minim
import kotlin.random.Random

const val NOISINESS = 0.1

/**
 * A Minecraft mob.
 */
abstract class Mob(
    type: String,
    maxHearts: Int,
    private val behavior: Behavior,
    attackStrength: Int,
    imageFileName: String,
    soundFileName: String? = null,
    private val subtitle: String? = null,
) : LivingEntity(type, imageFileName, maxHearts, attackStrength) {
    private var sound = if (soundFileName == null) null else minim?.loadFile("data/sounds/$soundFileName")

    enum class Behavior { Passive, Hostile, Neutral, Boss }

    val isAggressive
        get() =
            if (!isAlive) {
                false
            } else {
                when (behavior) {
                    Behavior.Passive -> false
                    Behavior.Boss, Behavior.Hostile -> true
                    Behavior.Neutral -> status == Status.Injured
                }
            }

    /**
     * Attacks [victim], doing [attackStrength] hearts of damage. This throws
     * [IllegalArgumentException] unless [isAggressive].
     */
    override fun attack(victim: Damageable) {
        require(this.isAggressive)
        super.attack(victim)
    }

    private fun makeNoise() {
        if (subtitle != null) {
            Game.addText(subtitle)
        }
        if (sound != null) {
            sound?.play()
            sound?.rewind()
        }
    }

    override fun tick() {
        // Once in a while, have the mob make a noise.
        if (Random.nextDouble() < NOISINESS) {
            makeNoise()
        }

        if (isAggressive) {
            if (Game.calculateDistance(this, Game.player) <= FIGHTING_RANGE) {
                attack(Game.player)
            } else {
                moveTowards(Game.player)
            }
        } else {
            moveRandomly()
        }
    }

    companion object {
        var minim: Minim? = null
    }
}
