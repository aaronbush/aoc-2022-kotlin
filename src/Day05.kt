fun main() {

    data class MoveInstruction(val howMany: Int, val fromWhere: Int, val toWhere: Int)

    fun String.toMoveInstruction(): MoveInstruction {
        val (howMany, from, to) = this.split(" ").chunked(2).map { it.last().toInt() }
        return MoveInstruction(howMany, from, to)
    }

    fun parseStacks(input: List<String>): MutableMap<Int, ArrayDeque<String>> {
        val result = mutableMapOf<Int, ArrayDeque<String>>()
        val stackPattern = "\\[([a-zA-Z])]".toRegex()
        val stackLines = input.takeWhile { it.isNotEmpty() && !it.startsWith(" 1") } // up to the stack numbers
        val stacks = stackLines.map {
            it.chunked(4) // each stack is 4-chars '[A] ' (except for last - chunked still grabs it though as partial window)
        }
        stacks.map { stack ->
            stack.mapIndexed { index, s ->
                val currStack = result.getOrPut(index + 1) { ArrayDeque() }
                stackPattern.matchEntire(s.trim())?.destructured
                    ?.apply { currStack.addFirst(this.component1()) }
            }
        }
        return result
    }

    fun ArrayDeque<String>.removeLast(n: Int): List<String> {
        val result = mutableListOf<String>()
        repeat(n) {
            result += this.removeLast()
        }
        return result
    }

    fun ArrayDeque<String>.removeLastTogether(n: Int) = removeLast(n).reversed()


    fun moveCrates(input: List<String>, f: (dq: ArrayDeque<String>, n: MoveInstruction) -> List<String>): String {
        val stacks = parseStacks(input)
        val moves = input.takeLastWhile { it.isNotBlank() }.map { it.toMoveInstruction() }

        moves.forEach { move ->
            val toMoveCrates = stacks[move.fromWhere]!!.let { f(it, move) }
            stacks[move.toWhere]!!.addAll(toMoveCrates)
        }
        return stacks.values.joinToString(separator = "") { it.last() }
    }

    fun part1(input: List<String>) =
        moveCrates(input) { d, m -> d.removeLast(m.howMany) }

    fun part2(input: List<String>) =
        moveCrates(input) { d, m -> d.removeLastTogether(m.howMany) }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")

    val input = readInput("Day05")
    println("p1: ${part1(input)}")
    println("p2: ${part2(input)}")
}