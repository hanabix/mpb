import org.scalajs.dom.*

object Main:

  def main(args: Array[String]): Unit =
    val route = OnPageActivities() orElse OnPageActivity() orElse OnPageWorkout()
    MutationObserver: (rs, _) =>
      route.applyOrElse((new URL(window.location.href), rs.toSeq), _ => ())
    .observe(
      document.querySelector("div.main-body"),
      new:
        childList = true
        subtree = true
    )
  end main

end Main
