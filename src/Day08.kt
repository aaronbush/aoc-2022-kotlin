enum class ForestDirection { N, S, E, W }
enum class TreeVisibility { VISIBLE, NOT_VISIBLE, UNKNOWN }

fun main() {
    data class Tree(
        val row: Int,
        val column: Int,
        val height: Int,
        var visibility: TreeVisibility = TreeVisibility.UNKNOWN,
        var treesVisible: Int = 0
    )

    data class Forest(val trees: List<Tree>) {
        val maxRow: Int
            get() {
                val last = trees.last()
                return last.row
            }

        val maxColumn: Int
            get() {
                val last = trees.last()
                return last.column
            }

        override fun toString(): String {
            var output = ""
            trees.chunked(maxColumn + 1).forEach { row ->
                row.forEach { tree ->
                    if (tree.visibility == TreeVisibility.VISIBLE) {
                        output += "\u001b[31m"
                        output += "${tree.height}(${tree.treesVisible})* "
                        output += "\u001b[0m"
                    } else if (tree.visibility == TreeVisibility.NOT_VISIBLE)
                        output += "${tree.height}(${tree.treesVisible})- "
                    else
                        output += "${tree.height}(${tree.treesVisible})? "
                }
                output += "\n"
            }
            return output
        }

        fun markBorder() {
            trees.filter { it.row == 0 || it.row == maxRow || it.column == 0 || it.column == maxColumn }
                .forEach { it.visibility = TreeVisibility.VISIBLE }
        }

        fun findTree(direction: ForestDirection, ofTree: Tree): Tree? {
            val (row, column) = when (direction) {
                ForestDirection.N -> ofTree.row - 1 to ofTree.column
                ForestDirection.S -> ofTree.row + 1 to ofTree.column
                ForestDirection.E -> ofTree.row to ofTree.column + 1
                ForestDirection.W -> ofTree.row to ofTree.column - 1
            }
            return trees.find { it.row == row && it.column == column }
        }

        fun toEdge(direction: ForestDirection, fromTree: Tree): List<Tree> {
            val remainingTrees = mutableListOf<Tree>()
            var next: Tree? = findTree(direction, fromTree)
            while (next != null) {
                remainingTrees += next
                next = findTree(direction, next)
            }
            return remainingTrees
        }

        fun Tree.visibleVia(direction: ForestDirection): TreeVisibility {
            // check to edge of forest
            val remainingTrees = toEdge(direction, this)
            val visibilityToEdge = remainingTrees.map {
                if (it.height >= this.height)
                    TreeVisibility.NOT_VISIBLE
                else
                    TreeVisibility.VISIBLE

            }
            return if (visibilityToEdge.contains(TreeVisibility.NOT_VISIBLE)) {
                TreeVisibility.NOT_VISIBLE
            } else
                TreeVisibility.VISIBLE
        }

        private fun Tree.visibility(): TreeVisibility {
            val visibilityStates = setOf(
                this.visibleVia(ForestDirection.N),
                this.visibleVia(ForestDirection.S),
                this.visibleVia(ForestDirection.E),
                this.visibleVia(ForestDirection.W)
            )
            return if (visibilityStates.contains(TreeVisibility.VISIBLE))
                TreeVisibility.VISIBLE
            else
                TreeVisibility.NOT_VISIBLE
        }

        fun markTrees() {
            trees.filter { it.row in 1 until maxRow && it.column in 1 until maxColumn }
                .forEach { it.visibility = it.visibility() }
        }

        fun updateVisibleTrees() {
            fun numTreesInLineOfSight(direction: ForestDirection, tree: Tree): Int {
                val lineOfSight = toEdge(direction, tree).takeWhile { it.height < tree.height }
                return if (lineOfSight.size < toEdge(direction, tree).size)
                    lineOfSight.size + 1
                else lineOfSight.size
            }

            // don't need to check edges as they are 0 - todo: optimize
            trees.forEach { tree ->
                val n = numTreesInLineOfSight(ForestDirection.N, tree)
                val e = numTreesInLineOfSight(ForestDirection.E, tree)
                val s = numTreesInLineOfSight(ForestDirection.S, tree)
                val w = numTreesInLineOfSight(ForestDirection.W, tree)
                tree.treesVisible = n * s * e * w
            }
        }
    }

    fun part1(input: List<String>): Int {
        val trees = input.flatMapIndexed { row, line ->
            line.mapIndexed { column, tree ->
                Tree(row, column, tree.toString().toInt())
            }
        }
        val forest = Forest(trees)
        forest.markBorder()
        forest.markTrees()
//        println(forest)
        return forest.trees.count { it.visibility == TreeVisibility.VISIBLE }
    }

    fun part2(input: List<String>): Int {
        val trees = input.flatMapIndexed { row, line ->
            line.mapIndexed { column, tree ->
                Tree(row, column, tree.toString().toInt())
            }
        }
        val forest = Forest(trees)
        forest.updateVisibleTrees()
//        println(forest)
        return forest.trees.maxOf { it.treesVisible }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 8)

    val input = readInput("Day08")
//    println("p1: ${part1(input)}")
    println("p2: ${part2(input)}")
}

