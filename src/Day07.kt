sealed class FileSystemObject
data class FileSystemDirectory(
    val name: String,
    val children: MutableMap<String, FileSystemObject>,
    val parent: FileSystemDirectory?, // todo: squash nullable parent.
    var size: Long = 0
) : FileSystemObject() {
    override fun toString() =
        "FileSystemDirectory(name='$name', children=$children, parent=${parent?.name}, size=$size)"

    fun cd(name: String) = children[name]!! as FileSystemDirectory // let it fail if not a dir

    fun <R> map(f: (FileSystemDirectory) -> R): List<R> {
        fun loop(d: FileSystemDirectory, accum: MutableList<R>) {
            accum += f(d)
            d.children.values.filterIsInstance<FileSystemDirectory>().map { loop(it, accum) }
        }

        val result = mutableListOf<R>()
        loop(this, result)
        return result
    }

    fun forEachDir(f: (FileSystemDirectory) -> Unit) {
        f(this)
        this.children.values.filterIsInstance<FileSystemDirectory>().forEach { it.forEachDir(f) }
    }

    fun calculateSize() {
        fun loop(pwd: FileSystemDirectory): Long {
            val sumOfFiles = pwd.children.values.filterIsInstance<FileSystemFile>().sumOf { it.size }
            pwd.size += sumOfFiles

            if (pwd.children.values.filterIsInstance<FileSystemDirectory>().isEmpty()) {
                return sumOfFiles
            }
            val subDirSize = pwd.children.values.filterIsInstance<FileSystemDirectory>().sumOf {
                loop(it)
            }
            pwd.size += subDirSize
            return subDirSize + sumOfFiles
        }

        loop(this)
    }
}

data class FileSystemFile(val name: String, val size: Long) : FileSystemObject()

fun main() {

    fun parseOutput(input: List<String>): FileSystemDirectory {
        val root = FileSystemDirectory("/", mutableMapOf(), null)
        var pwd = root
        input.forEach { line ->
            when (line.take(1)) {
                "$" -> { // cmd line
                    val cmd = line.split(" ")
                    when (cmd[1]) {
                        "cd" -> {
                            pwd = when (cmd[2]) {
                                "/" -> root
                                ".." -> pwd.parent!!
                                else -> pwd.cd(cmd[2])
                            }
                        }

                        else -> {} // do we care?
                    }
                } // output line
                else -> {
                    val type = line.split(" ")
                    when (type[0]) {
                        "dir" -> {
                            val d = FileSystemDirectory(type[1], mutableMapOf(), pwd)
                            pwd.children[d.name] = d
                        }

                        else -> {
                            val f = FileSystemFile(type[1], type[0].toLong())
                            pwd.children[f.name] = f
                        }
                    }
                }
            }
        }
        return root
    }

    fun part1(input: List<String>): Long {
        val root = parseOutput(input)
        root.calculateSize()
        val withinSpec = root.map { if (it.size <= 100000) it else null }.filterNotNull()
        return withinSpec.sumOf { it.size }
    }

    fun part2(input: List<String>): Long {
        val root = parseOutput(input)
        root.calculateSize()

        val totalSize = 70000000
        val neededSize = 30000000
        val free = totalSize - root.size
        val mustFree = neededSize - free

        val candidates = root.map { if (it.size >= mustFree) it else null }.filterNotNull()
        val x = candidates.sortedBy { it.size }.map { it.size }
        return x.first()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 95437L)
    check(part2(testInput) == 24933642L)

    val input = readInput("Day07")
    println("p1: ${part1(input)}")
    println("p2: ${part2(input)}")
}