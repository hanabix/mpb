package garmin

import java.util.NoSuchElementException as Complain

import org.scalajs.dom.{URL as _, *}

import core.*
import sourcecode.Name

sealed trait Activities
object Activities:
  given (using Initialize[HTMLElement], Inject[List[ActivityId]]): Proceed[Activities] = Proceed:
    case (URL("/modern/activities", `activityType`("running")), `a[href^="/modern/activity/"]`(_)) =>
      val es  = `a[href^="/modern/activity/"]`.all(Seq(document)).toList
      val e   = `div#searchAndFilterContainer`(document).getOrElse(throw Complain("anchor"))
      val ids = for case `href`(s"/modern/activity/$id") <- es yield ActivityId(id)
      ids.inject(`mpb`.elementAt(e.before(_)))

    case (URL("/modern/activities", `activityType`("running")), _) => 
    case (URL("/modern/activities", _), _)                         => `mpb`.reset()
  end given

  private val `mpb`                          = implicitly[Name].value
  private val `activityType`                 = implicitly[Name].value |> Param
  private val `href`                         = implicitly[Name].value |> Attribute
  private val `a[href^="/modern/activity/"]` = implicitly[Name].value |> Selector[Seq]
  private val `div#searchAndFilterContainer` = implicitly[Name].value |> Selector[Id]

end Activities
