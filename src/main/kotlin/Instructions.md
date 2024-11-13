## Instructions

The starter code is based on the solution code for Homework 6 with these
changes:

* The file `Minecraft2D.kt` has been replaced by `Homework9Game.kt`.
* Improved abstraction and bug fix over Homework 6
    * The classes `Mob` and `Block` are now abstract.
    * `Position` is now a data class.
    * `LivingEntity.takeDamage()` now takes only one argument (the victim)
      and does not print a redundant message about who attacks whom.
    * Reduced visibility of some public properties to `protected` or
      `private`.
    * Reduced visibility of `LivingEntity`'s movement methods to `protected`.
    * Made `Player` attack nearby aggressive mobs, which makes more sense
      than having it attack only hostile mobs.
* A new mob, `Spider` has been added.
* Scaffolding for adding spawners:
    * The method `die()` has been renamed to `exit()`.
    * There is a new interface `Damageable` and a new class `Spawner`, both
      of which you will need to complete.

Feel free to change the `NOISINESS` constant in `Mob.kt` if the noises get
annoying.

The initial code should be runnable (`Gradle > Tasks > application > run`).
It should behave like a completed Homework 6 with the addition of a `Spider`.

### Problem 1: Selecting an adjacent empty cell

Create and implement a new method in `Entity` with this KDoc and signature:

    /**
     * Returns the position of an empty cell adjacent to this entity or
     * `null` if no such cell exists or if this entity is not on the board.
     */
    fun selectAdjacentEmptyCell(): Position?

For full credit, it needs to behave as described. It does not need to select an
adjacent cell randomly. For example, it is fine if it always chooses the
position to the left if it is available. You do need to include the KDoc above
your code.

### Problem 2: Refactoring

Right now, the `takeDamage()` method is in `LivingEntity`. You will be creating
a new type of `Entity` that is not a `LivingEntity` but will be capable of
taking damage. To lay the groundwork, you will complete the interface
`Damageable`, move `takeDamage()` to it, have `LivingEntity` implement the
interface, and generalize functions to accept arguments of the more general type
`Damageable` rather than the specific type `LivingEntity`. You will learn more
if you keep this big picture in mind when doing each of the following steps.

1. Open `Damageable.kt`. You should see the declaration of an empty interface,
   `Damageable`, preceded by KDoc. Do not change the provided KDoc. Everything
   you do in steps 2-3 should go between the existing curly braces.
2. Move the KDoc for `takeDamage()` from `LivingEntity` to `Damageable`. Remove
   all references to properties, since they will not necessarily apply. Replace
   the word `hearts` with `units`, since not all entities have hearts or status.
3. Immediately below the KDoc, put the signature of the method. That means
   that subclasses will have to provide an implementation of the method.
4. Modify `LivingEntity` to indicate that it implements the `Damageable`
   interface and that `takeDamage()` overrides a method. (You may need to refer
   to lecture notes or ask ChatGPT how to do this.)  When you override or
   implement a method declared elsewhere, you should not provide KDoc.
5. We want it to be possible to attack anything that is `Damageable`, so, in
   `LivingEntity`, change the type of the `attack()` parameter to `Damageable`.
   You will also need to change the reference to `victim.type` because
   `Damageable` objects do not have a `type`.
6. Make the corresponding change to the `attack()` parameter in `Mob`.
7. Make sure your code still compiles and runs and that living entities can
   still take damage and be killed.
8. Generate the KDoc and look at the page for `LivingEntity`. See how it
   indicates that `LivingEntity` implements the `Damageable` interface and what
   it shows for `takeDamage()`.

### Problem 3: Creating a spider spawner

You will create a spider [spawner](https://minecraft.fandom.com/wiki/Monster_Spawner)
that randomly creates spiders that appear in empty adjacent cells. A Canvas
video shows the expected behavior.

1. Create a class `SpiderSpawner` that is a subclass of `Entity`. It should
   have no parameters. Use the provided `Spawner` image (`SpawnerOnSand.png`).
   Don't forget to add KDoc (a brief description of the class). You should
   regenerate and view the KDoc to make sure it appears properly.
2. Create a private method `spawnIfSpace()` that checks for an adjacent empty
   cell using `selectAdjacentEmptyCell()`. If it finds one, it constructs a
   `Spider` and places it at the returned `Position`.
3. Add a private (non-parameter) property `spawnProbability`, which should
   be a `Double` in the range 0-1, indicating the probability of spawning a
   spider on each turn. For example, if it is .75, the per-turn probability
   would be 75%. You may choose the value.
4. Create a method `tick()` so that on each turn, a random number between 0 and
   1 is generated. If the number is less than `spawnProbability`,
   `spawnIfSpace()` should be called.
5. For debugging and documentation purposes, have `tick()` and `spawnIfSpace()`
   call `Game.addText()` with messages indicating the value of the random number
   and (if `spawnIfSpace()` is called) whether an empty adjacent cell was found
   and a spider spawned.
6. Modify the `init` block in `Game` to randomly place a `SpiderSpawner` on the
   board. Run the program and make sure it behaves as expected. You should
   see turns in which a spider is spawned and turns in which they are not.
   **Copy the transcript showing different behaviors into `Summary.md`**.
7. Modify the `init` block in `Game` to place the `SpiderSpawner` at (0, 0), and
   place other entities so they block it in, as shown in the video. Increase the
   spawn probability to 1.0. Make sure it behaves as expected (not spawning
   spiders when there is no adjacent empty cell). **Copy the transcript into
   `Summary.md`**. Undo the changes in this step before proceeding to the next
   problem.

### Problem 4: Making spawners damageable

In Problem 3, you created indestructible spider spawners. In this problem, you
will make them `Damageable`. Watch the video for this problem on the Canvas page
to see the expected behavior.

1. Add to `SpiderSpawner` a private mutable property `hardness`, which should be
   an `Int` of your choice greater than 0, such as 6.
2. Modify the `SpiderSpawner` class header to indicate that it will implement
   the `Damageable` interface.
3. Implement (override) the method `takeDamage()`. (You might want to refer to
   the implementation in `LivingEntity` for reference.) It should print
   appropriate messages and reduce the `hardness` value. When `hardness` reaches
   0, it should call an appropriately named private method that you create that
   prints that the spawner has been destroyed and removes it from the game.
   As a reminder, private methods do not require KDoc.
4. Right now, the player attacks only aggressive mobs that are nearby. Change it
   so it also attacks all`Damageable` entities. Make sure to exclude the player
   or it will attack itself! Also make sure the names of your methods are
   accurate and proper style (verbs).
5. **Test your code to make sure the player can destroy a spider spawner and
   save the transcript to `Summary.md`.**

### Problem 5: Creating a generic spawner

In this problem, you will complete a generic version of `Spawner` so you are
able to construct spawners for different types of mobs. Do not delete
`SpiderSpawner`, which you should keep for reference and grading.

1. Examine the provided class `Spawner<T>`. Note that the constructor takes two
   arguments:
    * The name of the entity type. This enables it to create its own type string
      (e.g., `"Spider Spawner"`) to pass to the `Entity` constructor.
    * A no-argument function `spawn()` that constructs a new instance of the
      class to spawn (e.g., `Spider`) whenever it is called. For example, you
      could write: `val newEntity = spawn()`
2. Add these parameters to the constructor and make them properties:
    * `spawnProbability` (`Double`)
    * `hardness` (`Int`)
3. Modify the class header to show that `Spawner<T>` will implement the
   `Damageable` interface
4. Copy all the methods from `SpiderSpawner`, replacing occurrences of the
   string literal `"Spider"` with the property `spawnType`. Replace any calls
   to the `Spider` constructor with calls to `spawn(). Make other changes as
   needed.
5. Make `Spawner` open so it can be extended (subclassed).

### Problem 6: Creating a zombie spawner

For this part, you will create a zombie spawner, which will take very little
code, thanks to the work you did making `Spawner` generic. Watch the video for
this problem on the Canvas page to see the expected behavior.

1. Create a new class `ZombieSpawner` that extends `Spawner<Zombie>`.
   The new class constructor should take no parameters, but it should
   pass the necessary arguments to the `Spawner<Zombie>` constructor.
   These are:
    * the name of the class being spawned (`"Zombie"`)
    * an anonymous function of no arguments that returns a new `Zombie`
    * `spawnProbability` (you can choose the value)
    * `hardness` (you can choose the value)
      You should not need to provide any code.
2. Add code to the `init` block of `Game` to create and randomly place a
   `ZombieSpawner`. You can remove other placements from the `init` block.
3. Test out the game and make sure the zombie spawner
    * spawns zombies
    * generates appropriate output
    * can be destroyed

   It would be wise to decrease zombies' attack strength or increase the
   player's number of hearts for testing purposes. You don't need to change
   them back.

### Problem 7: Adding another spawner

That was a lot work, but now it is easy to add other spawners. Create a
spawner for an existing (or new) `Mob`. You can get a picture of a mob from
the [Minecraft Wiki](https://minecraft.fandom.com/wiki/Mob) and
[convert it from webpng to a 64x64 png online](https://cloudconvert.com/webp-to-png).
You can pass `null` for the sound file name.

Add your new `Spawner` subclass to the game and make sure it works.

### Problem 8: Creating a spawner spawner

You should now be able to create a `ZombieSpawnerSpawner`: a spawner that
creates instances of `ZombieSpawner`. This should take only a few lines of code.
Test that it works.

## Tips

These are the same tips as in Homework 6.

The #1 tip is to ask for help on Piazza or in office hours if anything is
unclear.

### Building

* Build the KDoc with `Gradle > Tasks > documentation > dokkaHtml`. You can then
  copy it to a directory outside the project so it won't get deleted when you
  rebuild the project. This is demonstrated in a Canvas video.
* Run the application with `Gradle > Tasks > application > run`. For subsequent
  runs, you can click the green play button (triangle) on the top of IntelliJ,
  towards the right. This is demonstrated in a Canvas video.
* If you get a strange compiler error, such as `Changes are not tracked,
  unable determine incremental changes.`, or strange run time errors, try each
  of the following (and document your issues in `Summary.md`):
    * `Gradle > Tasks > build > clean`
    * `File > Invalidate Caches...Invalidate and Restart`
* Ignore warnings about deprecated Gradle features as long as your project runs.

### Implementation and Debugging

* Always read provided KDoc carefully when implementing a method.
* If you prefer, you can modify [Player](Player.kt) to be Alex rather than
  Steve.
* Text output
    * If the game grid uses up too much of your screen for you to see the
      messages in the text area, you can:
        - Change the constant `NUM_ROWS` in [MinecraftSpawner.kt](Homework9Game.kt).
        - View the text in IntelliJ's `Run` pane.
    * You may find it useful to call `clearText()` from `Game.tick()`.
    * You can print debugging messages by calling `Game.addText()`.
* If you make use of the `Random` library, make sure `kotlin.random.Random` is
  imported, not `java.util.Random`.
* The [Minecraft Wiki](https://minecraft.fandom.com/wiki/Minecraft_Wiki) is
  a useful resource.












