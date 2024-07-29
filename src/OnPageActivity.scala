import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js.{Array as Arr, *}
import scala.scalajs.js.JSConverters.*

import org.scalajs.dom.*

import Functional.*
import Garmin.*
import GetElement.query
import GetMutationRecord.addedNodes
import GetURL.path
import Regex.*
import Unapply.*

object OnPageActivity:
  def apply()(using aa: Anchor[HTMLElement], s: Scatter[Arr[Cast[Lap]]]): Route =
    case (Activity(Seq(_, id: String)), ActivityMap(e)) =>
      for
        a <- activity(id.toDouble)
        t <- a.activityTypeDTO
        k <- t.typeKey if k == "running"
      do for laps <- laps(id.toDouble) do Plot(aa.init(e.before(_), "scatter"), Arr(laps), "速心比变化趋势")(using _.scatter)
  end apply

  private val Activity = Extract[URL, Seq[UndefOr[String]]]:
    path |> "/modern/activity/(\\d+)".capture

  private val ActivityMap = Extract[Seq[MutationRecord], Element]:
    inline def E = Extract[Element, Element]:
      query("div.activity-map").??

    _.flatMap(addedNodes[Element]).collectFirst({ case E(e) => e })
end OnPageActivity
