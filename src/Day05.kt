class MultiStack(private val stackCount: Int, private val stackSize: Int)  {
    private val array: Array<String> = Array(stackCount * stackSize) {""}
    private val tops: Array<Int> = Array(stackCount) {-1}

    fun push(stackIndex: Int, value: String) {
        if (tops[stackIndex] == stackSize -1) {
            throw Exception("full")
        }
        tops[stackIndex] += 1
        array[getTopIndex(stackIndex)] = value
    }

    fun pop(stackIndex : Int) : String {
        if (tops[stackIndex] == -1) {
            throw Exception("empty stack")
        }
        val topIndex = getTopIndex(stackIndex)
        val value = array[topIndex]

        array[topIndex] = ""
        tops[stackIndex] -= 1
        return value
    }
    
    fun multiPush(stackIndex: Int, values: List<String>) {
        values.forEach{ v -> push(stackIndex, v) }
    }
    
    fun multiPop(stackIndex: Int, count: Int) : List<String> {
        if (tops[stackIndex] == -1) {
            throw Exception("empty stack")
        }
        
        return IntRange(1, count).map { pop(stackIndex) }
    }

    fun peek( stackIndex: Int) : String {
        if (tops[stackIndex] == -1) {
            throw Exception("empty stack")
        }
        return array[getTopIndex(stackIndex)]
    }


    fun isEmpty(stackIndex: Int) : Boolean {
        return tops[stackIndex] == -1
    }

    private fun getTopIndex(stackIndex: Int) : Int {
        return stackIndex * stackSize + tops[stackIndex]
    }

}

class Command(val count: Int, val origin: Int, val dest: Int )

fun main() {
    fun parseAssignments(input: List<String>) : List<List<String>> {
        return input
            .takeWhile { !it.trimStart().startsWith("1") }
            .map { 
                current -> (1 until current.length step 4).map { current[it].toString() }
            }
    }

    fun createCommand(input: String) : Command {
        val regex = Regex("move ([0-9]+) from ([0-9]+) to ([0-9]+)")
        return regex.matchEntire(input)?.destructured
            ?.let {
                (count, origin, dest) -> 
                    try {
                        Command(count = count.toInt(), origin = origin.toInt(),
                        dest = dest.toInt())
                    } catch(e: Exception) {
                        Command (count = 0, origin = 0, dest = 0)
                    }
            } ?: Command (count = 0, origin = 0, dest = 0)
    }
    
    fun parseCommands(input: List<String>) : List<Command> {
        val firstCommandIndex = input.withIndex().first {
            it.value.trimStart().startsWith("move")
        }.index
        return input.subList(firstCommandIndex, input.size)
                .map { createCommand(it) }
    }

    fun processInput(input: List<String>) : Pair<List<List<String>>, List<Command>> {
        val assignments = parseAssignments(input)

        if (assignments.isEmpty()) {
            throw Exception("no assignments")
        }
        
        val commands = parseCommands(input)

        return Pair(assignments, commands)
    }

    fun part1(input: List<String>) : String {
        val (assignments, commands) = processInput(input)
        val stackCount = assignments.first().size
        val stacks : MultiStack = MultiStack(stackCount, assignments.size * stackCount)

        assignments.reversed()
            .forEach {
                assignment -> 
                assignment.indices.forEach { i -> if (assignment[i] != " ") stacks.push(i, assignment[i]) }
            }

        commands.forEach { command ->
            IntRange(1, command.count).forEach { _ ->
                run {
                    val el = stacks.pop(command.origin - 1)
                    stacks.push(command.dest - 1, el)
                }
            }
        }
        
        return IntRange(0, stackCount -1).joinToString("") { stacks.peek(it) }
    }
    fun part2(input: List<String>) : String {
        val (assignments, commands) = processInput(input)
        val stackCount = assignments.first().size
        val stacks : MultiStack = MultiStack(stackCount, assignments.size * stackCount)

        assignments.reversed()
            .forEach {
                    assignment ->
                assignment.indices.forEach { i -> if (assignment[i] != " ") stacks.push(i, assignment[i]) }
            }

        commands.forEach { command ->
            val elements = stacks.multiPop(command.origin -1, command.count)
            stacks.multiPush(command.dest -1, elements.reversed())
        }

        return IntRange(0, stackCount -1).joinToString("") { stacks.peek(it) }
    }


    val testInput = readInput("Day05_test")
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")

    val input = readInput("Day05")
    check(part1(input) == "PSNRGBTFT")
    check(part2(input) == "BNTZFPMMW")
}