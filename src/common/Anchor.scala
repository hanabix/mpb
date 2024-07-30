package common

import org.scalajs.dom.*

trait Anchor[A]:
  def init(mount: Node => Unit, id: String): A

object Anchor:
  given element: Anchor[HTMLElement] with
    def init(mount: Node => Unit, id: String): HTMLElement =
      inline def create =
        val e = document.createElement("div")
        e.id = id
        mount(e)
        e

      (document.getElementById(id) match
        case null => create
        case e    => e
      ).asInstanceOf[HTMLElement]
    end init
  end element
end Anchor
