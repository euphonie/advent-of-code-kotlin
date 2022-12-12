fun main() {
    
    fun findFirstUniqueSequence(input: String, windowSize: Int) : Int {
        var startIndex = 0

        for (i in IntRange(0, input.length-windowSize-1) step 1) {
            val letters = input.substring(i, i+windowSize)
            if (letters.toSet().size == letters.toList().size) {
                startIndex = i
                break
            }
        }
        return startIndex + windowSize
    }
    
    fun part1(input: List<String>) : List<Int> {
        val windowSize = 4
        return input.map { findFirstUniqueSequence(it, windowSize) }
    }
    fun part2(input: List<String>) : List<Int> {
        val markerSize = 4
        val messageSize = 14
        return input.map { 
            val markerIndex = findFirstUniqueSequence(it, markerSize) + 1
            Pair(
                markerIndex,
                it.substring(markerIndex, it.length)
            )
        }
        .map { findFirstUniqueSequence(it.second, messageSize) + it.first }
    }

    val testInput = readInput("Day06_test")
    check(part1(testInput) == listOf(7, 5, 6, 10, 11))
    check(part2(testInput) == listOf(25,23,23,29,26))
    
    val input = readInput("Day06")
    check(part1(input) == listOf(1598))
    check(part2(input) == listOf(2414))
}