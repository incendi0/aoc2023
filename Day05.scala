package me.illumination

import java.util
import scala.util.boundary
import scala.util.boundary.break
import scala.util.matching.Regex

object Day05 {

  val pattern: Regex = "\\d+".r

  def main(): Unit =
    val ss = FileReaderUtils.readLines("day05.txt")
    solve(ss)
    solve2(ss)

  def solve(ss: List[String]): Unit =
    var functionSet = List[List[Mapper]]()
    val seeds = pattern.findAllIn(ss.head).map(_.toLong).toList
    var functions = List[Mapper]()
    ss.drop(2).foreach(s => {
      if s.isBlank then
        functionSet = functionSet.appended(functions)
        functions = List[Mapper]()
      else if s(0).isDigit then
        val xs = pattern.findAllIn(s).map(_.toLong).toList
        functions = Mapper(xs(0), xs(1), xs(2)) :: functions
//      else
//        println(s)
    })
    functionSet = functionSet.appended(functions)
    println(seeds.map(d => {
      val xs = functionSet.scanLeft(d)((v, fs) => fs.find(f => f.isInRange(v)).map(mapper => mapper.map(v)).getOrElse(v))
      xs.last
    }).min)

  def solve2(ss: List[String]): Unit =
    var functionSet = List[List[ReverseMapper]]()
    val seeds = pattern.findAllIn(ss.head).map(_.toLong).toList
    def doWeHaveThatSeed(seed: Long): Boolean =
      seeds.grouped(2).exists(xs => xs(0) <= seed && xs(0) + xs(1) > seed)
    var functions = List[ReverseMapper]()
    ss.drop(2).foreach(s => {
      if s.isBlank then
        functionSet = functions :: functionSet
        functions = List[ReverseMapper]()
      else if s(0).isDigit then
        val xs = pattern.findAllIn(s).map(_.toLong).toList
        functions = ReverseMapper(xs(0), xs(1), xs(2)) :: functions
      //      else
      //        println(s)
    })
    functionSet = functions :: functionSet
    val str =
    boundary:
      for d <- (1L to 100_000_000L) do
        val r = functionSet.foldLeft(d)((v, fs) => fs.find(f => f.isInRange(v)).map(mapper => mapper.map(v)).getOrElse(v))
        if doWeHaveThatSeed(r) then break(d.toString)
    println(str)
//    following will throw heap out of memory
//    println((1L to 100_000_000L).map(d => {
//      (functionSet.foldLeft(d)((v, fs) => fs.find(f => f.isInRange(v)).map(mapper => mapper.map(v)).getOrElse(v)), d)
//    }).filter(t => doWeHaveThatSeed(t._1)).head)


  case class Mapper(dest: Long, source: Long, range: Long):
    def isInRange(v: Long): Boolean =
      v >= source && v < source + range
    def map(v: Long): Long = v + dest - source

  case class ReverseMapper(source: Long, dest: Long, range: Long):
    def isInRange(v: Long): Boolean =
      v >= source && v < source + range
    def map(v: Long): Long = v + dest - source
}
