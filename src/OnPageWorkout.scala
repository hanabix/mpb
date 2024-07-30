import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js.*
import scala.scalajs.js.JSConverters.*

import org.scalajs.dom.*

import common.*
import common.Functional.*
import common.GetElement.*
import common.GetMutationRecord.*
import common.GetURL.*
import common.Regex.*
import garmin.*
import plot.*

object OnPageWorkout:
  def apply()(using a: Anchor[HTMLElement], s: Scatter[ActivityLaps], b: Box[ActivityLaps]): Route =
    case (Workout(Seq(_, id: String), "running"), PageHeader(e)) =>
      for list <- Service.activityLapsList(id.toDouble, 30) do
        val limit  = Math.min(list.length, 14)
        val latest = Math.min(limit, 5)
        val layout = Plot.Layout(s"最近${limit}次速心比分布趋势").setShowlegend(false)
        Plot(a.init(e.appendChild, "box"), list.take(limit).reverse, layout)(using _.box)
        Plot(a.init(e.appendChild, "scatter"), list.take(latest).reverse, s"近${latest}次速心比趋势对比")(using _.scatter)

  private val Workout = Extract[URL, (Seq[UndefOr[String]], UndefOr[String])]:
    (path |> "/modern/workout/(\\d+)".capture) ~> param("workoutType")

  private val PageHeader = Extract[Seq[MutationRecord], Element]:
    inline def E = Extract[Element, Element]:
      query("div[class*=\"PageHeader\"]").??

    _.flatMap(addedNodes[Element]).collectFirst({ case E(e) => e })

end OnPageWorkout
