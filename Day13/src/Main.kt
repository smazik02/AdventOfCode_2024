import java.io.File

data class Point(val x: Long, val y: Long)

data class Machine(val buttonAPress: Point, val buttonBPress: Point, val prize: Point)

fun main() {
    val machineList = mutableListOf<Machine>()

    val buttonRegex = Regex("Button [AB]: X\\+(\\d+), Y\\+(\\d+)")
    val prizeRegex = Regex("Prize: X=(\\d+), Y=(\\d+)")

    val forTask2 = 10000000000000L

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
        val prize = Point(
            (prizeMatch?.groupValues?.get(1)?.toLong() ?: -1L) + forTask2,
            (prizeMatch?.groupValues?.get(2)?.toLong() ?: -1L) + forTask2
        )

        machineList.add(Machine(buttonAPress, buttonBPress, prize))
    }

    var tokenCount = 0L
    for (machine in machineList) {
        val w = machine.buttonAPress.x * machine.buttonBPress.y - machine.buttonAPress.y * machine.buttonBPress.x
        val wx = machine.prize.x * machine.buttonBPress.y - machine.prize.y * machine.buttonBPress.x
        val wy = machine.buttonAPress.x * machine.prize.y - machine.buttonAPress.y * machine.prize.x

        if (w == 0L && wx == 0L && wy == 0L) continue
        if ((wx.toDouble() / w.toDouble()) % 1L != 0.0 || (wy.toDouble() / w.toDouble()) % 1L != 0.0) continue

        val finalPos = Point(wx / w, wy / w)

        if (finalPos.x < 0 || finalPos.y < 0) continue

        tokenCount += finalPos.x * 3 + finalPos.y
    }

    println("$tokenCount tokens")
}
