package garmin

import scala.scalajs.js

trait Activity extends Performance:
  var activityId: js.UndefOr[Double]            = js.undefined
  var activityName: js.UndefOr[String]          = js.undefined
  var summaryDTO: js.UndefOr[Performance]       = js.undefined
  var activityTypeDTO: js.UndefOr[ActivityType] = js.undefined
  var activityType: js.UndefOr[ActivityType]    = js.undefined
