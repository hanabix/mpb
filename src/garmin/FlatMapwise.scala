package garmin

type Id[A] = A

trait FlatMapwise[F[_]]:
  extension [A](fa: F[A])
    def option[B](f: A => Option[B]): Option[B]
    def seq[B](f: A => IterableOnce[B]): Seq[B]
object FlatMapwise:

  given FlatMapwise[Id] with
    extension [A](fa: Id[A])
      inline def option[B](f: A => Option[B]): Option[B] = f(fa)
      inline def seq[B](f: A => IterableOnce[B]): Seq[B] = f(fa).iterator.toSeq

  given FlatMapwise[Seq] with
    extension [A](fa: Seq[A])
      inline def option[B](f: A => Option[B]): Option[B] = fa.collectFirst(f.unlift)
      inline def seq[B](f: A => IterableOnce[B]): Seq[B] = fa.flatMap(f)
end FlatMapwise
