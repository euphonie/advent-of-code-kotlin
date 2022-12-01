import java.lang.Double.max

fun main() {
    fun part1(input: List<String>): Int {
        var max = 0.0;
        var currSum = 0.0;
        for (food in input) {
            if (food == "") {
                max = max(max, currSum);
                currSum = 0.0;
            } else {
                currSum += food.toDouble();
            }
        }
        return max.toInt()
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000)

    val input = readInput("Day01")
    check(part1(input) == 67658)
    
    println(part2(input))
}
