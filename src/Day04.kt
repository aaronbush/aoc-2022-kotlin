fun main() {

    fun String.toIntRange(): IntRange {
        val parts = this.split("-", limit = 2)
        return parts[0].toInt()..parts[1].toInt()
    }

    fun IntRange.subSetOf(other: IntRange) =
        this.first >= other.first && this.last <= other.last

    fun IntRange.overlaps(other: IntRange) =
        this.last >= other.first && this.first <= other.last


    fun loadSections(input: List<String>): List<Pair<IntRange, IntRange>> {
        val sectionRanges = input.map { line ->
            val parts = line.split(",", limit = 2)
            val sectionA = parts[0].toIntRange()
            val sectionB = parts[1].toIntRange()
            sectionA to sectionB
        }
        return sectionRanges
    }

    fun part1(input: List<String>): Int {
        val sectionRanges = loadSections(input)
        val sectionsContained = sectionRanges.map {
            it.first.subSetOf(it.second) || it.second.subSetOf(it.first)
        }
        return sectionsContained.count { it }
    }

    fun part2(input: List<String>): Int {
        val sectionRanges = loadSections(input)
        val sectionOverlaps = sectionRanges.map {
            it.first.overlaps(it.second) || it.second.overlaps(it.first)
        }
        return sectionOverlaps.count { it }
    }
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day04")
    println("p1: ${part1(input)}")
    println("p2: ${part2(input)}")
}


