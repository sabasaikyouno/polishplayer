import scalafx.scene.image.{Image, ImageView}

object ButtonImageView {

  val playButtonImageView = createImageView("play.png")
  val stopButtonImageView = createImageView("stop.png")
  val fullScreenReleaseButtonImageView = createImageView("fullscreenRelease.png")
  val fullScreenButtonImageView = createImageView("fullscreen.png")

  private def createImageView(buttonImagePath: String) =
    new ImageView(
      new Image(getClass.getResourceAsStream(buttonImagePath))
    )
}
