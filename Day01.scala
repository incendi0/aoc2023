package me.illumination

object Day01 {

  val digits = Array("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")

  def solve(ss: List[String]): Unit =
    println(ss.map(calibration).sum)

  def solve2(ss: List[String]): Unit =
//    ss.map(calibration2).foreach(println)
    println(ss.map(calibration2).sum)

  private def calibration(s: String): Int =
    val lhs = s.indexWhere(ch => ch >= '0' && ch <= '9')
    val rhs = s.lastIndexWhere(ch => ch >= '0' && ch <= '9')
    (s(lhs) - '0') * 10 + (s(rhs) - '0')

  private def calibration2(s: String): Int =
    val lhs = s.indexWhere(ch => ch >= '0' && ch <= '9')
    val rhs = s.lastIndexWhere(ch => ch >= '0' && ch <= '9')
    val lv = digits.map(d => indexOfOrPlaceHolder(s, d, true)).zipWithIndex.minBy(_._1)
    val rv = digits.map(d => indexOfOrPlaceHolder(s, d, false)).zipWithIndex.maxBy(_._1)
    val l = if lhs == -1 || lhs > lv._1 then lv._2 + 1 else s.charAt(lhs) - '0'
    val r = if rhs == -1 || rhs < rv._1 then rv._2 + 1 else s.charAt(rhs) - '0'
    l * 10 + r

  private def indexOfOrPlaceHolder(s: String, sub: String, lhs: Boolean): Int =
    if lhs then
      val idx = s.indexOf(sub)
      if idx == -1 then s.length else idx
    else
      s.lastIndexOf(sub)

  def main(): Unit = {
    val ss = FileReaderUtils.readLines("day01.txt")
    solve(ss)
    solve2(ss)
  }
}
