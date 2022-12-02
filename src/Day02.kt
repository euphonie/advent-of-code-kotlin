sealed class Shape(var superiorTo: Set<Int>,
                   var inferiorTo: Set<Int>, val value: Int): Comparable<Shape> {
    constructor(value: Int): this(setOf(), setOf(), value)
    constructor(superiorTo: Int, inferiorTo: Int,value: Int):
            this(setOf(superiorTo), setOf(inferiorTo), value)

    override operator fun compareTo(other: Shape): Int {
        return when(other.value) {
            in inferiorTo -> -1
            in superiorTo -> 1
            else -> 0
        }
    }
}

object ROCK : Shape(superiorTo=3, inferiorTo=2,1)
object SCISSORS : Shape(superiorTo=2, inferiorTo=1, 3)
object PAPER : Shape(superiorTo=1, inferiorTo=3, 2)


enum class MatchResult(val score: Int) {
    LOSS(0), DRAW(3), WIN(6)
}

interface MatchResolverStrategy {
    fun runMatch(opponentsChoice: String, playersChoice: String) : Int;
}

class FixedHintResolver(private val shift: Int):MatchResolverStrategy {
    /**
     * Map each shape to an array index.
     * A, B, C are mapped by subtracting the ascii code
     * X, Y, Z are mapped accordingly.
     *  **shift** allows to find out the best combination for X, Y, Z
     */
    private val shapes = arrayOf(ROCK, PAPER, SCISSORS)

    private fun resolveOpponentsShape(choice: Char) : Shape {
        var shapeIndex = choice.code - 'A'.code;
        return shapes[shapeIndex]
    }

    private fun resolvePlayersShape(choice: Char) : Shape {
        var shapeIndex = ((choice.code - 'X'.code) + shift) % 3;
        return shapes[shapeIndex]
    }

    override fun runMatch(opponentsChoice: String, playersChoice: String) : Int {
        val oppShape = resolveOpponentsShape(opponentsChoice.single())
        val pShape = resolvePlayersShape(playersChoice.single())
        return if (oppShape > pShape) {
            MatchResult.LOSS.score + pShape.value
        } else if (oppShape < pShape) {
            MatchResult.WIN.score + pShape.value
        } else {
            MatchResult.DRAW.score + pShape.value
        }
    }

}

class MatchHintResolver: MatchResolverStrategy {
    /**
     * Here the second column refers to a hint:
     * X -> lose
     * Y -> draw
     * Z -> win
     *
     * To resolve player's shape use the hint to choose from:
     * - superiorTo shapes
     * - same shape
     * - inferiorTo shapes
     */
    private val shapes = arrayOf(ROCK, PAPER, SCISSORS)
    private fun resolveOpponentsShape(choice: Char) : Shape {
        var shapeIndex = choice.code - 'A'.code;
        return shapes[shapeIndex]
    }

    private fun resolveHintedShape(oppShape: Shape, hint: Char) : Shape {
        var mirroredShapeIndex = when(hint) {
            'X' -> oppShape.superiorTo.iterator().next()
            'Z' -> oppShape.inferiorTo.iterator().next()
            else -> oppShape.value
        }
        return shapes[mirroredShapeIndex -1]
    }

    override fun runMatch(opponentsChoice: String, playersChoice: String) : Int {
        val oppShape = resolveOpponentsShape(opponentsChoice.single())
        val pShape = resolveHintedShape(oppShape, playersChoice.single())
        return if (oppShape > pShape) {
            MatchResult.LOSS.score + pShape.value
        } else if (oppShape < pShape) {
            MatchResult.WIN.score + pShape.value
        } else {
            MatchResult.DRAW.score + pShape.value
        }
    }
}

fun main(){

    /**
     * Compute scores for all matches,
     * shift = 0, follow base logic for unspecified column. E.g.
     * X = ROCK,
     * Y = PAPER,
     * Z = SCISSORS
     */
    fun part1(input: List<String>) : Int {
        val resolver: MatchResolverStrategy = FixedHintResolver(0)
        var score = 0;
        for (match in input){
            val (opponentsChoice, playersChoice) = match.split(" ")
            score += resolver.runMatch(opponentsChoice,playersChoice)
        }
        return score;
    }

    fun part2(input: List<String>) : Int {
        val resolver: MatchResolverStrategy = MatchHintResolver()
        var score = 0;
        for (match in input){
            val (opponentsChoice, playersChoice) = match.split(" ")
            score += resolver.runMatch(opponentsChoice,playersChoice)
        }
        return score;
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInput("Day02")
    println(part1(input))
    check(part1(input) == 13221)
    println(part2(input))
    check(part2(input) == 13131)
}