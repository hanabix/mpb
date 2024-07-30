package garmin

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.JSConverters.*

import org.scalajs.dom.*

import common.*
import common.Functional.*

object Service:
  def activityLapsList(workoutId: Double, limit: Int)(using
    DateFormat[Double]
  ): Future[js.Array[ActivityLaps]] =
    val filter = SearchFilter("running")
    filter.limit = limit

    activities(filter)
      .flatMap: sa =>
        Future.sequence:
          for a <- sa if a.workoutId == workoutId yield laps(a.activityId.get).map(l => a -> l)
      .map(_.toJSArray)
  end activityLapsList

  def activity(id: Double) =
    val url = s"${base}/activity-service/activity/$id?_=${js.Date.now()}"
    get[Activity](url, s"https://connect.garmin.cn/modern/activity/$id")

  def activities(filter: SearchFilter) =
    val url = s"${base}/activitylist-service/activities/search/activities${params(filter)}"
    // TODO improve referer
    get[Activities](url, "https://connect.garmin.cn/modern/activities")

  def laps(activityId: Double) =
    inline def equal[A](a: A): js.UndefOr[A] => Boolean = a == _
    inline def running: Lap => Boolean = ((_: Lap).intensityType) |> (equal("ACTIVE") || equal("INTERVAL"))

    val url = s"${base}/activity-service/activity/${activityId}/splits?_=${js.Date.now()}"
    for r <- get[js.Dynamic](url, s"https://connect.garmin.cn/modern/activity/${activityId}")
    yield r.lapDTOs.asInstanceOf[Laps].filter(running)

  private inline def get[A](url: String, referrerV: String = window.location.href): Future[A] =
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
  end get

  private inline def params(o: js.Object) =
    val es = js.Object.entries(o)
    if es.isEmpty then "" else "?" + (for js.Tuple2(k, v) <- es yield s"$k=$v").mkString("&")

  private inline def accessToken = js.JSON.parse(window.localStorage.getItem("token")).access_token
  private inline def bustValue   = js.Dynamic.global.URL_BUST_VALUE

  extension (d: Double) inline def dayBefore(n: Int) = d - (n * 24 * 60 * 60 * 1000)

end Service
