import java.lang.Integer.max
import kotlin.math.abs

fun main() {

    val moveFuns = mapOf(
        "R" to fun(h: RopePoint) = h.copy(first = h.first + 1),
        "L" to fun(h: RopePoint) = h.copy(first = h.first - 1),
        "U" to fun(h: RopePoint) = h.copy(second = h.second + 1),
        "D" to fun(h: RopePoint) = h.copy(second = h.second - 1),
    )

    fun part1(input: List<String>): Int {
        val startPoint = RopePoint(0, 0)
        var headAt = startPoint.copy()
        var tailAt = startPoint.copy()
        val tailPoints = mutableSetOf<RopePoint>(startPoint)

        input.forEach { motion ->
            val (dir, len) = motion.split(" ", limit = 2)
            val f = moveFuns[dir]!!
            repeat(len.toInt()) {
                headAt = f(headAt)
                if (headAt - tailAt == 2) {
                    tailAt = tailAt.advanceTo(headAt)
                    tailPoints += tailAt
                }
//                println("tailPoints = ${tailPoints}")
            }
        }
        return tailPoints.size
    }

    fun part2(input: List<String>): Int {
        val startPoint = RopePoint(0, 0)
        val headAt = startPoint.copy()
        val pointLocations = (mutableListOf(headAt) +
                MutableList(9) { startPoint.copy() }).toMutableList() // 9-tails
        val tailPoints = mutableSetOf(startPoint)

        input.forEach { motion ->
            val (dir, len) = motion.split(" ", limit = 2)
//            println("dir = ${dir} / len = $len")
            val f = moveFuns[dir]!!

            repeat(len.toInt()) {
                val headPoint = f(pointLocations.first())
                pointLocations[0] = headPoint // store updated head
                var pointFollowed = headPoint
                pointLocations.drop(1).forEachIndexed { n, tail -> // all tail points
                    var newTailPosition = tail
                    while (pointFollowed - newTailPosition > 1) {
                        newTailPosition = tail.advanceTo(pointFollowed)
//                        println("advancing ${n+1} to ${newTailPosition}")
                        // moving the final tail -> update tail points
                        if (n + 1 ==9) {
                            tailPoints += newTailPosition
                        }
                    }
                    pointLocations[n + 1] = newTailPosition
                    pointFollowed = pointLocations[n + 1]
                }
            }
//            println("nextLocations = ${pointLocations}")
//            println("tailPoints = ${tailPoints}")
        }
        return tailPoints.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test2")
//    check(part1(testInput) == 13)
//    check(part2(testInput) == 36)

    val input = readInput("Day09")
//    println("p1: ${part1(input)}")
    println("p2: ${part2(input)}")


    // some 'tests'
    /*
        println(RopePoint(0, 2) - RopePoint(0, 0)) // 2
        println(RopePoint(2, 0) - RopePoint(0, 0)) // 2
        println(RopePoint(2, 1) - RopePoint(0, 0)) // 2
        println(RopePoint(1, 2) - RopePoint(0, 0)) // 2
        println(RopePoint(1, 1) - RopePoint(0, 0)) // 1
        println(RopePoint(0, 0) - RopePoint(0, 0)) // 0
        println(RopePoint(2, 0) - RopePoint(0, 0)) // 2
        println(RopePoint(0, 0) - RopePoint(2, 2)) // 2
*/
    /*
        println(RopePoint(0, 0).advanceTo(RopePoint(0, 2))) // 0,1
        println(RopePoint(0, 0).advanceTo(RopePoint(0, -2))) // 0,-1
        println(RopePoint(0, 0).advanceTo(RopePoint(2, 0))) // 1,0
        println(RopePoint(0, 0).advanceTo(RopePoint(-2, 0))) // -1,0
        println(RopePoint(0, 0).advanceTo(RopePoint(1, 2))) // 1,1
        println(RopePoint(0, 0).advanceTo(RopePoint(1, -2))) // 1,-1
        println(RopePoint(0, 0).advanceTo(RopePoint(-1, -2))) // -1,-1
        println(RopePoint(0, 0).advanceTo(RopePoint(-1, 2))) // -1,1
        */

}

typealias RopePoint = Pair<Int, Int>

// what new coordinate should 'this' advance to get close to the other
fun RopePoint.advanceTo(other: RopePoint): RopePoint {
    // TODO: what if locations are equal?
    if (this == other) {
        return this
    }

    val delta1 = other.first - first
    val delta2 = other.second - second

    return if (delta1 == 0) { // mov v
        if (delta2 > 0) {
            RopePoint(first, second + 1)
        } else {
            RopePoint(first, second - 1)
        }
    } else if (delta2 == 0) { // move h
        if (delta1 > 0) {
            RopePoint(first + 1, second)
        } else {
            RopePoint(first - 1, second)
        }
    } else {
        if (delta1 > 0 && delta2 > 0) {
            // add one to each
            RopePoint(first + 1, second + 1)
        } else if (delta1 > 0) { //  && delta2 < 0) {
            // add to delta1
            // sub delta2
            RopePoint(first + 1, second - 1)
        } else if (delta2 < 0) { // && delta2 < 0) {
            // sub from both
            RopePoint(first - 1, second - 1)
        } else {
            // sub delta1
            // add delta2
            RopePoint(first - 1, second + 1)
        }
    }
}

operator fun RopePoint.minus(other: RopePoint): Int {
    val delta1 = abs(this.first - other.first)
    val delta2 = abs(this.second - other.second)

    val diff = if (delta1 == 0 || delta2 == 0) { // same row or col
        max(delta1, delta2)
    }  else if (delta1 + delta2 in 3..4)// diag
        2
    else
        delta1 + delta2 - 1

    check(diff in 0..2) { "$diff ($delta1, $delta2) outside range for $this - $other" }

    return diff
}

