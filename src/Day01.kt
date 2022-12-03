fun main() {
    fun loadInventory(input: List<String>): Map<Int, List<Int>> {
        val inventory = mutableMapOf<Int, MutableList<Int>>()
        var elf = 1
        input.forEach {
            if (it.isEmpty()) {
                elf++
            } else {
                val cals = inventory.getOrDefault(elf, mutableListOf())
                cals += it.toInt()
                inventory[elf] = cals
            }
        }

        return inventory
    }

    fun part1(input: List<String>): Int {
        val inventory = loadInventory(input)
        val result = inventory.map { entry ->
            entry.key to entry.value.sum()
        }.toMap()

        println(result)
        return result.maxOf { it.value }
    }

    fun part2(input: List<String>): Int {
        val inventory = loadInventory(input)
        val result = inventory.map { entry ->
            entry.key to entry.value.sum()
        }.toMap()
        val sorted = result.toSortedMap { o1, o2 ->
            if (result[o1]!! > result[o2]!!) -1 else 1 } // reverse order
        println(sorted.values.take(3))
        return sorted.values.take(3).sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
//    check(part1(testInput) == 24000)
    part2(testInput)

    val input = readInput("Day01")
//    println(part1(input))
    println(part2(input))
}
