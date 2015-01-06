package ch.epfl.data

import java.io._

object CakeGenerator {
  sealed trait Scenario
  case object Case1 extends Scenario
  case object Case2 extends Scenario
  case object Case3 extends Scenario
  case object Case4 extends Scenario
  val scenario: Scenario = Case4
  val folderObject = new File("generated")
  val N = 500
  val NA = 100 * 2
  val N_FIELDS = 80
  def main(args: Array[String]) {
    implicit val out = new StringBuilder
    out ++= """trait Base {
  def a: Int
}
"""
    scenario match {
      case Case1 =>
        childrenGenerate1
        appGenerate
      case Case2 =>
        childrenGenerate2
        appGenerate
      case Case3 =>
        childrenGenerate3
        appGenerate3
      case Case4 =>
        childrenGenerate4
        appGenerate3
    }
    val outputFile = new PrintWriter(new File(folderObject, "CakeFile.scala"))
    outputFile.println(out.toString)
    outputFile.close()
  }

  def appGenerate(implicit out: StringBuilder) {
    out ++= s"trait Main extends Base"
    for (i <- 0 to N) {
      out ++= s" with C$i"
    }
    out ++= """{
  def a: Int = -1
"""
    appMethodGenerate
    out ++= "}\n"
  }

  def appGenerate3(implicit out: StringBuilder) {
    out ++= s"object Main extends Base {\n"
    for (i <- 0 to N) {
      out ++= s"import C$i._\n"
    }
    out ++= """
  def a: Int = -1
"""
    appMethodGenerate
    out ++= "}\n"
  }

  def appMethodGenerate(implicit out: StringBuilder) {
    out ++= "def app = "
    for (i <- 0 to NA) {
      if (i != 0)
        out ++= " + "
      out ++= s"x1_$i"
    }
  }

  def normalMethod(index: Int, variable: String) = method(index, variable, variable)
  def cyclicMethod(index: Int, variable: String) = method(index, variable, s"$variable${(index + 1) % N}")
  def method(index: Int, variable: String, rhs: String) = s"def $variable$index = $rhs"

  def childrenGenerate1(implicit out: StringBuilder) {
    for (i <- 0 to N) {
      out ++= s"trait C$i { this: Main =>"
      childrenBody(i)
      out ++= "}\n"
    }
  }

  def childrenGenerate2(implicit out: StringBuilder) {
    for (i <- 0 to N) {
      out ++= s"trait C$i extends Base {"
      childrenBody(i)
      out ++= "}\n"
    }
  }

  def childrenGenerate3(implicit out: StringBuilder) {
    childrenGenerate2
    for (i <- 0 to N) {
      out ++= s"object C$i extends C$i {"
      out ++= "def a: Int = -1\n"
      out ++= "}\n"
    }
  }

  def childrenGenerate4(implicit out: StringBuilder) {
    for (i <- 0 to N) {
      out ++= s"object C$i {"
      out ++= "def a: Int = -1\n"
      childrenBody(i)
      out ++= "}\n"
    }
  }

  def childrenBody(i: Int)(implicit out: StringBuilder) {
    // val variables = List[Char]()
    // val variables = List('a')
    val variables = (1 to N_FIELDS).map("x" + _ + "_")
    out ++= s"""
${(for (c <- variables) yield { method(i, c.toString, "a") }).mkString("\n")}
  class CA$i
"""
  }
}