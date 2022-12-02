sealed class Shape(var superiorTo: Set<String>,
                   var inferiorTo: Set<String>,
                   private val name: String, val value: Int): Comparable<Shape> {
    constructor(name: String, value: Int): this(setOf(), setOf(), name, value)
    constructor(superiorTo: String, inferiorTo: String,name: String, value: Int):
            this(setOf(superiorTo), setOf(inferiorTo), name, value)

    override operator fun compareTo(other: Shape): Int {
        return when(other.name) {
            in inferiorTo -> -1
            in superiorTo -> 1
            else -> 0
        }
    }
}

object ROCK : Shape(superiorTo="scissors", inferiorTo="paper", "rock",1)
object SCISSORS : Shape(superiorTo="paper", inferiorTo= "rock", "scissors", 3)
object PAPER : Shape(superiorTo= "rock", inferiorTo="scissors", "paper", 2)


enum class MatchResult(val score: Int) {
    LOSS(0), DRAW(3), WIN(6)
}

fun main(){

    val shapes = arrayOf(ROCK, PAPER, SCISSORS)

    fun resolvePlayersShape(shape: Char, shift: Int) : Shape {
        var shapeIndex = ((shape.code - 88) +shift) % 3;
        return shapes[shapeIndex]
    }

    fun resolveShape(shape: Char) : Shape {
        var shapeIndex = shape.code - 65;
        return shapes[shapeIndex]
    }

    fun runMatch(opponentsShape: Shape, playersShape: Shape) : Int {
        return if (opponentsShape > playersShape) {
            MatchResult.LOSS.score + playersShape.value
        } else if (opponentsShape < playersShape) {
            MatchResult.WIN.score + playersShape.value
        } else {
            MatchResult.DRAW.score + playersShape.value
        }
    }

    /**
     * Compute scores for all matches,
     * shift = 0, follow base logic for unspecified column. E.g.
     * X = ROCK,
     * Y = PAPER,
     * Z = SCISSORS
     */
    fun part1(input: List<String>) : Int {
        var score = 0;
        var shift = 0;
        for (match in input){
            val (opponentsChoice, playersChoice) = match.split(" ")
            score += runMatch(
                resolveShape(opponentsChoice.single()),
                resolvePlayersShape(playersChoice.single(), shift)
            )
        }
        return score;
    }
    fun part2(input: List<String>) : Int {return 2}


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 2)

    val input = readInput("Day02")
    //println(part1(input))
    //check(part1(input) == 1)
    //check(part2(input) == 2)
}