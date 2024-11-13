/**
 * An inanimate object in the game, such as [Sand].
 */
abstract class Block(
    type: String,
    imageFileName: String,
) : Entity(type, imageFileName) {
    override fun tick() {
        // Blocks don't do anything.
    }
}
