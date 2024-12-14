import java.io.File
import kotlin.math.min

data class Point(val x: Long, val y: Long)

data class Machine(val buttonAPress: Point, val buttonBPress: Point, val prize: Point)

private operator fun Point.plus(point: Point): Point {
    return Point(this.x + point.x, this.y + point.y)
}

private operator fun Point.minus(point: Point): Point {
    return Point(this.x - point.x, this.y - point.y)
}

private operator fun Point.rem(point: Point): Point {
    return Point(this.x % point.x, this.y % point.y)
}

fun GCD(a: Long, b: Long): Long {
    var num1 = a
    var num2 = b
    while (num2 != 0L) {
        val temp = num2
        num2 = num1 % num2
        num1 = temp
    }
    return num1
}

fun main() {
    val machineList = mutableListOf<Machine>()

    val buttonRegex = Regex("Button [AB]: X\\+(\\d+), Y\\+(\\d+)")
    val prizeRegex = Regex("Prize: X=(\\d+), Y=(\\d+)")
    File("inputs/input.txt").readText().split("\n\n").forEach { machineDescription ->
        val buttonMatches = buttonRegex.findAll(machineDescription)
        val buttonAPress =
            Point(buttonMatches.first().groupValues[1].toLong(), buttonMatches.first().groupValues[2].toLong())
        val buttonBPress =
            Point(
                buttonMatches.toList()[1].groupValues[1].toLong(),
                buttonMatches.toList()[1].groupValues[2].toLong()
            )

        val prizeMatch = prizeRegex.find(machineDescription)
        val prize =
            Point(prizeMatch?.groupValues?.get(1)?.toLong() ?: -1L, prizeMatch?.groupValues?.get(2)?.toLong() ?: -1L)

        machineList.add(Machine(buttonAPress, buttonBPress, prize))
    }

    var tokenCount = 0L
    machineLoop@ for ((idx, machine) in machineList.withIndex()) {
        println(idx)
//        var tokenMin = Long.MAX_VALUE

        val xCondition = machine.prize.x % GCD(machine.buttonAPress.x, machine.buttonBPress.x) == 0L
        val yCondition = machine.prize.y % GCD(machine.buttonAPress.y, machine.buttonBPress.y) == 0L
        if (!xCondition || !yCondition)
            continue

        val xCount = machine.prize.x / machine.buttonBPress.x
        val yCount = machine.prize.y / machine.buttonBPress.y

        var aCount: Long
        var bCount = min(xCount, yCount)

        while (bCount-- >= 0L) {
            val currentPosition = Point(bCount * machine.buttonBPress.x, bCount * machine.buttonBPress.y)

            val positionDifference = machine.prize - currentPosition

            if ((positionDifference % machine.buttonAPress) == Point(0L, 0L)) {
                if (positionDifference.x / machine.buttonAPress.x != positionDifference.y / machine.buttonAPress.y)
                    continue
                aCount = positionDifference.x / machine.buttonAPress.x
                tokenCount += (aCount * 3L + bCount)
                continue@machineLoop
            }
        }

//        tokenCount += if (tokenMin == Int.MAX_VALUE) 0 else tokenMin
    }

    println("$tokenCount tokens")
}
