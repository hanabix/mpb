package garmin

import java.util.NoSuchElementException as Complain

import org.scalajs.dom.Element
import org.scalajs.dom.HTMLElement
import org.scalajs.dom.document

import core.*
import sourcecode.Name

sealed trait Profile
object Profile:
  given page(
    using Initialize[HTMLElement],
    Inject[List[ActivityId]]
  ): Page[Profile] =
    case (URL(s"/modern/profile/$_", _), `a[data-activityid]`(_)) =>
      val es  = `a[data-activityid]`.all(Seq(document)).filter(isRunning).toList
      val ids = for case `data-activityid`(id) <- es yield ActivityId(id)
      val e   = `div[class^="PageContent"]`(document).getOrElse(throw Complain("anchor"))
      ids.inject("mpb".elementAt(e.before(_)))
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

end Profile
