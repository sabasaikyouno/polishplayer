package event

import event.MediaPlayerEvent._
import javafx.scene.input.MouseButton._
import scalafx.Includes._
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.image.ImageView
import scalafx.scene.layout.{StackPane, VBox}
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer
import utils.KeySettings.findKeySetting

object KeyMouseEvent {

  def setKeyMouseEvent(toolBar: VBox, timeThumbnail: ImageView)(
    implicit
    stage: PrimaryStage,
    embeddedMediaPlayer: EmbeddedMediaPlayer,
    videoStack: StackPane) =
  {
    setKeyEvent(stage.getScene)
    setMouseEvent(toolBar, timeThumbnail: ImageView)
  }

  private def setKeyEvent(scene: Scene)(implicit stage: PrimaryStage, embeddedMediaPlayer: EmbeddedMediaPlayer) =
    scene.onKeyPressed = keyEvent =>
      findKeySetting(keyEvent.getCode) match {
        case ("Pause", _) => pause()
        case ("FastForward", _) => fastForward()
        case ("Rewind", _) => rewind()
        case ("UpVolume", _) => upVolume()
        case ("DownVolume", _)=> downVolume()
        case ("MuteVolume", _) => muteVolume()
        case ("FullScreen", _) => fullScreen()
        case (_, digit) if digit.isDigitKey => skipRate(digit.getName.toFloat / 10)
        case ("Pin", _) => println("pin")
        case _ => ()
      }

  private def setMouseEvent(toolBar: VBox, timeThumbnail: ImageView)(implicit videoStack: StackPane) = {
    videoStack.onMouseReleased = mouseEvent =>
      mouseEvent.getButton match {
        case PRIMARY => videoStack.requestFocus()
        case SECONDARY =>
          toolBar.opacity = toolBar.getOpacity.toInt ^ 1
          timeThumbnail.opacity = timeThumbnail.getOpacity.toInt ^ 1
      }

    videoStack.onMouseExited = _ =>
      toolBar.opacity = 0

    videoStack.onMouseEntered = _ =>
      toolBar.opacity = 1
  }

}
