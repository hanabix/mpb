import mill._, scalalib._, scalajslib._, scalajslib.api._

import $ivy.`com.github.lolgab::mill-scalablytyped::0.1.14`
import com.github.lolgab.mill.scalablytyped._

object root extends RootModule with ScalaJSModule with ScalablyTyped {
  def scalaVersion   = "3.4.2"
  def scalaJSVersion = "1.16.0"
  def moduleKind     = ModuleKind.ESModule

  object test extends ScalaJSTests {
    def ivyDeps       = Agg(ivy"com.lihaoyi::utest::0.7.11")
    def testFramework = "utest.runner.Framework"
  }
}
