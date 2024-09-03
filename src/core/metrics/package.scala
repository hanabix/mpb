package core.metrics

type Intervals = List[Interval]
type History   = List[Intervals]

type BeatPerMinute  = Double // bpm, average heart rate
type StepPerMinute  = Double // spm, average cadence
type Meter          = Double // distance
type Second         = Double // duration
type MeterPerBeat   = Double // mpb, performance
type MeterPerSecond = Double // speed
type Timestamp      = String

trait Performance[A] extends (A => MeterPerBeat):
  extension (a: A) inline def mpb = this(a)

trait Speed[A] extends (A => MeterPerSecond):
  extension (a: A) inline def speed = this(a)

trait Pace[A] extends (A => Second):
  extension (a: A) inline def pace = this(a)
