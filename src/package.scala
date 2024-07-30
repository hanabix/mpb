import org.scalajs.dom.*

import common.*

type Page  = (URL, Seq[MutationRecord])
type Route = Extract[Page, Unit]
