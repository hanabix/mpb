package plot

import scala.scalajs.js

import garmin.Performance

trait HoverText[A] extends (A => String):
  extension (a: A) inline def text = this(a)

object HoverText:
  given text: HoverText[Performance] = p =>
    Seq(
      unit(p.averageHR, "bpm"),
      unit(Math.round(p.averageRunCadence.get), "spm"),
      pace(p.averageSpeed),
      pace(p.avgGradeAdjustedSpeed),
      unit(Math.round(p.distance.get), "m")
    ).mkString("<br>")

  private inline def unit(p: js.Any, s: js.Any) = s"$p<sub>$s</sub>"
  private inline def pace(v: js.UndefOr[Double]) =
    (for
      d <- v
      sec = Math.round(1000 / d)
      m   = Math.round(sec / 60)
      s   = sec % 60
    yield unit("%02d:%02d".format(m, s), "/km")).getOrElse("--")
end HoverText
