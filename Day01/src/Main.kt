import java.io.File
import kotlin.math.abs

fun main() {
    val list1 = mutableListOf<Int>()
    val list2 = mutableListOf<Int>()
    File("input.txt").forEachLine { line ->
        val splitLine = line.trim().split("   ")
        list1.add(splitLine[0].toInt())
        list2.add(splitLine[1].toInt())
    }

    list1.sort()
    list2.sort()

    var distance = 0
    list1.zip(list2).forEach { distance += abs(it.first - it.second) }

    var similarity = 0
    val numCount = mutableMapOf<Int, Int>()
    for (num in list1) {
        if (num !in numCount.keys) {
            numCount[num] = list2.count { it == num }
        }
        similarity += num * numCount[num]!!
    }

    println("Distance between both lists is $distance")
    println("Similarity of both lists is $similarity")
}