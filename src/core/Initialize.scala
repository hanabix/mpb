package core

import org.scalajs.dom.Element
import org.scalajs.dom.HTMLDivElement as Div
import org.scalajs.dom.Node
import org.scalajs.dom.document

type Initialization = (String, Node => Unit)
trait Initialize[+A <: Element] extends (Initialization => A):
  extension (id: String)
    inline def elementAt(pos: Node => Unit) = this(id, pos)
    inline def reset(): Unit                = this(id, _ => ())

object Initialize:
  given html: Initialize[Div] = (id, pos) =>
    Option(document.getElementById(id)).foreach(_.remove())
    val a = document.createElement("div").asInstanceOf[Div]
    a.id = id
    pos(a)
    a
