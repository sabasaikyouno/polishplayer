import scalafx.Includes._
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Scene
import javafx.scene.input.KeyCode._
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer
import MediaPlayerEvent._
import javafx.scene.input.MouseButton._
import scalafx.scene.layout.StackPane

object KeyMouseEvent {
  def setKeyMouseEvent()(
    implicit
    stage: PrimaryStage,
    embeddedMediaPlayer: EmbeddedMediaPlayer,
    videoStack: StackPane) =
  {
    setKeyEvent(stage.getScene)
    setMouseEvent()
  }


  private def setKeyEvent(scene: Scene)(implicit stage: PrimaryStage, embeddedMediaPlayer: EmbeddedMediaPlayer) =
    scene.onKeyPressed = keyEvent =>
      keyEvent.getCode match {
        case SPACE => pause()
        case RIGHT => fastForward()
        case LEFT => rewind()
        case UP => upVolume()
        case DOWN => downVolume()
        case M => muteVolume()
        case F11 => fullScreen()
      }

  private def setMouseEvent()(implicit videoStack: StackPane) = {
    videoStack.onMouseReleased = mouseEvent =>
      mouseEvent.getButton match {
        case PRIMARY => videoStack.requestFocus()
        case SECONDARY =>
          ??? // 1 ^ 1 ... 0 ^ 1
      }
  }

}
