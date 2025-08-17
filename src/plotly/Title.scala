package plotly
import core.*

opaque type Title[A] = A => String
object Title:
  def apply[A](a: A)(using t: Title[A]): String = t(a)
  def apply[A](f: A => String): Title[A]        = f

  given [A](using Read[Timestamp, A, String]): Title[Interval[A]] = Title: i =>
    s"${Read[Timestamp, A, String](i.deref.head)} 速心比关联走势"

  given [A]: Title[History[A]] = Title: _ =>
    "速心比分布走势"

end Title
