package me.illumination

import java.util
import scala.util.boundary
import scala.util.boundary.break
import scala.util.matching.Regex

object Day04 {

  val pattern: Regex = "\\d+".r

  def main(): Unit =
    val ss = FileReaderUtils.readLines("day04.txt")
    solve(ss)
    solve2(ss)

  private def calculate(s: String): Int =
    val xs1 = s.split(":")
    val xs2 = xs1(1).split("\\|")
    val winningSet = pattern.findAllIn(xs2(0)).map(_.toInt).toSet
    val ownList = pattern.findAllIn(xs2(1)).map(_.toInt).toList
    val count = ownList.count(d => winningSet.contains(d))
    if count > 0 then math.pow(2, count - 1).toInt else 0

  def solve(ss: List[String]): Unit =
    println(ss.map(calculate).sum)
  def solve2(ss: List[String]): Unit =
    var count = util.ArrayList[List[Int]]()
    for (s, idx) <- ss.zipWithIndex do
      val xs1 = s.split(":")
      val xs2 = xs1(1).split("\\|")
      val winningSet = pattern.findAllIn(xs2(0)).map(_.toInt).toSet
      val ownList = pattern.findAllIn(xs2(1)).map(_.toInt).toList
      val c = ownList.count(d => winningSet.contains(d))
      count.add((idx + 2 to idx + c + 1).toList)

    var acc = List[scala.collection.mutable.HashMap[Int, Int]]()
    for idx <- count.size() to 1 by -1 do
      var map = scala.collection.mutable.HashMap[Int, Int]()
      map.put(idx, 1)
      val list = count.get(idx - 1)
      list.foreach(ix => {
        val innermap = acc(count.size() - ix)
        innermap.foreach((k, v) => {
          val oldValue = map.getOrElse(k, 0);
          map.update(k, v + oldValue)
        })
      })
      acc = acc.appended(map)
    println(acc.map(hm => hm.values.sum).sum)
}
