fun main() {

    fun existsFullIntersection(assignment: String) : Int {
        val ranges = assignment.split(",", "-")

        val firstInSecond =
            ranges[0].toInt() in ranges[2].toInt()..ranges[3].toInt()  &&
                    ranges[1].toInt() in ranges[2].toInt()..ranges[3].toInt()

        val secondInFirst =
            ranges[2].toInt() in ranges[0].toInt()..ranges[1].toInt()  &&
                    ranges[3].toInt() in ranges[0].toInt()..ranges[1].toInt()
        return if (firstInSecond || secondInFirst ) 1 else 0
    }


    fun part1(input: List<String>) : Int {
        return input.sumOf { existsFullIntersection(it) }
    }

    fun part2(input: List<String>) : Int {
        return 2
    }


    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)

    val input = readInput("Day04")
    check(part1(input) == 475)
}