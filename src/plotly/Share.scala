package plotly

import scala.language.implicitConversions

import typings.plotlyJs.anon.PartialPlotDataAutobinx
import typings.plotlyJs.mod.Data

import core.*
import convs.given

opaque type Share[T, A] = A => Data => Data
object Share:
  def apply[T, A](a: A)(using c: Share[T, A]): Data => Data = c(a)

  given [A](using Read[Distance, A, Double]): Share[Distance, Interval[A]] = i =>
    val (_, sd) = i.deref
      .map(Read[Distance, A, Double])
      .foldLeft(0.0 -> List.empty[Double]):
        case ((s, t), d) => (d + s) -> (d + s :: t)
    _.asInstanceOf[PartialPlotDataAutobinx].setX(sd.reverse)
