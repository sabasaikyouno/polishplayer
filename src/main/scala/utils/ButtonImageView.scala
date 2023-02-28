package utils

import scalafx.scene.image.{Image, ImageView}

object ButtonImageView {

  val playButton: ImageView = createImageView("/play.png")
  val stopButton: ImageView = createImageView("/stop.png")
  val fullScreenReleaseButton: ImageView = createImageView("/fullscreenRelease.png")
  val fullScreenButton: ImageView = createImageView("/fullscreen.png")
  val backButton: ImageView = createImageView("/back.png")
  val forwardButton: ImageView = createImageView("/forward.png")
  val iconifiedButton: ImageView = createImageView("/iconified.png")
  val exitButton: ImageView = createImageView("/exit.png")
  val maximizedButton: ImageView = createImageView("/maximized.png")
  val minimizedButton: ImageView = createImageView("/minimized.png")
  val settingsButton: ImageView = createImageView("/settings.png")
  val mediaInfoButton: ImageView = createImageView("/mediaInfo.png")

  private def createImageView(buttonImagePath: String) =
    new ImageView(
      new Image(getClass.getResourceAsStream(buttonImagePath))
    )
}
