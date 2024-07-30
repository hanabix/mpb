package common
import scala.scalajs.js.*

import org.scalajs.dom.*

object GetURL:
  inline def path: URL => String = _.pathname

  inline def param(key: String): URL => Option[UndefOr[String]] =
    u => u.searchParams.find(_._1 == key).map(_._2).orElse(Option(undefined))
