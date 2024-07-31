package garmin

import scala.scalajs.js

trait Pagination extends js.Object:
  var excludeChildren: js.UndefOr[Boolean] = js.undefined
  var start: js.UndefOr[Int | String]      = js.undefined
  var limit: js.UndefOr[Int | String]      = js.undefined
