package core.service

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js

import org.scalajs.dom.*

trait Fetch[A, B]:
  extension (a: A) def request: Future[B]
object Fetch:

  given [A]: Fetch[Request, A] with
    extension (r: Request)
      def request: Future[A] =
        for
          rr <- fetch(r).toFuture
          j  <- rr.json().toFuture
        yield j.asInstanceOf[A]

  given [A, B](using Conversion[A, Request], Fetch[Request, B]): Fetch[A, B] with
    extension (a: A) def request: Future[B] = a.convert.request
end Fetch
