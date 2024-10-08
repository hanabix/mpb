package garmin.read

import scala.language.implicitConversions
import scala.scalajs.js

import core.*

given Read[Gauge.BeatPerMinute, js.Dynamic, Double] = Safety[Lap, Double](_.averageHR.round)
given Read[Gauge.StepPerMinute, js.Dynamic, Double] = Safety[Lap, Double](_.averageRunCadence.round)
given Read[Distance, js.Dynamic, Double]            = Safety[Lap, Double](_.distance.round)
given Read[Duration, js.Dynamic, Double]            = Safety[Lap, Double](_.duration.round)

given Read[Timestamp, js.Dynamic, String] = Safety[Lap, String]: d =>
  val dt  = new js.Date(d.startTimeGMT)
  val gmt = new js.Date(dt.getTime() - (dt.getTimezoneOffset() * 60000))
  gmt.asInstanceOf[js.Dynamic].toLocaleDateString("fr-CA").asInstanceOf[String]

given [A](using
  Read[Distance, A, Double],
  Read[Duration, A, Double],
  Read[Gauge.BeatPerMinute, A, Double]
): Read[Gauge.MeterPerBeat, A, Double] = Read: a =>
  val speed = Read[Distance, A, Double](a) / Read[Duration, A, Double](a)
  val hr    = Read[Gauge.BeatPerMinute, A, Double](a)
  speed * 60 / hr
end given

given [A](using
  Read[Distance, A, Double],
  Read[Duration, A, Double]
): Read[Gauge.Pace, A, js.Date] = Read: a =>
  inline val minute = 60
  inline val hour   = (60 * minute)
  inline def spm    = Read[Duration, A, Double](a) / Read[Distance, A, Double](a)
  inline def spkm   = Math.round(spm * 1000).intValue
  new js.Date(0, 0, 0, spkm / hour, spkm / minute, spkm % minute)
end given

extension (d: Double) private inline def round = js.Math.round(d)

private type Lap = Safety:
  val averageHR: Double
  val averageRunCadence: Double
  val distance: Double
  val duration: Double
  val startTimeGMT: String
