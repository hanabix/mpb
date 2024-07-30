package common

import scala.scalajs.js.*

object Regex:
  extension (s: String)
    inline def re                                              = RegExp(s)
    inline def capture: String => Option[Seq[UndefOr[String]]] = ss => Option(s.re.exec(ss)).map(_.toSeq)
