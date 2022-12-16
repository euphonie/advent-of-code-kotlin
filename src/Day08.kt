fun main() {
    
    fun buildMatrix(input: List<String>) : Array<IntArray> {
        return input.map {
            it.map { v -> v.digitToInt() }.toIntArray()
        }.toTypedArray()
    }
    
    fun printMatrix(matrix: Array<IntArray>) {
        println(
            matrix.map { 
                it.contentToString() + "\n"
            }
        )
    }
    
    fun isVisible(i:Int, j:Int, matrix: Array<IntArray>) : Boolean {
        val height = matrix.size
        val width = matrix[0].size
        
        // find first blocking tree at each direction for given coordinate
        val maxNorth = 
            if (matrix[i-1][j] < matrix[i][j]) (i-1 downTo 0).maxOf { matrix[it][j] } 
            else matrix[i-1][j]
        val maxSouth =
            if (matrix[i+1][j] < matrix[i][j]) (i+1 until height).maxOf { matrix[it][j] } 
            else matrix[i+1][j]
        val maxEast =
            if (matrix[i][j+1] < matrix[i][j]) (j+1 until width).maxOf { matrix[i][it] } 
            else matrix[i][j+1]
        val maxWest =
            if (matrix[i][j-1] < matrix[i][j]) (j-1 downTo 0).maxOf { matrix[i][it] } 
            else matrix[i][j-1]
        
        return arrayOf(maxNorth, maxSouth, maxEast, maxWest).any { matrix[i][j] > it }
    }
    
    fun part1(input: List<String>) : Int {
        val matrix = buildMatrix(input)
        val height = matrix.size 
        val width = matrix[0].size
        var counter = 0

        for (i in 1 until height -1) {
            for (j in 1 until width -1) {
                counter += if (isVisible(i, j, matrix)) 1 else 0
            }
        }
        return counter + (2*height) + (2*width) - 4
    }
    
    
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 21)
    
    val input = readInput("Day08")
    check(part1(input) == 1823)
}