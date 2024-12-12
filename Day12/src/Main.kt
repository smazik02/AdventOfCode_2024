import java.io.File

var fileXSize = 0
var fileYSize = 0
val fileContent = mutableListOf<String>()

val plantPlots = mutableMapOf<Int, MutableSet<Pair<Int, Int>>>()
val plantFences = mutableMapOf<Int, Int>()

val visitedCoords = mutableSetOf<Pair<Int, Int>>()

fun isWithinOuterPerimeter(checkedCoords: Pair<Int, Int>) =
    checkedCoords.first == -1 || checkedCoords.first == fileXSize || checkedCoords.second == -1 || checkedCoords.second == fileYSize

fun isWithinBounds(checkedCoords: Pair<Int, Int>, maxX: Int, maxY: Int): Boolean {
    return checkedCoords.first in 0..maxX && checkedCoords.second in 0..maxY
}

fun plantGroupDFS(coords: Pair<Int, Int>, plant: Char, groupId: Int, groupCoords: MutableSet<Pair<Int, Int>>) {
    visitedCoords.add(coords)
    groupCoords.add(coords)

    val neighCoords = mutableListOf(
        Pair(coords.first - 1, coords.second),
        Pair(coords.first, coords.second + 1),
        Pair(coords.first + 1, coords.second),
        Pair(coords.first, coords.second - 1),
    )
    neighCoords.forEach { neighbor ->
        if (isWithinBounds(
                neighbor,
                fileXSize - 1,
                fileYSize - 1
            ) && neighbor !in visitedCoords && fileContent[neighbor.first][neighbor.second] == plant
        )
            plantGroupDFS(neighbor, plant, groupId, groupCoords)

        if (neighbor !in (plantPlots[groupId] ?: setOf())) {
            if (isWithinOuterPerimeter(neighbor) || (isWithinBounds(
                    neighbor,
                    fileXSize - 1,
                    fileYSize - 1
                ) && fileContent[neighbor.first][neighbor.second] != plant)
            )
                plantFences[groupId] = (plantFences[groupId] ?: 0) + 1
        }
    }
}

fun main() {
    File("inputs/input.txt").forEachLine { fileContent.add(it) }

    fileXSize = fileContent.size
    fileYSize = fileContent[0].length

    // Part 1
    var groupID = 0
    val plantGroupIds = mutableMapOf<Char, MutableSet<Int>>()
    fileContent.forEachIndexed rowLoop@{ rowIdx, row ->
        row.forEachIndexed colLoop@{ colIdx, plant ->
            val currentCoords = Pair(rowIdx, colIdx)
            if (currentCoords in visitedCoords) return@colLoop

            plantGroupIds.computeIfAbsent(plant) { mutableSetOf() }.add(groupID)
            val groupCoords = mutableSetOf<Pair<Int, Int>>()
            plantGroupDFS(currentCoords, plant, groupID, groupCoords)

            plantPlots.computeIfAbsent(groupID) { mutableSetOf() }.addAll(groupCoords)
            groupID++
        }
    }

    val totalPrice = plantPlots.keys.fold(0) { price, plant ->
        val area = plantPlots[plant]?.size ?: 0
        val perimeter = plantFences[plant] ?: 0
        price + (area * perimeter)
    }

    println("Total price: $totalPrice")
}