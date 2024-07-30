import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js.{Array as Arr, *}
import scala.scalajs.js.JSConverters.*

import org.scalajs.dom.*

import common.*
import common.Functional.*
import common.GetElement.query
import common.GetMutationRecord.*
import common.GetURL.*
import common.Regex.*
import garmin.*
import plot.*

object OnPageActivity:
  def apply()(using aa: Anchor[HTMLElement], s: Scatter[Laps]): Route =
    case (Activity(Seq(_, id: String)), ActivityMap(e)) =>
      for
        a <- Service.activity(id.toDouble)
        t <- a.activityTypeDTO
        k <- t.typeKey if k == "running"
      do
        for laps <- Service.laps(id.toDouble) if laps.nonEmpty do
          Plot(aa.init(e.before(_), "scatter"), Arr(laps), "速心比变化趋势")(using _.scatter)
  end apply

  private val Activity = Extract[URL, Seq[UndefOr[String]]]:
    path |> "/modern/activity/(\\d+)".capture

  private val ActivityMap = Extract[Seq[MutationRecord], Element]:
    inline def E = Extract[Element, Element]:
      query("div.activity-map").??

    _.flatMap(addedNodes[Element]).collectFirst({ case E(e) => e })
end OnPageActivity
