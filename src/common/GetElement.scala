package common

import org.scalajs.dom.*

object GetElement:
  inline def query(selector: String): Element => Element = _.querySelector(selector)