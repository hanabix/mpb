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
      for list <- Service.activityLapsList(id.toDouble, 7) do
        val r = list.reverse
        Plot(a.init(e.appendChild, "box"), r, "近七日速心比分布趋势")(using _.box)
        Plot(a.init(e.appendChild, "scatter"), r, "近七日速心比趋势对比")(using _.scatter)

  private val Workout = Extract[URL, (Seq[UndefOr[String]], UndefOr[String])]:
    (path |> "/modern/workout/(\\d+)".capture) ~> param("workoutType")

  private val PageHeader = Extract[Seq[MutationRecord], Element]:
    inline def E = Extract[Element, Element]:
      query("div[class*=\"PageHeader\"]").??

    _.flatMap(addedNodes[Element]).collectFirst({ case E(e) => e })

end OnPageWorkout
