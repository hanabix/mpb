import mill._, scalalib._, scalajslib._, scalajslib.api._

import $ivy.`com.github.lolgab::mill-scalablytyped::0.1.14`
import com.github.lolgab.mill.scalablytyped._

object `package` extends RootModule with ScalaJSModule with ScalablyTyped {
  def scalaVersion   = "3.5.0"
  def scalaJSVersion = "1.16.0"
  def moduleKind     = ModuleKind.ESModule

  override def ivyDeps: T[Agg[Dep]] = T {
    super.ivyDeps() ++ Agg(ivy"com.lihaoyi::sourcecode::0.4.2")
  }

  override def scalacOptions: Target[Seq[String]] = T {
    super.scalacOptions() ++ Seq("-Wunused:all", "-feature", "-Yexplicit-nulls")
  }

  object test extends ScalaJSTests {
    def ivyDeps       = Agg(ivy"com.lihaoyi::utest::0.7.11")
    def testFramework = "utest.runner.Framework"
  }
}
