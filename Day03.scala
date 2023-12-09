package me.illumination

object Day03 {

  def main(): Unit =
    val ss = FileReaderUtils.readLines("day03.txt")
    solve(ss)
    solve2(ss)

  private def statAllowedSet(ss: List[String]): Set[(Int, Int)] =
    val xs = Array((-1, 1), (0, 1), (1, 1), (1, 0), (1, -1), (0, -1), (-1, -1), (-1, 0))
    val allowedSet =
      (for {
        i <- ss.indices
        j <- ss(i).indices
        if isSymbol(ss(i)(j))
        (dx, dy) <- xs
      } yield (i + dx, j + dy)).toSet

    allowedSet

  private def isSymbol(ch: Char): Boolean =
    ch != '.' && (ch < '0' || ch > '9')

  def solve(ss: List[String]): Unit =
    val allowedSet = statAllowedSet(ss)
    val ret = ss.zipWithIndex.map((s, x) => {
      var idx = 0
      var total = 0
      while idx < s.length do
        if Character.isDigit(s(idx)) then
          var num = 0
          var valid = false
          while idx < s.length && Character.isDigit(s(idx)) do
            if allowedSet.contains((x, idx)) then valid = true
            num = num * 10 + s(idx) - '0'
            idx += 1
          if valid then total += num
        else idx += 1
      total
    }).sum
    println(ret)

  private def aroundHasAsterisk(ss: List[String], loc: (Int, Int)): List[(Int, Int)] =
    List((-1, 1), (0, 1), (1, 1), (1, 0), (1, -1), (0, -1), (-1, -1), (-1, 0))
      .map((x, y) => (x + loc._1, y + loc._2))
      .filter((x, y) => x >= 0 && x < ss.length && y >= 0 && y < ss(x).length)
      .filter((x, y) => ss(x)(y) == '*')

  def solve2(ss: List[String]): Unit =
    val allowedSet = statAllowedSet(ss)
    var aroundAsterisks = scala.collection.mutable.HashMap[(Int, Int), scala.collection.mutable.HashSet[Int]]()
    val ret = ss.zipWithIndex.map((s, x) => {
      var idx = 0
      while idx < s.length do
        if Character.isDigit(s(idx)) then
          var num = 0
          var asteriskSet = scala.collection.mutable.HashSet[(Int, Int)]()
          while idx < s.length && Character.isDigit(s(idx)) do
            asteriskSet.addAll(aroundHasAsterisk(ss, (x, idx)))
            num = num * 10 + s(idx) - '0'
            idx += 1
          asteriskSet.foreach(loc => {
            val v = aroundAsterisks.getOrElse(loc, scala.collection.mutable.HashSet[Int]())
            v.add(num)
            aroundAsterisks.put(loc, v)
          })
        else idx += 1
    })
    println(aroundAsterisks.values.filter(v => v.size == 2).map(s => s.toList).map(s => s(0) * s(1)).sum)
}
