import scala.scalajs.js

import org.scalajs.dom.*

import core.*
import garmin.*
import garmin.read.given
import plotly.{*, given}

object Main:

  def main(args: Array[String]): Unit =
    val init = new MutationObserverInit:
      childList = true
      subtree = true

    MutationObserver: (smr, _) =>
      val url   = URL(window.location.href)
      val added = smr.flatMap(_.addedNodes).collect({ case e: HTMLElement => e }).toSeq
      Page[(Activities, Profile)](url -> added)
    .observe(document.querySelector("div.main-body"), init)

  end main

  type A  = js.Dynamic
  type X  = Distance
  type Y  = Gauge.MeterPerBeat
  type Y2 = (Gauge.BeatPerMinute, Gauge.StepPerMinute, Gauge.Pace)
  given CorrelatePlot[Interval[A]] = CorrelatePlot[X, Y, Y2, Interval[A]]

end Main
