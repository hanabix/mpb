package garmin

import scala.scalajs.js.*

import org.scalajs.dom.*

final case class Get(url: String, referrer: String)
object Get:
  given Conversion[Get, Request] =
    case Get(u, r) => Request(u, init(r))

  private def init(ref: String) = new RequestInit:
    headers = headersInit
    referrerPolicy = ReferrerPolicy.`origin-when-cross-origin`
    method = HttpMethod.GET
    mode = RequestMode.cors
    credentials = RequestCredentials.include
    referrer = ref
    body = undefined

  private inline def headersInit = Dynamic
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

  private inline def accessToken = JSON.parse(window.localStorage.getItem("token")).access_token
  private inline def bustValue   = Dynamic.global.URL_BUST_VALUE

end Get
