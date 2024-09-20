package garmin

import java.util.NoSuchElementException as Complain

import org.scalajs.dom.{URL as _, *}

import core.*
import sourcecode.Name

sealed trait Activities
object Activities:
  given (using Initialize[HTMLElement], Inject[List[ActivityId]]): Proceed[Activities] = Proceed:
    case (URL("/modern/activities", `activityType`("running")), `a.inline-edit-target`(_)) =>
      val es  = `a.inline-edit-target`.all(Seq(document)).toList
      val e   = `div.advanced-filtering`(document).getOrElse(throw Complain("anchor"))
      val ids = for case `href`(s"/modern/activity/$id") <- es yield ActivityId(id)
      ids.inject(`mpb`.elementAt(e.before(_)))

    case (URL("/modern/activities", `activityType`("running")), _) =>
    case (URL("/modern/activities", _), _)                         => `mpb`.reset()
  end given

  private val `mpb`                    = implicitly[Name].value
  private val `activityType`           = implicitly[Name].value |> Param
  private val `href`                   = implicitly[Name].value |> Attribute
  private val `a.inline-edit-target`   = implicitly[Name].value |> Selector[Seq]
  private val `div.advanced-filtering` = implicitly[Name].value |> Selector[Id]

end Activities
