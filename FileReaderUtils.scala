package me.illumination

import me.illumination.FileReaderUtils.getClass

import scala.io.Source

object FileReaderUtils {

  def readLines(fn: String): List[String] =
    val inputStream = getClass.getClassLoader.getResourceAsStream(fn)
    try {
      val content = Source.fromInputStream(inputStream).getLines()
      content.toList
    } finally {
      inputStream.close()
    }

  def readLinesToArray(fn: String): Array[String] =
    val inputStream = getClass.getClassLoader.getResourceAsStream(fn)
    try {
      val content = Source.fromInputStream(inputStream).getLines()
      content.toArray
    } finally {
      inputStream.close()
    }
}
