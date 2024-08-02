package plotly

import core.DateFormat
import core.metrics.*

trait Title[A] extends (A => String):
  extension (a: A) inline def title = this(a)
object Title:
  given date(using DateFormat[String]): Title[Intervals] = oi => s"${oi.head.startTimeGMT.ymd("zh-CN")} 速心比走势"
  given Title[History]                                   = _ => "速心比分布走势"
end Title
