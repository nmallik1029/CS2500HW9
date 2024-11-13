/**
 * A zombie, a hostile [Mob].
 */
class Zombie :
    Mob(
        "Zombie",
        20,
        Behavior.Hostile,
        5,
        "Zombie.png",
        "zombie.mp3",
        "Zombie groans",
    )
