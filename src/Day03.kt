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

    fun getBadgePriority(group: List<String>) : Int {
        val matchingType = group[0].filter {
            it in group[1] && it in group[2]
        }.toSet()
        return matchingType.sumOf { c -> getCharValue(c) }
    }

    fun part2(input: List<String>) : Int {
        var badgePriorities = 0
        for (i in input.indices step 3) {
            badgePriorities += getBadgePriority(input.slice(i..i+2))
        }
        return badgePriorities
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput("Day03")
    check(part1(input) == 7763)
    check(part2(input) == 2569)
}