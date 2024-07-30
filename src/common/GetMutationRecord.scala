package common

import org.scalajs.dom.*

object GetMutationRecord:
  import scala.reflect.Typeable
  inline def addedNodes[A >: Element: Typeable]: MutationRecord => Seq[A] = r =>
    for case a: A <- r.addedNodes.toSeq yield a
