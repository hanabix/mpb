package garmin

import java.util as ju

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import org.scalajs.dom.Element
import org.scalajs.dom.HTMLElement
import org.scalajs.dom.document

import core.*
import core.metrics.*
import core.service.Fetch
import sourcecode.Name

sealed trait Profile
object Profile:
  given page(
    using Initialize[HTMLElement],
    Fetch[ActivityId, Intervals],
    Inject[History]
  ): Page[Profile] =
    case (URL(s"/modern/profile/$_", _), `a[data-activityid]`(_)) =>
      val es = `a[data-activityid]`.all(Seq(document)).filter(isRunning).toList
      val fs = for case `data-activityid`(id) <- es yield ActivityId(id).request
      if !fs.isEmpty then
        for history <- Future.sequence(fs) do
          inline def complain[A]: A = throw ju.NoSuchElementException("anchor")
          val e                     = `div[class^="PageContent"]`(document).getOrElse(complain)
          history.filter(_.nonEmpty).inject("mpb".elementAt(e.before(_)))
      end if
      None

    case m =>
      Some(m)
  end page

  private inline def isRunning(e: Element): Boolean =
    val t = for
      case `class`(s"icon-activity-${t}") <- `i[class^="icon-activity-"]`(e)
      if t.endsWith("running")
    yield t
    t.isDefined
  end isRunning

  private val `class`                      = implicitly[Name].value |> Attribute
  private val `data-activityid`            = implicitly[Name].value |> Attribute
  private val `a[data-activityid]`         = implicitly[Name].value |> Selector[Seq]
  private val `div[class^="PageContent"]`  = implicitly[Name].value |> Selector[Id]
  private val `i[class^="icon-activity-"]` = implicitly[Name].value |> Selector[Id]

  extension [A](a: A) inline def |>[B](f: A => B): B = f(a)
end Profile
