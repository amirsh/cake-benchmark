package ch.epfl.data

import java.io._

object CakeGenerator {
  val folderObject = new File("generated")
  val N = 1000 * 2
  def main(args: Array[String]) {
    implicit val out = new StringBuilder
    out ++= """trait Base {
  def a: Int
}
"""
    childrenGenerate1
    // childrenGenerate2
    out ++= s"trait Main extends Base"
    for (i <- 0 to N) {
      out ++= s" with C$i"
    }
    out ++= """{
  def a: Int = -1
"""
    for (i <- 0 to N) {
      out ++= s"  override def a$i: Int = $i\n"
    }
    out ++= "}\n"
    val outputFile = new PrintWriter(new File(folderObject, "CakeFile.scala"))
    outputFile.println(out.toString)
    outputFile.close()
  }

  def childrenGenerate1(implicit out: StringBuilder) {
    for (i <- 0 to N) {
      out ++= s"""trait C$i { this: Base =>
  def a$i: Int = a
}
"""
    }
  }

  def childrenGenerate2(implicit out: StringBuilder) {
    for (i <- 0 to N) {
      out ++= s"""trait C$i extends Base {
  def a$i: Int = a
}
"""
    }
  }
}