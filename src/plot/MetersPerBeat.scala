package plot

import scala.scalajs.js

import garmin.Performance

trait MetersPerBeat[A] extends (A => Double):
  extension (a: A) inline def mpb = this(a)

object MetersPerBeat:
  given mpb: MetersPerBeat[Performance] = p => p.averageSpeed.get * 60 / p.averageHR.get
