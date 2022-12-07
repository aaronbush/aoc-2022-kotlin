fun main() {
    fun locateMarker(input: String, markerLength: Int): Int {
        val slices = input.windowed(markerLength, step = 1).asSequence()
        val sliceLocation = slices.indexOfFirst { it.toSet().size == markerLength }
        return sliceLocation + markerLength
    }

    fun part1(input: String) =
        locateMarker(input, 4)


    fun part2(input: String) =
        locateMarker(input, 14)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput[0]) == 7)
    check(part2(testInput[0]) == 19)

    val input = readInput("Day06")
    println("p1: ${part1(input[0])}")
    println("p2: ${part2(input[0])}")
}

