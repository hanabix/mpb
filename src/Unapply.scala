object Unapply:
  //
  inline def extract[A, B](e: A => Option[B]) = e.unlift

  trait Predicate[A]:
    def unapply(a: A): Boolean
  object Predicate:
    inline def apply[A](p: A => Boolean): Predicate[A] = new:
      def unapply(a: A) = p(a)

  /** It is more readable than converting a function to extractor by [[scala.Function.unlift]].
    * @see
    *   [[scala.PartialFunction]]
    */
  trait Extract[A, B]:
    def unapply(a: A): Option[B]
  object Extract:
    inline def apply[A, B](e: A => Option[B]): Extract[A, B] = new:
      def unapply(a: A) = e(a)

end Unapply
