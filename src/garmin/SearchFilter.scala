package garmin

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

trait SearchFilter extends Pagination:
  var activityType: js.UndefOr[String]     = js.undefined
  var startDate: js.UndefOr[String]        = js.undefined
  @JSName("_") var now: js.UndefOr[Double] = js.undefined

object SearchFilter:
  def apply(tp: String): SearchFilter = new:
    excludeChildren = false
    start = 0
    limit = 30
    activityType = tp
    now = js.Date.now()
