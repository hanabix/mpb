import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js.*

import org.scalajs.dom.*

import Functional.*
import Garmin.*
import GetURL.*
import Regex.*
import Unapply.*

object OnPageActivities:
  type ActivityDTO = Cast[Activity & Workout & Performace]
  
  def apply()(using a: Anchor[HTMLElement], s: Scatter[Array[ActivityDTO]]): Route =
    case (Activities(tp), HasListItem()) =>
      val root = a.init(document.querySelector("div.advanced-filtering").before(_), "mpb")
      if tp != "running" then root.remove()
      else for gas <- activities(ActivitiesFilter(tp.get)) do Plot(root, Array(gas), "速心比变化趋势")(using _.scatter)
  end apply

  private val Activities = Extract[URL, UndefOr[String]]:
    (path |> "/modern/activities".re.test) ?> param("activityType")

  private val HasListItem = Predicate[Seq[MutationRecord]]:
    _.flatMap(_.addedNodes).exists(_.nodeName == "LI")
end OnPageActivities
