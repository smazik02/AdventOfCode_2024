import java.io.File

val mapBoard = mutableListOf<MutableList<Char>>()

enum class Direction { UP, DOWN, RIGHT, LEFT }

data class Point(val x: Int, val y: Int)

fun move(instruction: Direction, currentPoint: Point): Point {
    val nextPoint = when (instruction) {
        Direction.UP -> Point(currentPoint.x - 1, currentPoint.y)
        Direction.DOWN -> Point(currentPoint.x + 1, currentPoint.y)
        Direction.LEFT -> Point(currentPoint.x, currentPoint.y - 1)
        Direction.RIGHT -> Point(currentPoint.x, currentPoint.y + 1)
    }

    when (mapBoard[nextPoint.x][nextPoint.y]) {
        '#' -> return currentPoint
        '.' -> return nextPoint
        'O' -> {
            val newPoint = move(instruction, nextPoint)
            if (newPoint != nextPoint) {
                mapBoard[newPoint.x][newPoint.y] = 'O'
                mapBoard[nextPoint.x][nextPoint.y] = '.'
                return nextPoint
            } else {
                return currentPoint
            }
        }

        else -> return currentPoint
    }
}

fun main() {
    val instructionSet = mutableListOf<Direction>()
    var robotPos = Point(0, 0)

    val fileContent = File("inputs/input.txt").readLines().map { it.trim() }
    fileContent.forEachIndexed { idx, line ->
        if (line.isEmpty())
            return@forEachIndexed

        if (line.first() == '#') {
            mapBoard.add(line.toMutableList())
            val robotIdx = line.indexOfFirst { it == '@' }
            if (robotIdx != -1) {
                robotPos = Point(idx, robotIdx)
                mapBoard[robotPos.x][robotPos.y] = '.'
            }
            return@forEachIndexed
        }

        instructionSet.addAll(line.map {
            when (it) {
                '^' -> Direction.UP
                'v' -> Direction.DOWN
                '>' -> Direction.RIGHT
                '<' -> Direction.LEFT
                else -> throw UnsupportedOperationException()
            }
        })
    }

    for (instruction in instructionSet) {
        val newPoint = move(instruction, robotPos)
        if (newPoint != robotPos) {
            robotPos = newPoint
        }
    }

//    mapBoard[robotPos.x][robotPos.y] = '@'
//    mapBoard.forEach { println(it) }

    var coordSum = 0
    mapBoard.forEachIndexed { rowIdx, row ->
        row.forEachIndexed rowLoop@{ colIdx, place ->
            if (place != 'O') return@rowLoop

            coordSum += (100 * rowIdx + colIdx)
        }
    }

    println("Total sum: $coordSum")
}