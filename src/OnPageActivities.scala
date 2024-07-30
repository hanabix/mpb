import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js.*

import org.scalajs.dom.*

import common.*
import common.Functional.*
import common.GetURL.*
import common.Regex.*
import garmin.*
import plot.*

object OnPageActivities:

  def apply()(using a: Anchor[HTMLElement], s: Scatter[SearchResult]): Route =
    case (Activities(tp), HasListItem()) =>
      val root = a.init(document.querySelector("div.advanced-filtering").before(_), "mpb")
      if tp != "running" then root.remove()
      else for gas <- Service.activities(SearchFilter(tp.get)) do Plot(root, Array(gas), "速心比变化趋势")(using _.scatter)
  end apply

  private val Activities = Extract[URL, UndefOr[String]]:
    (path |> "/modern/activities".re.test) ?> param("activityType")

  private val HasListItem = Predicate[Seq[MutationRecord]]:
    _.flatMap(_.addedNodes).exists(_.nodeName == "LI")
end OnPageActivities
