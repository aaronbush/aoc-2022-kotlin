fun main() {
    fun process(input: List<String>): List<Int> {
        var xreg = 1
        var cycleCount = 0
        val regHistory = mutableListOf(xreg)
        input.forEach { line ->
            when (val op = line.substringBefore(" ")) {
                "addx" -> {
                    val arg = line.substringAfter(" ").toInt()
                    cycleCount++
                    regHistory += xreg
                    cycleCount++
                    xreg += arg
                    regHistory += xreg
                }

                "noop" -> {
                    regHistory += xreg
                    cycleCount++
                }

                else -> throw UnsupportedOperationException("$op not recognized")
            }
        }
        return regHistory
    }

    fun part1(input: List<String>): Int {
        val regHistory = process(input)
        val signals = mutableListOf(regHistory.take(20).last() * 20)
        regHistory.drop(20).chunked(40).forEachIndexed { i, l ->
            if (l.size == 40)
                signals += l.last() * (20 + (i + 1) * 40)
        }
        println("signals = $signals")
        return signals.sum()
    }

    fun part2(input: List<String>): Int {
        val regHistory = process(input)
        val screenBuffer = mutableListOf<Array<Array<String>>>()

        fun render(crt: Array<Array<String>>): String {
            return crt.mapIndexed { index, line ->
                "$index:  " + line.joinToString(separator = "")
            }.joinToString(separator = "\n")
        }

        regHistory.windowed(40 * 6, 40 * 6) { regWindow ->
            val crtContents = Array(6) { _ -> Array(40) { _ -> "." } }

            regWindow.forEachIndexed { index, regValue ->
                val row = index / 40
                val col = index - row * 40
                val spritePos = regValue - 1..regValue + 1
                if (col in spritePos) {
                    crtContents[row][col] = "#"
                }
            }
            screenBuffer += crtContents
        }

        screenBuffer.forEach {
            println(render(it))
        }
        // F C J A P J R E

        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
//    check(part1(testInput) == 13140)
//    check(part2(testInput) == 0)

    val input = readInput("Day10")
//    println("p1: ${part1(input)}")
    println("p2: ${part2(input)}")
}

