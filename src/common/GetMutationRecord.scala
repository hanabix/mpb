package common

import org.scalajs.dom.*

object GetMutationRecord:
  import scala.reflect.Typeable
  inline def addedNodes[A >: Element: Typeable]: MutationRecord => Seq[A] =
    _.addedNodes.toSeq.collect({ case a: A => a })
