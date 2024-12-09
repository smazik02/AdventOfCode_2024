import java.io.File

fun main() {
    val diskMap = File("input.txt").readText().trim().map { it.digitToInt() }
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

    var checksum = newDiskFiles.foldIndexed(0UL) { index, acc, file ->
        acc.plus((file*index).toUInt())
    }
    println("Checksum: $checksum")

    // PART 2
    // BEWARE - IT'S *HORRIBLY* unoptimized, there is probably a better way to solve it, but I haven't figured it out
    val diskContentChunked = ArrayDeque<Int?>()
    val diskFilesChunked = ArrayDeque<List<Int>>()
    fileId = 0
    diskMap.forEachIndexed { index, block ->
        if (block == 0) return@forEachIndexed

        if (index % 2 == 0) {
            diskContentChunked.addAll(List(block) { fileId })
            diskFilesChunked.addFirst(List(block) { fileId })
            fileId++
        } else
            diskContentChunked.addAll(List(block) { null })
    }

    diskFilesChunked.forEach { chunk ->
        if (chunk.first() % 100 == 0) println(chunk.first())
        var spareRoomIdx = -1
        var chunkIdx = -1
        val compList = List(chunk.size) { null }
        for ((idx, window) in diskContentChunked.windowed(chunk.size).withIndex()) {
            if (window == compList && spareRoomIdx == -1)
                spareRoomIdx = idx
            if (window == chunk)
                chunkIdx = idx
            if (spareRoomIdx != -1 && chunkIdx != -1)
                break
        }

        if (spareRoomIdx == -1 || chunkIdx == -1 || chunkIdx < spareRoomIdx)
            return@forEach

        diskContentChunked
            .slice(spareRoomIdx..<spareRoomIdx+chunk.size)
            .forEachIndexed { idx, _ ->
                diskContentChunked[idx+spareRoomIdx] = chunk.first()
            }

        diskContentChunked
            .slice(chunkIdx..<chunkIdx+chunk.size)
            .forEachIndexed { idx, _ ->
                diskContentChunked[idx+chunkIdx] = null
            }
    }

    checksum = diskContentChunked.foldIndexed(0UL) { index, acc, file ->
        acc.plus(((file ?: 0)*index).toUInt())
    }
    println("Checksum: $checksum")
}