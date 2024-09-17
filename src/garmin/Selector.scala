package garmin

import org.scalajs.dom.*

final class Selector[F[_]: FlatMapwise](expr: String):

  def apply(fa: F[NodeSelector]): Option[Element]   = fa.option(s => Option(s.querySelector(expr)))
  def unapply(fa: F[NodeSelector]): Option[Element] = this(fa)

  object all:
    def apply(fa: F[NodeSelector]): Seq[Element]      = fa.seq(s => s.querySelectorAll(expr))
    def unapplySeq(fa: F[NodeSelector]): Seq[Element] = this(fa)
    
object Selector:
  def apply[F[_]: FlatMapwise]: String => Selector[F] = new Selector[F](_) 
