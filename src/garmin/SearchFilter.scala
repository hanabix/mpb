package garmin

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

trait SearchFilter extends js.Object:
  var activityType: js.UndefOr[String]     = js.undefined
  var start: js.UndefOr[Int | String]      = js.undefined
  var limit: js.UndefOr[Int | String]      = js.undefined
  var excludeChildren: js.UndefOr[Boolean] = js.undefined
  var startDate: js.UndefOr[String]        = js.undefined
  @JSName("_") var now: js.UndefOr[Double] = js.undefined

object SearchFilter:
  def apply(tp: String): SearchFilter = new:
    excludeChildren = false
    start = 0
    limit = 30
    activityType = tp
    now = js.Date.now()
