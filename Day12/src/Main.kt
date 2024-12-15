import java.io.File
import kotlin.math.max
import kotlin.math.min

data class Point(val x: Int, val y: Int)

var fileXSize = 0
var fileYSize = 0
val fileContent = mutableListOf<String>()
val groupIdRepresent = mutableListOf<MutableList<Int>>()

val plantPlots = mutableMapOf<Int, MutableSet<Point>>()
val plantFences = mutableMapOf<Int, Int>()
val plantFenceLocations = mutableMapOf<Int, MutableSet<Point>>()

val visitedCoords = mutableSetOf<Point>()

val minCoordsForId = linkedMapOf<Int, Point>()
val maxCoordsForId = mutableMapOf<Int, Point>()

fun isWithinOuterPerimeter(checkedCoords: Point) =
    checkedCoords.x == -1 || checkedCoords.x == fileXSize || checkedCoords.y == -1 || checkedCoords.y == fileYSize

fun isWithinBounds(checkedCoords: Point, maxX: Int, maxY: Int) =
    checkedCoords.x in 0..maxX && checkedCoords.y in 0..maxY

fun plantGroupDFS(coords: Point, plant: Char, groupId: Int, groupCoords: MutableSet<Point>) {
    visitedCoords.add(coords)
    groupCoords.add(coords)
    groupIdRepresent[coords.x][coords.y] = groupId

    val neighCoords = mutableListOf(
        Point(coords.x - 1, coords.y),
        Point(coords.x, coords.y + 1),
        Point(coords.x + 1, coords.y),
        Point(coords.x, coords.y - 1),
    )

    minCoordsForId[groupId] = Point(
        min(minCoordsForId[groupId]?.x ?: Int.MAX_VALUE, coords.x - 1),
        min(minCoordsForId[groupId]?.y ?: Int.MAX_VALUE, coords.y - 1)
    )
    maxCoordsForId[groupId] = Point(
        max(maxCoordsForId[groupId]?.x ?: Int.MIN_VALUE, coords.x + 1),
        max(maxCoordsForId[groupId]?.y ?: Int.MIN_VALUE, coords.y + 1)
    )

    neighCoords.forEach { neighbor ->
        if (isWithinBounds(
                neighbor,
                fileXSize - 1,
                fileYSize - 1
            ) && neighbor !in visitedCoords && fileContent[neighbor.x][neighbor.y] == plant
        )
            plantGroupDFS(neighbor, plant, groupId, groupCoords)

        if (neighbor !in (plantPlots[groupId] ?: setOf())) {
            if (isWithinOuterPerimeter(neighbor) || (isWithinBounds(
                    neighbor,
                    fileXSize - 1,
                    fileYSize - 1
                ) && fileContent[neighbor.x][neighbor.y] != plant)
            ) {
                plantFences[groupId] = (plantFences[groupId] ?: 0) + 1
                plantFenceLocations.computeIfAbsent(groupId) { mutableSetOf() }.add(neighbor)
            }
        }
    }
}

fun main() {
    File("inputs/input.txt").forEachLine { fileContent.add(it) }

    fileXSize = fileContent.size
    fileYSize = fileContent[0].length
    for (i in 0..<fileXSize) {
        groupIdRepresent.add(mutableListOf())
        repeat(fileYSize) {
            groupIdRepresent[i].add(0)
        }
    }

    // Part 1
    var groupId = 0
    fileContent.forEachIndexed rowLoop@{ rowIdx, row ->
        row.forEachIndexed colLoop@{ colIdx, plant ->
            val currentCoords = Point(rowIdx, colIdx)
            if (currentCoords in visitedCoords) return@colLoop

            val groupCoords = mutableSetOf<Point>()
            plantGroupDFS(currentCoords, plant, groupId, groupCoords)

            plantPlots.computeIfAbsent(groupId) { mutableSetOf() }.addAll(groupCoords)
            groupId++
        }
    }

    var totalPrice = plantPlots.keys.fold(0) { price, plant ->
        val area = plantPlots[plant]?.size ?: 0
        val perimeter = plantFences[plant] ?: 0
        price + (area * perimeter)
    }

    println("Total price: $totalPrice")

    // Part 2
    plantFences.clear()

    for ((groupId, _) in minCoordsForId) {

        val rowRange = (minCoordsForId[groupId]!!.x)..(maxCoordsForId[groupId]!!.x)
        val colRange = (minCoordsForId[groupId]!!.y)..(maxCoordsForId[groupId]!!.y)

        for (rowIdx in rowRange) {
            var fenceOnTheRight = false
            var fenceOnTheLeft = false
            for (colIdx in colRange) {

                if (Point(rowIdx, colIdx) !in (plantFenceLocations[groupId] ?: listOf())) {
                    fenceOnTheLeft = false
                    fenceOnTheRight = false
                    continue
                }

                val leftNeighbor = Point(rowIdx - 1, colIdx)
                val rightNeighbor = Point(rowIdx + 1, colIdx)

                if (isWithinBounds(leftNeighbor, fileXSize - 1, fileYSize - 1)) {
                    if (!fenceOnTheLeft && groupIdRepresent[leftNeighbor.x][leftNeighbor.y] == groupId) {
                        fenceOnTheLeft = true
                        plantFences[groupId] = (plantFences[groupId] ?: 0) + 1
                    }

                    if (fenceOnTheLeft && groupIdRepresent[leftNeighbor.x][leftNeighbor.y] != groupId)
                        fenceOnTheLeft = false
                }

                if (isWithinBounds(rightNeighbor, fileXSize - 1, fileYSize - 1)) {
                    if (!fenceOnTheRight && groupIdRepresent[rightNeighbor.x][rightNeighbor.y] == groupId) {
                        fenceOnTheRight = true
                        plantFences[groupId] = (plantFences[groupId] ?: 0) + 1
                    }

                    if (fenceOnTheRight && groupIdRepresent[rightNeighbor.x][rightNeighbor.y] != groupId)
                        fenceOnTheRight = false
                }
            }
        }

        for (colIdx in colRange) {
            var fenceOnTheRight = false
            var fenceOnTheLeft = false
            for (rowIdx in rowRange) {
                if (Point(rowIdx, colIdx) !in (plantFenceLocations[groupId] ?: listOf())) {
                    fenceOnTheLeft = false
                    fenceOnTheRight = false
                    continue
                }

                val leftNeighbor = Point(rowIdx, colIdx + 1)
                val rightNeighbor = Point(rowIdx, colIdx - 1)

                if (isWithinBounds(leftNeighbor, fileXSize - 1, fileYSize - 1)) {
                    if (!fenceOnTheLeft && groupIdRepresent[leftNeighbor.x][leftNeighbor.y] == groupId) {
                        fenceOnTheLeft = true
                        plantFences[groupId] = (plantFences[groupId] ?: 0) + 1
                    }

                    if (fenceOnTheLeft && groupIdRepresent[leftNeighbor.x][leftNeighbor.y] != groupId)
                        fenceOnTheLeft = false
                }

                if (isWithinBounds(rightNeighbor, fileXSize - 1, fileYSize - 1)) {
                    if (!fenceOnTheRight && groupIdRepresent[rightNeighbor.x][rightNeighbor.y] == groupId) {
                        fenceOnTheRight = true
                        plantFences[groupId] = (plantFences[groupId] ?: 0) + 1
                    }

                    if (fenceOnTheRight && groupIdRepresent[rightNeighbor.x][rightNeighbor.y] != groupId)
                        fenceOnTheRight = false
                }
            }
        }

    }

    totalPrice = plantPlots.keys.fold(0) { price, plant ->
        val area = plantPlots[plant]?.size ?: 0
        val perimeter = plantFences[plant] ?: 0
        price + (area * perimeter)
    }

    println("Total price combined: $totalPrice")
}