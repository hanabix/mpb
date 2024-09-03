import org.scalajs.dom.*

import core.*
import garmin.*
import plotly.given

object Main:

  def main(args: Array[String]): Unit =
    val init = new MutationObserverInit:
      childList = true
      subtree = true

    MutationObserver: (smr, _) =>
      val url   = URL(window.location.href)
      val added = smr.flatMap(_.addedNodes).collect({ case e: HTMLElement => e }).toSeq
      summon[Page[(Activities, Profile)]](url -> added)
    .observe(document.querySelector("div.main-body"), init)

  end main

end Main
