interface OverlapValidator {
    fun checks(range: IntRange, otherRange: IntRange) : Boolean
}
class FullOverlapValidator: OverlapValidator {
    override fun checks(range: IntRange, otherRange: IntRange): Boolean {
        val firstInSecond =
            range.first in otherRange  &&
                    range.last in otherRange

        val secondInFirst =
            otherRange.first in range.first..range.last  &&
                    otherRange.last in range.first..range.last

        return firstInSecond || secondInFirst
    }
}

class PartialOverlapValidator : OverlapValidator {
    override fun checks(range: IntRange, otherRange: IntRange): Boolean {
        val firstInSecond =
            range.first in otherRange  ||
                    range.last in otherRange

        val secondInFirst =
            otherRange.first in range.first..range.last  ||
                    otherRange.last in range.first..range.last

        return firstInSecond || secondInFirst
    }
}

fun main() {

    fun existsFullIntersection(assignment: String) : Int {
        val ranges = assignment.split(",", "-")
        val validator = FullOverlapValidator()
        val isContained = validator.checks(
            ranges[0].toInt()..ranges[1].toInt(),
            ranges[2].toInt()..ranges[3].toInt())

        return if (isContained) 1 else 0
    }

    fun existsPartialIntersection(assignment: String) : Int {
        val ranges = assignment.split(",", "-")
        val validator = PartialOverlapValidator()
        val isContained = validator.checks(
            ranges[0].toInt()..ranges[1].toInt(),
            ranges[2].toInt()..ranges[3].toInt())

        return if (isContained) 1 else 0
    }


    fun part1(input: List<String>) : Int {
        return input.sumOf { existsFullIntersection(it) }
    }

    fun part2(input: List<String>) : Int {
        return input.sumOf { existsPartialIntersection(it) }
    }


    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day04")
    check(part1(input) == 475)
    check(part2(input) == 825)
}