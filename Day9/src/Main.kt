import java.io.File

fun main() {
    val diskMap = File("test.txt").readText().trim().map { it.digitToInt() }
    val diskContent = mutableListOf<String>()
    val diskFiles = ArrayDeque<Int>()

    // PART 1
    var fileId = 0
    diskMap.forEachIndexed { index, block ->
        if (index % 2 == 0) {
            diskContent.addAll(List(block) { "$fileId" })
            diskFiles.addAll(List(block) { fileId })
            fileId++
        } else {
            diskContent.addAll(List(block) { "." })
        }
    }

    val newDiskFiles = mutableListOf<Int>()
    val diskFilesSize = diskFiles.size
    diskContent.forEachIndexed { idx, file ->
        if (idx >= diskFilesSize) return@forEachIndexed

        if (file == ".") {
            newDiskFiles.add(diskFiles.removeLast())
        } else {
            newDiskFiles.add(file.toInt())
        }
    }

    val checksum = newDiskFiles.foldIndexed(0UL) { index, acc, file ->
        acc.plus((file*index).toUInt())
    }
    println("Checksum: $checksum")
}