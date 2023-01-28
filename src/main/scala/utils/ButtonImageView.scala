package utils

import scalafx.scene.image.{Image, ImageView}

object ButtonImageView {

  val playButtonImageView: ImageView = createImageView("play.png")
  val stopButtonImageView: ImageView = createImageView("stop.png")
  val fullScreenReleaseButtonImageView: ImageView = createImageView("fullscreenRelease.png")
  val fullScreenButtonImageView: ImageView = createImageView("fullscreen.png")

  private def createImageView(buttonImagePath: String) =
    new ImageView(
      new Image(getClass.getResourceAsStream(buttonImagePath))
    )
}
