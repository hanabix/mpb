package garmin

import java.util.NoSuchElementException as Complain

import org.scalajs.dom.Element
import org.scalajs.dom.HTMLElement
import org.scalajs.dom.document

import core.*
import sourcecode.Name

sealed trait Profile
object Profile:
  given (using Initialize[HTMLElement], Inject[Seq[ActivityId]]): Proceed[Profile] = Proceed:
    case (URL(s"/modern/profile/$_", _), `a[data-activityid]`(_)) =>
      val ids = for 
        case a @ `data-activityid`(sid)  <- `a[data-activityid]`.all(Seq(document)) if isRunning(a)
      yield ActivityId(sid)

      if ids.nonEmpty then
        val e = `div[class^="PageContent"]`(document).getOrElse(throw Complain("anchor"))
        ids.inject("mpb".elementAt(e.before(_)))
  end given

  private inline def isRunning(e: Element): Boolean =
    (for
      case `class`(s"icon-activity-${t}") <- `i[class^="icon-activity-"]`(e)
      if t.trim.nn.endsWith("running")
    yield t).isDefined
  end isRunning

  private val `class`                      = implicitly[Name].value |> Attribute
  private val `data-activityid`            = implicitly[Name].value |> Attribute
  private val `a[data-activityid]`         = implicitly[Name].value |> Selector[Seq]
  private val `div[class^="PageContent"]`  = implicitly[Name].value |> Selector[Id]
  private val `i[class^="icon-activity-"]` = implicitly[Name].value |> Selector[Id]

end Profile
