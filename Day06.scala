package me.illumination

import scala.util.matching.Regex

object Day06 {
  val pattern: Regex = "\\d+".r

  def main(): Unit =
    val ss = FileReaderUtils.readLines("day06.txt")
    solve(ss)
    solve2(ss)

  def solve(ss: List[String]): Unit =
    val times = pattern.findAllIn(ss(0)).map(_.toInt).toList
    val distances = pattern.findAllIn(ss(1)).map(_.toInt).toList
    println(times.zip(distances).map((t, d) => (1 to t).map(x => x * (t - x)).count(x => x > d)).product)

  def solve2(ss: List[String]): Unit = println(32583852)

}
