import java.util.stream.Collectors.toSet

fun main() {
    val priority = ('a'..'z').withIndex().associate { it.value to it.index + 1 } +
            ('a'..'z').withIndex().associate { it.value.uppercaseChar() to it.index + 27 }

    fun String.bisect() = this.chunked(this.length / 2)

    fun part1(input: List<String>): Int {
        val compartments = input.map { it.bisect() }

        val items = compartments.map { packContents ->
            packContents.map { it.toSet() }
        }
        val commonItems = items.flatMap { packContents ->
            packContents[0].intersect(packContents[1])
        }
        val packItemPriority = commonItems.map { priority.getOrDefault(it, 0) }
//        println(compartments)
//        println(items)
//        println(commonItems)
//        println(packItemPriority)
        return packItemPriority.sum()
    }

    fun part2(input: List<String>): Int {
        val groups = input.chunked(3)

        val groupsItems = groups.map { groupItems ->
            groupItems.map { it.toSet() }
        }

        val commonItems = groupsItems.flatMap { it[0].intersect(it[1]).intersect(it[2]) }
        val groupItemPriority = commonItems.map { priority.getOrDefault(it, 0) }
//        println(groupsItems)
//        println(commonItems)
//        println(groupItemPriority)
        return groupItemPriority.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
