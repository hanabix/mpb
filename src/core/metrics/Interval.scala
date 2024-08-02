package core.metrics

import scala.scalajs.js

trait Interval extends js.Object:
  def startTimeGMT: Timestamp
  def distance: Meter
  def duration: Second
  def averageHR: BeatPerMinute
  def averageRunCadence: StepPerMinute
  def elevationGain: Meter
  def elevationLoss: Meter
end Interval

object Interval:
  given perf(using Speed[Interval]): Performance[Interval] = i => i.speed * 60 / i.averageHR
  given speed: Speed[Interval]                             = i => i.distance / i.duration
  given pace: Pace[Interval]                               = i => i.duration / i.distance * 1000
end Interval
