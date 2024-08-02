package garmin

import java.util as ju

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import org.scalajs.dom.HTMLElement
import org.scalajs.dom.document

import core.*
import core.metrics.*
import core.service.Fetch
import sourcecode.Name

trait Activities
object Activities:
  given page(
    using Initialize[HTMLElement],
    Fetch[ActivityId, Intervals],
    Inject[History]
  ): Page[Activities] =
    case (URL("/modern/activities", `activityType`("running")), `a.inline-edit-target`(_)) =>
      val es = `a.inline-edit-target`.all(Seq(document)).toList
      val fs = for case `href`(s"/modern/activity/$id") <- es yield ActivityId(id).request
      if !fs.isEmpty then
        for history <- Future.sequence(fs) do
          inline def complain[A]: A = throw ju.NoSuchElementException("div.advanced-filtering")
          val e                     = `div.advanced-filtering`(document).getOrElse(complain)
          history.filter(_.nonEmpty).inject(`mpb`.elementAt(e.before(_)))
      end if
      None

    case (URL("/modern/activities", `activityType`("running")), _) =>
      None

    case (URL("/modern/activities", _), _) =>
      `mpb`.reset()
      None

    case m =>
      Some(m)
  end page

  private val `mpb`                    = implicitly[Name].value
  private val `activityType`           = implicitly[Name].value |> Param
  private val `href`                   = implicitly[Name].value |> Attribute
  private val `a.inline-edit-target`   = implicitly[Name].value |> Selector[Seq]
  private val `div.advanced-filtering` = implicitly[Name].value |> Selector[Id]

  extension [A](a: A) inline def |>[B](f: A => B): B = f(a)
end Activities
