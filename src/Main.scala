import scala.annotation.tailrec
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.Array as Arr
import scala.scalajs.js.JSConverters.*
import scala.scalajs.js.RegExp
import scala.util.boundary
import scala.util.boundary.break

import org.scalajs.dom.*

import typings.plotlyJs.anon.*
import typings.plotlyJs.mod.*
import typings.plotlyJsDistMin.mod

import Garmin.*

object Main:

  def main(args: Array[String]): Unit =
    MutationObserver: (rs, _) =>
      on(new URL(window.location.href), rs.toSeq)
    .observe(
      document.querySelector("div.main-body"),
      new:
        childList = true
        subtree = true
    )
  end main

  private def on: (URL, Seq[MutationRecord]) => Unit =
    case (Activities(tp), AddedNodes(ListItem(_)))           => onActivitiesPage(tp)
    case (Activity(id), AddedNodes(ActivityMap(e)))          => onActivityPage(id, e)
    case (Workout(id, "running"), AddedNodes(PageHeader(e))) => onRunningWorkoutPage(id, e)
    case (u, ms)                                             => ()
  end on

  private inline def onActivitiesPage(activityType: js.UndefOr[String])(using
    Scatter[js.Array[Cast[Activity & Workout & Performace]]]
  ) =
    val root = div(document.querySelector("div.advanced-filtering").before(_))

    if activityType != "running" then root.remove()
    else
      val filter = new Garmin.ActivitiesFilter:
        excludeChildren = false
        start = 0
        limit = 30
        now = js.Date.now()
      filter.activityType = activityType.get
      for gas <- Garmin.activities(filter)
      do
        val layout = defaultLayout.setTitle("速心比变化趋势")
        mod.newPlot(root, Arr(gas.scatter), layout, defaultConfig)
    end if
  end onActivitiesPage

  private inline def onActivityPage(id: String, point: Element)(using Scatter[Arr[Cast[Garmin.Lap]]]) =
    for
      a <- Garmin.activity(id.toDouble)
      t <- a.activityTypeDTO
      k <- t.typeKey if k == "running"
    do
      for laps <- Garmin.laps(id.toDouble) do
        val layout = defaultLayout.setTitle("速心比变化趋势")
        mod.newPlot(div(point.before(_)), Arr(laps.scatter), layout, defaultConfig)

  private inline def onRunningWorkoutPage(id: String, point: Element)(using Scatter[ActivityLaps], Box[ActivityLaps]) =
    for list <- Garmin.activityLapsList(id.toDouble, 7) do
      mod.newPlot(
        div(point.appendChild, "latest-7-days-box"),
        list.reverse.map(_.box),
        defaultLayout.setTitle("近七日速心比分布趋势"),
        defaultConfig
      )
      mod.newPlot(
        div(point.appendChild, "latest-7-days-scatter"),
        list.reverse.map(_.scatter),
        defaultLayout.setTitle("近七日速心比趋势对比"),
        defaultConfig
      )
    end for

  end onRunningWorkoutPage

  private inline def defaultConfig = PartialConfig().setResponsive(true)

  private inline def defaultLayout = PartialLayout()
    .setHeight(200)
    .setMargin(PartialMargin().setPad(4).setL(50).setR(50).setT(50).setB(50))

  private def div(at: Node => Unit, id: String = "mpb"): HTMLElement =
    inline def create =
      val e = document.createElement("div")
      e.id = id
      at(e)
      e

    (document.getElementById(id) match
      case null => create
      case e    => e
    ).asInstanceOf[HTMLElement]
  end div

  val Activities = Extract: (u: URL) =>
    (for
      n <- "/modern/activities".capture(u.pathname)
      t <- u.searchParams.get("activityType").opt.orElse(Option(js.undefined))
    yield t)

  val Activity = Extract: (u: URL) =>
    (for
      r  <- "/modern/activity/(\\d+)".capture(u.pathname)
      id <- r(1).opt
    yield id)

  val Workout = Extract: (u: URL) =>
    (for
      r  <- "/modern/workout/(\\d+)".capture(u.pathname)
      id <- r(1).opt
      t  <- u.searchParams.get("workoutType").opt
    yield id -> t)

  val PageHeader = Extract: (se: Seq[Element]) =>
    se.map(_.querySelector("div[class*=\"PageHeader\"]")).find(_ != null)

  val ListItem = Extract: (se: Seq[Element]) =>
    se.find(named("li"))

  val ActivityMap = Extract: (se: Seq[Element]) =>
    se.map(_.querySelector("div.activity-map")).find(_ != null)

  val AddedNodes = Extract: (sr: Seq[MutationRecord]) =>
    Option(sr.flatMap(_.addedNodes.toSeq).collect({ case e: Element => e }))

  inline def named(value: String): Node => Boolean = _.nodeName == value.toUpperCase()

  extension [A](a: js.UndefOr[A])
    inline def opt: Option[A] = inline a match
      case _: Unit => None
      case _       => Some(a.asInstanceOf[A])

  extension (p: String) inline def capture(s: String) = Option(js.RegExp(p).exec(s))

end Main
