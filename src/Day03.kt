fun main() {
    fun getCharValue(c: Char) : Int {
        return (if (c.isLowerCase()) c.code -'a'.code else c.code -'A'.code+26)+1
    }

    fun countMatchingTypes(rucksack: String) : Int {
        val halfIndex = (rucksack.length/2)
        val firstCompartment = rucksack.substring(0, halfIndex)
        val secondCompartment = rucksack.substring(halfIndex)

        val matchingTypes = firstCompartment.filter{
            it in secondCompartment
        }.toSet()
        return matchingTypes.sumOf { c -> getCharValue(c) }
    }

    fun part1(input: List<String>) : Int {
        return input.sumOf { rucksack -> countMatchingTypes(rucksack) }
    }


    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)

    val input = readInput("Day03")
    check(part1(input) == 7763)
}