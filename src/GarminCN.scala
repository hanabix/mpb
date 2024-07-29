import scala.concurrent.ExecutionContext.Implicits.global as gec
import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.Dynamic.global
import scala.scalajs.js.JSConverters.*
import scala.scalajs.js.annotation.*

import org.scalajs.dom.*

object Garmin:
  inline val base = "https://connect.garmin.cn"

  type DateString = String

  type ActivityLaps = (Cast[Activity & Workout], js.Array[Cast[Lap]])

  trait ActivitiesFilter extends js.Object:
    var activityType: js.UndefOr[String]     = js.undefined
    var start: js.UndefOr[Int | String]      = js.undefined
    var limit: js.UndefOr[Int | String]      = js.undefined
    var excludeChildren: js.UndefOr[Boolean] = js.undefined
    var startDate: js.UndefOr[DateString]    = js.undefined
    @JSName("_") var now: js.UndefOr[Double] = js.undefined
  object ActivitiesFilter:
    def apply(tp: String): ActivitiesFilter = new:
      excludeChildren = false
      start = 0
      limit = 30
      activityType = tp
      now = js.Date.now()

  trait Performace extends js.Object:
    var startTimeLocal: js.UndefOr[String]        = js.undefined
    var distance: js.UndefOr[Double]              = js.undefined
    var averageHR: js.UndefOr[Double]             = js.undefined
    var averageRunCadence: js.UndefOr[Double]     = js.undefined
    var averageSpeed: js.UndefOr[Double]          = js.undefined
    var avgGradeAdjustedSpeed: js.UndefOr[Double] = js.undefined

  object Performace:
    given mpb: MetersPerBeat[Performace] = p => p.averageSpeed.get * 60 / p.averageHR.get
    given text: HoverText[Performace] = p =>
      Seq(
        unit(p.averageHR, "bpm"),
        unit(Math.round(p.averageRunCadence.get), "spm"),
        pace(p.averageSpeed),
        pace(p.avgGradeAdjustedSpeed),
        unit(Math.round(p.distance.get), "m")
      ).mkString("<br>")

    private inline def unit(p: js.Any, s: js.Any) = s"$p<sub>$s</sub>"
    private inline def pace(v: js.UndefOr[Double]) =
      (for
        d <- v
        sec = Math.round(1000 / d)
        m   = Math.round(sec / 60)
        s   = sec % 60
      yield unit("%02d:%02d".format(m, s), "/km")).getOrElse("--")

  end Performace

  trait Lap extends Performace:
    var intensityType: js.UndefOr[String] = js.undefined
    var lapIndex: js.UndefOr[Double]      = js.undefined

  trait Workout extends js.Object:
    var workoutId: js.UndefOr[Double] = js.undefined

  trait Activity extends Performace:
    var activityId: js.UndefOr[Double]            = js.undefined
    var activityName: js.UndefOr[String]          = js.undefined
    var summaryDTO: js.UndefOr[Performace]        = js.undefined
    var activityTypeDTO: js.UndefOr[ActivityType] = js.undefined

  trait ActivityType extends js.Object:
    var typeKey: js.UndefOr[String] = js.undefined

  def activityLapsList(workoutId: Double, latestDays: Double)(using
    DateFormat[Double]
  ): Future[js.Array[ActivityLaps]] =
    val cur = js.Date.now()
    def filter = new Garmin.ActivitiesFilter:
      activityType = "running"
      excludeChildren = false
      startDate = cur.dayBefore(7).ymd("fr-CA")
      start = 0
      limit = 20
      now = cur

    Garmin
      .activities(filter)
      .flatMap: sa =>
        Future.sequence:
          for a <- sa if a.workoutId == workoutId yield Garmin.laps(a.activityId.get).map(l => a -> l)
      .map(_.toJSArray)
  end activityLapsList

  def activity(id: Double) =
    val url = s"${base}/activity-service/activity/$id?_=${js.Date.now()}"
    service[Cast[Activity]](url, s"https://connect.garmin.cn/modern/activity/$id")

  def activities(filter: ActivitiesFilter) =
    val url = s"${base}/activitylist-service/activities/search/activities${params(filter)}"
    // TODO improve referer
    service[js.Array[Cast[Activity & Workout & Performace]]](url, "https://connect.garmin.cn/modern/activities")

  def laps(activityId: Double) =
    val url = s"${base}/activity-service/activity/${activityId}/splits?_=${js.Date.now()}"
    for r <- service[js.Dynamic](url, s"https://connect.garmin.cn/modern/activity/${activityId}")
    yield r.lapDTOs.asInstanceOf[js.Array[Cast[Lap]]]

  inline def service[A](url: String, referrerV: String = window.location.href): Future[A] =
    val hi = js.Dynamic
      .literal(
        "accept"             -> "application/json, text/javascript, */*; q=0.01",
        "accept-language"    -> "zh-CN,zh;q=0.9,en;q=0.8",
        "authorization"      -> s"Bearer ${accessToken}",
        "di-backend"         -> "connectapi.garmin.cn",
        "nk"                 -> "NT",
        "priority"           -> "u=1, i",
        "sec-ch-ua"          -> "\"Not/A)Brand\";v=\"8\", \"Chromium\";v=\"126\", \"Google Chrome\";v=\"126\"",
        "sec-ch-ua-mobile"   -> "?0",
        "sec-ch-ua-platform" -> "\"macOS\"",
        "sec-fetch-dest"     -> "empty",
        "sec-fetch-mode"     -> "cors",
        "sec-fetch-site"     -> "same-origin",
        "x-app-ver"          -> bustValue,
        "x-lang"             -> "zh-CN",
        "x-requested-with"   -> "XMLHttpRequest"
      )
      .asInstanceOf[HeadersInit]

    val init: RequestInit = new:
      headers = hi
      referrerPolicy = ReferrerPolicy.`origin-when-cross-origin`
      method = HttpMethod.GET
      mode = RequestMode.cors
      credentials = RequestCredentials.include
      referrer = referrerV
      body = null

    fetch(url, init).toFuture.flatMap(_.json().toFuture.map(_.asInstanceOf[A]))
  end service

  private inline def params(o: js.Object) =
    val es = js.Object.entries(o)
    if es.isEmpty then "" else "?" + (for js.Tuple2(k, v) <- es yield s"$k=$v").mkString("&")

  private inline def accessToken = js.JSON.parse(window.localStorage.getItem("token")).access_token
  private inline def bustValue   = global.URL_BUST_VALUE

  extension (d: Double) inline def dayBefore(n: Int) = d - (n * 24 * 60 * 60 * 1000)

end Garmin
