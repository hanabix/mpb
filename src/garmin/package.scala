package garmin

import scala.scalajs.js

inline val base = "https://connect.garmin.cn"

type SearchResult      = js.Array[ActivityBySearch]
type Laps              = js.Array[Lap]
type ActivityBySearch  = Activity & Workout & Performance
type ActivityByWorkout = (Activity & Workout, Laps)
