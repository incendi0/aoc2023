package me.illumination

import scala.collection.mutable.ArrayBuffer

object Day09 {

  def main(): Unit =
    val ss: Array[String] = FileReaderUtils.readLinesToArray("day09.txt")
    solve(ss)
    solve2(ss)

  def solve(ss: Array[String]): Unit =
    println(ss.map(s => s.split("\\s+").map(_.toInt)).map(extrapolate).map(xxs => xxs.map(xs => xs.last).sum).sum)

  def solve2(ss: Array[String]): Unit =
    println(ss.map(s => s.split("\\s+").map(_.toInt)).map(extrapolate).map(xxs => xxs.map(xs => xs.head).foldRight(0)((e, acc) => e - acc)).sum)

  private def extrapolate(xs: Array[Int]): Array[Array[Int]] =
    def generate(xs: Array[Int]): Array[Int] =
      (1 until xs.length).map(idx => xs(idx) - xs(idx - 1)).toArray

    var xxs = ArrayBuffer[Array[Int]]()
    var r = xs
    while !r.forall(_ == 0) do
      xxs.append(r)
      r = generate(r)
    xxs.append(r)
    xxs.toArray




}
