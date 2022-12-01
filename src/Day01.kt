fun main() {
    fun buildSumArray(input: List<String>) : List<Int> {
        val sums = ArrayList<Int>()
        var currSum = 0;
        for (food in input) {
            if (food == "") {
                sums.add(currSum);
                currSum = 0;
            } else {
                currSum += food.toInt()
            }
        }
        return sums
    }

    /**
     * Return max sum from array
     */
    fun part1(input: List<String>): Int {
        var max = 0;
        val sums = buildSumArray(input)
        for (sum in sums){
            if (sum > max){
                max = sum
            }
        }
        return max
    }

    /**
     * Get 3 greatest values from array
     */
    fun part2(input: List<String>): Int {
        val sums =  ArrayList<Int>(buildSumArray(input))
        sums.sort()
        return sums[sums.size-1] + sums[sums.size-2] + sums[sums.size-3]
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000)
    check(part2(testInput) == 41000)

    val input = readInput("Day01")
    check(part1(input) == 67658)
    check(part2(input) == 200158)

}
