import java.io.File

fun main() {
    val adjList = mutableMapOf<Int, MutableSet<Int>>()
    val reports = mutableListOf<List<Int>>()

    File("input.txt").forEachLine {line ->
        if (line.trim().isEmpty()) return@forEachLine

        if (line.contains('|')) {
            val (lhs, rhs) = line.trim().split('|').map(String::toInt)
            adjList[lhs]?.add(rhs) ?: run { adjList[lhs] = mutableSetOf(rhs) }
        }
        else {
            reports.add(line.trim().split(',').map(String::toInt))
        }
    }

    // PART 1
    var total = 0
    var totalCorrected = 0
    for (report in reports) {
        val badList = mutableSetOf<Int>()
        var isCorrect = true;
        for (page in report) {
            val intersection = adjList[page]?.intersect(badList) ?: emptySet()
            if (intersection.isNotEmpty()) {
                isCorrect = false
                break
            }
            badList.add(page)
        }

        if (isCorrect) {
            total += report[(report.size - 1)/2]
            continue
        }

        // PART 2
        val sortedReport = report
            .fold(mutableMapOf<Int, Int>()) { acc, page ->
                if (!acc.containsKey(page)) acc[page] = 0
                adjList[page]?.forEach { adj ->
                    if (adj in report)
                        acc[adj] = acc[adj]?.plus(1) ?: 1
                }
                return@fold acc
            }
            .toList()
            .sortedBy { (_, value) -> value }
            .map { it.first }

        totalCorrected += sortedReport[(sortedReport.size - 1)/2]
    }

    println("Total: $total")
    println("TotalCorrected: $totalCorrected")
}