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

        else -> throw UnsupportedOperationException()
    }
}

fun checkIfCanMove(instruction: Direction, currentPoint: Point): Point {
    val nextPoint = when (instruction) {
        Direction.UP -> Point(currentPoint.x - 1, currentPoint.y)
        Direction.DOWN -> Point(currentPoint.x + 1, currentPoint.y)
        Direction.LEFT -> Point(currentPoint.x, currentPoint.y - 1)
        Direction.RIGHT -> Point(currentPoint.x, currentPoint.y + 1)
    }

    when (mapBoard[nextPoint.x][nextPoint.y]) {
        '#' -> return currentPoint
        '.' -> return nextPoint
        '[', ']' -> {
            when (instruction) {
                Direction.LEFT, Direction.RIGHT -> {
                    val newPoint = checkIfCanMove(instruction, nextPoint)
                    return if (newPoint != nextPoint) {
                        nextPoint
                    } else {
                        currentPoint
                    }
                }

                Direction.UP, Direction.DOWN -> {
                    val newPoint = checkIfCanMove(instruction, nextPoint)
                    val otherNextPoint =
                        if (mapBoard[nextPoint.x][nextPoint.y] == '[')
                            Point(nextPoint.x, nextPoint.y + 1)
                        else
                            Point(nextPoint.x, nextPoint.y - 1)
                    val otherNewPoint = checkIfCanMove(instruction, otherNextPoint)

                    return if (newPoint != nextPoint && otherNewPoint != otherNextPoint) {
                        nextPoint
                    } else {
                        currentPoint
                    }
                }
            }
        }

        else -> throw UnsupportedOperationException()
    }
}

fun move2(instruction: Direction, currentPoint: Point): Point {
    val nextPoint = when (instruction) {
        Direction.UP -> Point(currentPoint.x - 1, currentPoint.y)
        Direction.DOWN -> Point(currentPoint.x + 1, currentPoint.y)
        Direction.LEFT -> Point(currentPoint.x, currentPoint.y - 1)
        Direction.RIGHT -> Point(currentPoint.x, currentPoint.y + 1)
    }

    when (mapBoard[nextPoint.x][nextPoint.y]) {
        '#' -> return currentPoint
        '.' -> return nextPoint
        '[', ']' -> {
            when (instruction) {
                Direction.LEFT, Direction.RIGHT -> {
                    val newPoint = move2(instruction, nextPoint)
                    mapBoard[newPoint.x][newPoint.y] = mapBoard[nextPoint.x][nextPoint.y]
                    mapBoard[nextPoint.x][nextPoint.y] = '.'
                    return nextPoint
                }

                Direction.UP, Direction.DOWN -> {
                    val newPoint = move2(instruction, nextPoint)
                    val otherNextPoint =
                        if (mapBoard[nextPoint.x][nextPoint.y] == '[')
                            Point(nextPoint.x, nextPoint.y + 1)
                        else
                            Point(nextPoint.x, nextPoint.y - 1)
                    val otherNewPoint = move2(instruction, otherNextPoint)

                    mapBoard[newPoint.x][newPoint.y] = mapBoard[nextPoint.x][nextPoint.y]
                    mapBoard[otherNewPoint.x][otherNewPoint.y] = mapBoard[otherNextPoint.x][otherNextPoint.y]
                    mapBoard[nextPoint.x][nextPoint.y] = '.'
                    mapBoard[otherNextPoint.x][otherNextPoint.y] = '.'

                    return nextPoint
                }
            }
        }

        else -> throw UnsupportedOperationException()
    }
}

fun main() {
    val instructionSet = mutableListOf<Direction>()
    var robotPos = Point(0, 0)

    // PART 1

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

    var coordSum = 0
    mapBoard.forEachIndexed { rowIdx, row ->
        row.forEachIndexed rowLoop@{ colIdx, place ->
            if (place != 'O') return@rowLoop

            coordSum += (100 * rowIdx + colIdx)
        }
    }

    println("Total sum: $coordSum")

    // PART 2
    mapBoard.clear()

    fileContent.forEachIndexed { rowIdx, line ->
        if (line.isEmpty())
            return@forEachIndexed

        if (line.first() == '#') {
            val rowToAdd = mutableListOf<Char>()
            line.forEachIndexed { colIdx, char ->
                when (char) {
                    '#' -> rowToAdd.addAll("##".toList())
                    'O' -> rowToAdd.addAll("[]".toList())
                    '.' -> rowToAdd.addAll("..".toList())
                    '@' -> {
                        robotPos = Point(rowIdx, colIdx * 2)
                        rowToAdd.addAll("..".toList())
                    }
                }
            }
            mapBoard.add(rowToAdd)
        }
    }

    for (instruction in instructionSet) {
        val newPoint = checkIfCanMove(instruction, robotPos)
        if (newPoint != robotPos) {
            move2(instruction, robotPos)
            robotPos = newPoint
        }
    }

    coordSum = 0
    mapBoard.forEachIndexed { rowIdx, row ->
        row.forEachIndexed rowLoop@{ colIdx, place ->
            if (place != '[') return@rowLoop

            coordSum += (100 * rowIdx + colIdx)
        }
    }

    println("Total sum part 2: $coordSum")
}