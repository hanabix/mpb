import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js

import org.scalajs.dom.*

import common.Anchor
import common.Extract
import common.Functional.*
import common.GetElement.query
import common.GetMutationRecord.addedNodes
import common.GetURL.path
import common.Regex.*
import garmin.Pagination
import garmin.SearchResult
import garmin.Service
import plot.Plot
import plot.Scatter

object OnPageProfile:
  def apply()(using a: Anchor[HTMLElement], s: Scatter[SearchResult]): Route =
    case (Profile(Seq(_, id: String)), PageContent(e)) =>
      val p: Pagination = new:
        start = 1
        limit = 30
      for ap <- Service.activitiesByProfile(id, p) do
        val sr     = ap.activityList.get.filter(_.activityType.get.typeKey == "running")
        val layout = Plot.Layout("近期跑步速心比变化趋势")
        val config = Plot.Config().setDisplayModeBar(false)
        Plot(a.init(e.before(_), "scatter"), js.Array(sr), layout, config)(using _.scatter)
  end apply

  private val Profile = Extract[URL, Seq[js.UndefOr[String]]]:
    path |> "/modern/profile/(.+)".capture

  private val PageContent = Extract[Seq[MutationRecord], Element]:
    inline def E = Extract[Element, Element]:
      query("div[class^=\"PageContent\"]").??

    _.flatMap(addedNodes[Element]).collectFirst({ case E(e) => e })
end OnPageProfile
