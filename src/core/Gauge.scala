package core

enum Gauge:
  case MeterPerBeat
  case BeatPerMinute
  case StepPerMinute
  case Pace

  def label: String = this match
    case MeterPerBeat  => "mpb"
    case BeatPerMinute => "bpm"
    case StepPerMinute => "spm"
    case Pace          => "/km"
end Gauge
object Gauge:
  type MeterPerBeat  = MeterPerBeat.type
  type BeatPerMinute = BeatPerMinute.type
  type StepPerMinute = StepPerMinute.type
  type Pace          = Pace.type

trait Distance
trait Duration
trait Timestamp
trait Box
