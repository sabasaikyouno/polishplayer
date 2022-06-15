import scalafx.application.JFXApp3
import NodeCreator._
import MediaPlayerCreator._
import DragEvent.setDragEvent
import scalafx.Includes._

object SFMain extends JFXApp3 {
  override def start(): Unit = {
    val videoStack = createStackPane
    val videoImageView = createVideoImageView(videoStack)
    stage = createStage(videoStack)
    val embeddedMediaPlayer = createMediaPlayer(videoImageView)

    videoStack.children = Seq(videoImageView)

    setDragEvent(stage.getScene, embeddedMediaPlayer)
  }
}
