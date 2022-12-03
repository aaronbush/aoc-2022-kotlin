sealed class RpsPlay(val points: Int) {
    companion object {
        fun of(s: String): RpsPlay {
            return when (s) {
                "A" -> ROCK
                "X" -> ROCK
                "B" -> PAPER
                "Y" -> PAPER
                "C" -> SCISSORS
                "Z" -> SCISSORS
                else -> throw IllegalArgumentException("unknown")
            }
        }
    }
}

object ROCK : RpsPlay(1)
object PAPER : RpsPlay(2)
object SCISSORS : RpsPlay(3)

enum class RPS_RESULT {
    WIN, LOSE, DRAW
}

operator fun RpsPlay.compareTo(other: RpsPlay): Int {
    return if (other == this) {
        0
    } else {
        when (this) {
            ROCK -> if (other == PAPER) -1 else 1
            PAPER -> if (other == SCISSORS) -1 else 1
            SCISSORS -> if (other == ROCK) -1 else 1
        }
    }
}

fun main() {
    fun String.asResult(): RPS_RESULT {
        return when (this) {
            "X" -> RPS_RESULT.LOSE
            "Y" -> RPS_RESULT.DRAW
            "Z" -> RPS_RESULT.WIN
            else -> throw IllegalArgumentException(this)
        }
    }

    fun getScores(results: List<Pair<RpsPlay, RPS_RESULT>>) = results.map {
        it.first.points to
                when (it.second) {
                    RPS_RESULT.WIN -> 6
                    RPS_RESULT.DRAW -> 3
                    else -> 0
                }
    }

    fun part1(input: List<String>): Int {
        val plays = input.map {
            val plays = it.split(" ")
            RpsPlay.of(plays[0]) to RpsPlay.of(plays[1])
        }
//        println(plays)
        val results = plays.map {
            it.second to if (it.first == it.second)
                RPS_RESULT.DRAW
            else if (it.first < it.second)
                RPS_RESULT.WIN
            else
                RPS_RESULT.LOSE
        }
//    println(results)
        val scores = getScores(results)
//        println(scores)

        return scores.sumOf { it.first + it.second }
    }

    fun part2(input: List<String>): Int {
        val expects = input.map {
            val plays = it.split(" ")
            RpsPlay.of(plays[0]) to plays[1].asResult()
        }
        val results = expects.map {
            when (it.second) {
                RPS_RESULT.DRAW -> it.first
                RPS_RESULT.LOSE -> {
                    when (it.first) {
                        ROCK -> SCISSORS
                        PAPER -> ROCK
                        SCISSORS -> PAPER
                    }
                }
                RPS_RESULT.WIN -> {
                    when (it.first) {
                        ROCK -> PAPER
                        PAPER -> SCISSORS
                        SCISSORS -> ROCK
                    }
                }
            } to it.second
        }
        val scores = getScores(results)
//        println(scores)
        return scores.sumOf { it.first + it.second }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
