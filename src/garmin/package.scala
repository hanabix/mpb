package garmin

import scala.scalajs.js

inline val base = "https://connect.garmin.cn"

type Activities   = js.Array[ActivityItem]
type Laps         = js.Array[Lap]
type ActivityItem = Activity & Workout & Performance
type ActivityLaps = (Activity & Workout, Laps)
