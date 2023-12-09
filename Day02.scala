package me.illumination

object Day02 {

  def solve(ss: List[String]): Unit =
    println(ss.map(analyzeComponents).filter((id, t) => t._1 <= 12 && t._2 <= 13 && t._3 <= 14).map(t => t._1).sum)

  def solve2(ss: List[String]): Unit =
    println(ss.map(analyzeComponents).map((id, t) => t._1 * t._2 * t._3).sum)

  private def analyzeComponents(s: String): (Int, (Int, Int, Int)) =
    val xs = s.split(":")
    val id = xs(0).substring("Game ".length).toInt
    var (r, g, b) = (0, 0, 0)
    xs(1).split(";").map(parseRGB).foreach(t => {
      if t._1 > r then r = t._1
      if t._2 > g then g = t._2
      if t._3 > b then b = t._3
    })
    (id, (r, g, b))

  private def parseRGB(s: String): (Int, Int, Int) =
    val parts = s.split(",")
    var (r, g, b) = (0, 0, 0)
    for (p <- parts) {
      val xs = p.trim.split(" ")
      if xs(1) == "red" then r = xs(0).toInt
      else if xs(1) == "green" then g = xs(0).toInt
      else b = xs(0).toInt
    }
    (r, g, b)


  def main(): Unit =
    val ss = FileReaderUtils.readLines("day02.txt")
    solve(ss)
    solve2(ss)
}
