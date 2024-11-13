import org.openrndr.application
import org.openrndr.draw.loadFont
import org.openrndr.extra.minim.minim
import org.openrndr.shape.Rectangle
import org.openrndr.writer
import kotlin.system.exitProcess

private const val CELL_WIDTH = 64
private const val CELL_HEIGHT = 64

/**
 * Number of columns in the game
 */
const val NUM_COLS = 10

/**
 * Number of rows in the game
 */
const val NUM_ROWS = 10

private const val GRID_WIDTH = CELL_WIDTH * NUM_COLS
private const val GRID_HEIGHT = CELL_HEIGHT * NUM_ROWS

// Constants related to the text area.
private const val TEXTAREA_HEIGHT = 240.0
private const val TEXTAREA_WIDTH = GRID_WIDTH.toDouble()
private const val TEXTAREA_HORIZONTAL_MARGIN = 20.0
private const val FONT_SIZE = 24.0

/**
 * Number of lines of text that can be displayed
 */
const val NUM_TEXT_LINES = (TEXTAREA_HEIGHT / FONT_SIZE).toInt()

/**
 * Starts a game.
 */
fun main() =
    application {
        configure {
            width = GRID_WIDTH
            height = GRID_HEIGHT + TEXTAREA_HEIGHT.toInt()
            title = "Fundies Homework 9"
        }

        program {
            val font = loadFont("data/fonts/default.otf", 24.0)
            Mob.minim = minim()

            keyboard.keyDown.listen {
                if (it.name == "escape") {
                    // This *should* exit the program gracefully but doesn't always work.
                    program.application.exit()
                    // This will definitely end the program.
                    exitProcess(0)
                }
                Game.player.lastKeyPressed = it.name
                Game.tick()
            }

            extend {
                for (x in 0 until NUM_COLS) {
                    for (y in 0 until NUM_ROWS) {
                        Game.getImage(x, y)?.let {
                            drawer.image(
                                it,
                                x * CELL_WIDTH.toDouble(),
                                y * CELL_HEIGHT.toDouble(),
                            )
                        }
                    }
                }

                drawer.fontMap = font

                writer {
                    box =
                        Rectangle(
                            TEXTAREA_HORIZONTAL_MARGIN,
                            GRID_HEIGHT.toDouble(),
                            TEXTAREA_WIDTH,
                            TEXTAREA_HEIGHT,
                        )

                    for (s in Game.textToPrint.takeLast(NUM_TEXT_LINES)) {
                        newLine()
                        text(s)
                    }
                }
            }
        }
    }
