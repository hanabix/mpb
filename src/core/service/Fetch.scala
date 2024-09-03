package core.service

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js

import org.scalajs.dom.*

trait Fetch[A, B] extends (A => Future[B]):
  extension (a: A) inline def request = this(a)
object Fetch:
  type Req[A] = Fetch[Request, A]

  given req[A]: Req[A] = r =>
    for
      rr <- fetch(r).toFuture
      j  <- rr.json().toFuture
    yield j.asInstanceOf[A]

end Fetch
