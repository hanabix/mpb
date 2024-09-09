package plotly

import core.DateTimeGMT
import core.metrics.*

trait Title[A] extends (A => String):
  extension (a: A) inline def title = this(a)
object Title:
  given date(using DateTimeGMT[String]): Title[Intervals] = oi =>
    s"${(oi.head.startTimeGMT).ymd("fr-CA")} 速心比关联走势"
  given Title[History] = _ => "速心比分布走势"
end Title
