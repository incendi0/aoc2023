package me.illumination

import scala.annotation.tailrec

object Day08 {

  def main(): Unit =
    val ss = FileReaderUtils.readLines("day08.txt")
    val instructions = ss(0)
    val map = ss.drop(2).map(line => {
      val xs = line.split(" = ")
      val key = xs(0)
      val vs = xs(1).substring(1, xs(1).length - 1).split(", ").toList
      (key, vs)
    }).toMap
    solve(instructions, map)
    solve2(instructions, map)

  def solve(instructions: String, map: Map[String, List[String]]): Unit =
    println(helper("AAA", _ == "ZZZ", instructions, map))

  private def helper(startNode: String, endPred: String => Boolean, instructions: String, map: Map[String, List[String]]): Int =
    instructions.repeat(1000).scanLeft((startNode, 0))((acc, ch) => (if ch == 'L' then map(acc._1)(0) else map(acc._1)(1), acc._2 + 1)).find(acc => endPred(acc._1)).get._2

  def solve2(instructions: String, map: Map[String, List[String]]): Unit =
    val nodes = map.keys.filter(k => k.endsWith("A")).toList
    val steps = nodes.map(node => helper(node, _.endsWith("Z"), instructions, map))
    println(lcm(steps.map(d => d.toLong)))

  private def lcm(xs: List[Long]): Long =
    @tailrec
    def gcd(a: Long, b: Long): Long =
      if b == 0 then a
      else gcd(b, a % b)

    xs.reduce((a, b) => a * b / gcd(a, b))
}
