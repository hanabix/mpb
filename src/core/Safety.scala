package core

import scala.scalajs.js

class Safety private (d: js.Dynamic) extends Selectable:
  def selectDynamic(name: String): Any =
    js.typeOf(d) match
      case "object" =>
        val v = d.selectDynamic(name)
        if js.isUndefined(v) then throw NoSuchFieldException(name) else v
      case t =>
        throw UnsupportedOperationException(s"get $name from $t")

object Safety:
  def apply[T <: Safety](d: js.Dynamic)                 = new Safety(d).asInstanceOf[T]
  def apply[A <: Safety, B](f: A => B): js.Dynamic => B = d => f(Safety[A](d))
