
import org.scalajs.dom.*

object Main:

  def main(args: Array[String]): Unit =
    val routes = OnPageActivities() orElse OnPageActivity() orElse OnPageWorkout()
    MutationObserver: (rs, _) => 
      routes.applyOrElse((new URL(window.location.href), rs.toSeq), _ => ())
    .observe(
      document.querySelector("div.main-body"),
      new:
        childList = true
        subtree = true
    )
  end main
end Main
