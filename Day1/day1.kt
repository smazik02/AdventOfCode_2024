import java.io.File
import kotlin.math.abs

fun main() {
    val list1 = mutableListOf<Int>()
    val list2 = mutableListOf<Int>()
    File("input.txt").forEachLine { line ->
        run {
            val splitLine = line.trim().split("   ")
            list1.add(splitLine[0].toInt())
            list2.add(splitLine[1].toInt())
        }
    }
    list1.sort()
    list2.sort()

    var distance = 0
    list1.zip(list2).forEach {pair ->
        run {
            distance += abs(pair.first - pair.second)
        }
    }

    println("Distance between both lists is $distance")
}