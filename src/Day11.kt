class Monkey(private val items: MutableList<Long>, val op: (Long) -> Long, val test: (Long) -> Boolean) {
    lateinit var testPassesMonkey: Monkey
    lateinit var testFailsMonkey: Monkey
    var worryReducer = { w: Long -> w / 3 }
    var numInspections = 0L

    fun takeTurn() {
        items.forEach {
            numInspections++
            val newWorry = worryReducer(op(it))
            if (test(newWorry))
                testPassesMonkey.items.add(newWorry)
            else
                testFailsMonkey.items.add(newWorry)
        }
        items.clear()
    }

    override fun toString(): String {
        return "inspected: $numInspections / $items"
    }
}

fun main() {
    fun gameState(monkeys: List<Monkey>) {
        monkeys.forEachIndexed { index, monkey ->
            println("$index: $monkey")
        }
    }

    fun part1(monkeys: List<Monkey>): Long {
        repeat(20) {
            monkeys.forEach {
                it.takeTurn()
            }
            println("round ${it + 1}> ")
            gameState(monkeys)
        }
        val (top1, top2) = monkeys.sortedBy { it.numInspections }.takeLast(2)
        return top1.numInspections * top2.numInspections
    }

    fun part2(monkeys: List<Monkey>): Long {
        repeat(10000) { roundNum
            ->
            monkeys.forEach {
                it.takeTurn()
            }
            if ((roundNum + 1) % 1000 == 0 || roundNum == 0 || roundNum == 19) {
                println("round ${roundNum + 1}> ")
                gameState(monkeys)
            }
        }
        val (top1, top2) = monkeys.sortedBy { it.numInspections }.takeLast(2)
        return top1.numInspections * top2.numInspections
    }


    fun p1t1(): List<Monkey> {
        val m0 = Monkey(mutableListOf(79, 98), { i -> i * 19 }, { i -> i % 23 == 0L })
        val m1 = Monkey(mutableListOf(54, 65, 75, 74), { i -> i + 6 }, { i -> i % 19 == 0L })
        val m2 = Monkey(mutableListOf(79, 60, 97), { i -> i * i }, { i -> i % 13 == 0L })
        val m3 = Monkey(mutableListOf(74), { i -> i + 3 }, { i -> i % 17 == 0L })

        val monkeys = mutableListOf<Monkey>()
        monkeys += m0
        monkeys += m1
        monkeys += m2
        monkeys += m3

        m0.testPassesMonkey = m2
        m0.testFailsMonkey = m3

        m1.testPassesMonkey = m2
        m1.testFailsMonkey = m0

        m2.testPassesMonkey = m1
        m2.testFailsMonkey = m3

        m3.testPassesMonkey = m0
        m3.testFailsMonkey = m1

        // for p2 only
        val mod = 23 * 19 * 13 * 17
        m0.worryReducer = { x -> x % mod }
        m1.worryReducer = { x -> x % mod }
        m2.worryReducer = { x -> x % mod }
        m3.worryReducer = { x -> x % mod }

        return monkeys
    }

    fun p1(): List<Monkey> {
        val m0 = Monkey(mutableListOf(83, 62, 93), { i -> i * 17 }, { i -> i % 2 == 0L })
        val m1 = Monkey(mutableListOf(90, 55), { i -> i + 1 }, { i -> i % 17 == 0L })
        val m2 = Monkey(mutableListOf(91, 78, 80, 97, 79, 88), { i -> i + 3 }, { i -> i % 19 == 0L })
        val m3 = Monkey(mutableListOf(64, 80, 83, 89, 59), { i -> i + 5 }, { i -> i % 3 == 0L })
        val m4 = Monkey(mutableListOf(98, 92, 99, 51), { i -> i * i }, { i -> i % 5 == 0L })
        val m5 = Monkey(mutableListOf(68, 57, 95, 85, 98, 75, 98, 75), { i -> i + 2 }, { i -> i % 13 == 0L })
        val m6 = Monkey(mutableListOf(74), { i -> i + 4 }, { i -> i % 7 == 0L })
        val m7 = Monkey(mutableListOf(68, 64, 60, 68, 87, 80, 82), { i -> i * 19 }, { i -> i % 11 == 0L })

        val monkeys = mutableListOf<Monkey>()
        monkeys += m0
        monkeys += m1
        monkeys += m2
        monkeys += m3
        monkeys += m4
        monkeys += m5
        monkeys += m6
        monkeys += m7

        m0.testPassesMonkey = m1
        m0.testFailsMonkey = m6

        m1.testPassesMonkey = m6
        m1.testFailsMonkey = m3

        m2.testPassesMonkey = m7
        m2.testFailsMonkey = m5

        m3.testPassesMonkey = m7
        m3.testFailsMonkey = m2

        m4.testPassesMonkey = m0
        m4.testFailsMonkey = m1

        m5.testPassesMonkey = m4
        m5.testFailsMonkey = m0

        m6.testPassesMonkey = m3
        m6.testFailsMonkey = m2

        m7.testPassesMonkey = m4
        m7.testFailsMonkey = m5

        // for p2 only
        val mod = 2 * 17 * 19 * 3 * 5 * 13 * 7 * 11
        val wr = { x: Long -> x % mod }
        m0.worryReducer = wr
        m1.worryReducer = wr
        m2.worryReducer = wr
        m3.worryReducer = wr
        m4.worryReducer = wr
        m5.worryReducer = wr
        m6.worryReducer = wr
        m7.worryReducer = wr

        return monkeys
    }

    // test if implementation meets criteria from the description, like:
//    check(part1(p1t1()) == 10605)
//    check(part2(p1t1()) == 10605L)

//    println("p1: ${part1(p1())}")
    println("p2: ${part2(p1())}")
}

