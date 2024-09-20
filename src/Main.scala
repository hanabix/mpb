import scala.language.implicitConversions
import scala.scalajs.js

import org.scalajs.dom.*

import core.*
import garmin.*
import garmin.read.given
import plotly.{*, given}

object Main:

  given Aka[Gauge.BeatPerMinute] = "bpm"
  given Aka[Gauge.MeterPerBeat]  = "mpb"
  given Aka[Gauge.StepPerMinute] = "spm"
  given Aka[Gauge.Pace]          = "/km"

  given CorrelatePlot[Interval[js.Dynamic]] =
    type X  = Distance
    type Y  = Gauge.MeterPerBeat
    type Y2 = (Gauge.BeatPerMinute, Gauge.StepPerMinute, Gauge.Pace)
    CorrelatePlot[X, Y, Y2, Interval[js.Dynamic]]

  def main(args: Array[String]): Unit =
    val init = new MutationObserverInit:
      childList = true
      subtree = true

    MutationObserver: (smr, _) =>
      type Pages = (Activities, Profile)

      val url   = URL(window.location.href)
      val added = smr.flatMap(_.addedNodes).collect({ case e: HTMLElement => e }).toSeq
      Proceed[Pages](url -> added)
    .observe(document.querySelector("div.main-body"), init)

  end main

end Main
