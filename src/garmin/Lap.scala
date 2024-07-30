package garmin

import scala.scalajs.js

trait Lap extends Performance:
  var intensityType: js.UndefOr[String] = js.undefined
  var lapIndex: js.UndefOr[Double]      = js.undefined
