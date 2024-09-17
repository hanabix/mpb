package plotly

import scala.scalajs.js

import typings.plotlyJs.anon.PartialLayoutAxis
import typings.plotlyJs.plotlyJsStrings.*

import core.*

opaque type Axis[T] = List[PartialLayoutAxis]
object Axis:

  def apply[T](using a: Axis[T]): List[PartialLayoutAxis] = a

  given [A]: Axis[EmptyTuple]                                       = Nil
  given [H, T <: Tuple](using h: Axis[H], t: Axis[T]): Axis[H *: T] = h ::: t

  given Axis[Gauge.MeterPerBeat] = PartialLayoutAxis()
    .setOverlaying(y2)
    .setTickformat(".2f")
    .setTickmodeSync
    :: Nil

  given Axis[Gauge.BeatPerMinute] = PartialLayoutAxis()
    .setSide(right)
    :: Nil

  given Axis[Gauge.StepPerMinute] = PartialLayoutAxis()
    .setSide(right)
    :: Nil

  given Axis[Gauge.Pace] = PartialLayoutAxis()
    .setSide(right)
    .setTickformat("%M:%S")
    .setAutorange(reversed)
    :: Nil

  given Axis[Distance] = PartialLayoutAxis()
    .setTickformat("~s")
    .setTicksuffix("m")
    .setTickmode(array)
    :: Nil

  given (using ColorPalette): Axis[Box] = PartialLayoutAxis()
    .setTickformat(".2f")
    .setColorIndex(0)
    :: Nil

  extension (a: PartialLayoutAxis)
    private inline def setTickmodeSync: PartialLayoutAxis =
      a.asInstanceOf[js.Dynamic].tickmode = "sync"
      a

end Axis
