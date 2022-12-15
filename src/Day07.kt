import kotlin.math.abs

class Node(
    var size: Int,
    val parent: Node?,
    val isDir: Boolean,
    val basename: String) {
    
    private val children: MutableMap<String, Node> = HashMap<String, Node>()

    fun addChild(basename: String, node: Node) {
        children[basename] = node
    }
    
    fun getChildren() : Collection<Node> {
        return children.values
    }
    
    fun getChild(basename: String) : Node? {
        return children[basename]
    }
}

enum class OutputType(private val type: Int){
    CHANGE_DIR(1), LIST_FILES(2), DIR_NAME(3), FILE_INFO(4),
    IGNORE(0)
}
class ConsoleOutput(val type: OutputType, val args: List<String>)

interface DirVisitor {
    fun visit(node: Node)
}
class LoggerDirVisitor : DirVisitor {
    override fun visit(node: Node) {
        println("${node.basename}  (${node.size})")
    }
}
class SizeAggregationVisitor: DirVisitor {
    override fun visit(node: Node) {
        if (node.isDir) {
            node.size += node.getChildren().sumOf { it.size }
        }
    }
}
class DeleteCandidateVisitor(
    private val shouldBeDeleted : (node: Node) -> Boolean): DirVisitor {
    
    val candidates: MutableList<Node> = mutableListOf()
    override fun visit(node: Node) {
        if (node.isDir && shouldBeDeleted(node)) {
            candidates.add(node)            
        }
    }
}
fun main() {
    
    val getArgByIndex : (String, Int) -> String = {output: String, index: Int ->
        output.removePrefix("$ ").split(" ").getOrNull(index) ?: ""}
    
    val sizeCounter : SizeAggregationVisitor = SizeAggregationVisitor()
    val logger = LoggerDirVisitor()
    
    fun parseCommands(output: String) : ConsoleOutput {
        return when (getArgByIndex(output, 0)) {
            "cd" -> ConsoleOutput(OutputType.CHANGE_DIR, listOf(getArgByIndex(output, 1)))
            "ls" -> ConsoleOutput(OutputType.LIST_FILES, listOf())
            "dir" -> ConsoleOutput(OutputType.DIR_NAME, listOf(getArgByIndex(output, 1)))
            null -> ConsoleOutput(OutputType.IGNORE, listOf())
            else -> ConsoleOutput(OutputType.FILE_INFO,
                listOf(getArgByIndex(output, 0), getArgByIndex(output, 1)))
        }
    }
    
    fun processOutput(consoleOutput: ConsoleOutput, current: Node) : Node {
        return when (consoleOutput.type) {
            OutputType.CHANGE_DIR -> {
                if (consoleOutput.args[0] == "..") {
                    return current.parent ?: throw Exception("Invalid dir")
                }
                else if (consoleOutput.args[0] == "/") {
                    var root : Node? = current
                    while(root?.parent != null) {
                        root = root.parent
                    }
                    return root ?: throw Exception("no root")
                }
                return current.getChild(consoleOutput.args[0])
                    ?: throw Exception("Invalid dir")
            }
            OutputType.FILE_INFO -> {
                val nodeName = consoleOutput.args[1]
                val child: Node = Node(
                    Integer.parseInt(consoleOutput.args[0]),
                    current, false, "${current.basename}/$nodeName")
                current.addChild(
                    nodeName,
                    child
                )
                return current
            }
            OutputType.DIR_NAME -> {
                val nodeName = consoleOutput.args[0]
                val child: Node =
                    Node(0, 
                        current, true, "${current.basename}/$nodeName")
                current.addChild(
                    nodeName,
                    child
                )
                return current
            }

            else -> current
        }
    }
    
    fun traverseDir(current : Node, visitor: DirVisitor) {
        for (child in current.getChildren()) traverseDir(child, visitor)
        visitor.visit(current)
    }
    
    fun buildFilesystem(input: List<String>) : Node {
        val root : Node = Node(0, null, true, "")
        var tail : Node = root
        input.map {
            parseCommands(it)
        }.map {
            tail = processOutput(it, tail)
        }
        return root
    }
    
    fun part1(input: List<String>) : Int {
        val deleteLowerThan10k = {node: Node -> node.size <= 100_000}
        val deleteCandidateLookup = DeleteCandidateVisitor(deleteLowerThan10k)
        val root = buildFilesystem(input)
        traverseDir(root, sizeCounter)
        traverseDir(root, deleteCandidateLookup)
        
        return deleteCandidateLookup.candidates.sumOf { it.size}
    }
    
    fun part2(input: List<String>) : Int {
        val diskSize = 70_000_000
        val allowedMinimumSize = 30_000_000
        
        val root = buildFilesystem(input)
        traverseDir(root, sizeCounter)
        val requiredThreshold = 
            abs((diskSize - root.size) - allowedMinimumSize)
        val deleteBiggerThanThreshold = {node: Node -> node.size >= requiredThreshold}
        
        val deleteCandidateLookup = DeleteCandidateVisitor(deleteBiggerThanThreshold)
        //traverseDir(root, logger) // debugging
        traverseDir(root, deleteCandidateLookup)

        return deleteCandidateLookup.candidates.minOf { it.size }
    }
    
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 95437)
    check(part2(testInput) == 24933642)
    
    val input = readInput("Day07")
    check(part1(input) == 1086293)
    check(part2(input) == 366028)
}