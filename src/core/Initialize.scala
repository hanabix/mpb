package core

import org.scalajs.dom.{HTMLDivElement as Div, *}

trait Initialize[+A <: Element]:
  extension (id: String)
    def elementAt(pos: Node => Unit): A
    def reset(): Unit

object Initialize:
  given Initialize[Div] with
    extension (id: String)
      def reset(): Unit                     = init(id, _ => ())
      def elementAt(pos: Node => Unit): Div = init(id, pos)

    private inline def init(id: String, pos: Node => Unit) =
      Option(document.getElementById(id)).foreach(_.remove())
      val a = document.createElement("div").asInstanceOf[Div]
      a.id = id
      pos(a)
      a
  end given
end Initialize
