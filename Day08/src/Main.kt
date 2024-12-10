import java.io.File

fun isInBounds(coords: Pair<Int, Int>, maxX: Int, maxY: Int): Boolean {
    return coords.first in 0..maxX && coords.second in 0..maxY
}

fun main() {
    val fileList = mutableListOf<String>()
    val allAntennasPositions = mutableSetOf<Pair<Int, Int>>()
    val antenaFreqPositions = mutableMapOf<Char, MutableSet<Pair<Int, Int>>>()

    var rowIdx = 0
    File("input.txt").forEachLine {
        val line = it.trim()
        fileList.add(line)
        line.forEachIndexed { colIdx, ant ->
            if (ant == '.') return@forEachIndexed
            val coords = Pair(rowIdx, colIdx)
            allAntennasPositions.add(coords)
            antenaFreqPositions.computeIfAbsent(ant) { mutableSetOf() }.add(coords)
        }
        rowIdx++
    }

    val fileXSize = fileList.size
    val fileYSize = fileList[0].length

    // PART 1
    val antinodesPositions = mutableSetOf<Pair<Int, Int>>()
    for ((_, coordsList) in antenaFreqPositions) {
        for (coordA in coordsList) {
            for (coordB in coordsList) {
                if (coordA == coordB) continue

                val xDifference = coordB.first - coordA.first
                val yDifference = coordB.second - coordA.second
                val antinodePosition = Pair(coordB.first + xDifference, coordB.second + yDifference)

                if (!isInBounds(antinodePosition, fileXSize - 1, fileYSize - 1)) continue

                antinodesPositions.add(antinodePosition)
            }
        }
    }

    var uniqueLocations = antinodesPositions.size
    println("There are $uniqueLocations unique locations")

    // PART 2
    antinodesPositions.clear()
    for ((_, coordsList) in antenaFreqPositions) {
        for (coordA in coordsList) {
            for (coordB in coordsList) {
                if (coordA == coordB) continue

                antinodesPositions.add(coordB)

                var xDifference = coordB.first - coordA.first
                var yDifference = coordB.second - coordA.second
                var antinodePosition = Pair(coordB.first + xDifference, coordB.second + yDifference)

                while (isInBounds(antinodePosition, fileXSize - 1, fileYSize - 1)) {
                    antinodesPositions.add(antinodePosition)

                    xDifference += (coordB.first - coordA.first)
                    yDifference += (coordB.second - coordA.second)
                    antinodePosition = Pair(coordB.first + xDifference, coordB.second + yDifference)
                }
            }
        }
    }

    uniqueLocations = antinodesPositions.size
    println("There are $uniqueLocations unique locations with harmonics")
}