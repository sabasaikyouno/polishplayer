import scalafx.application.JFXApp3
import NodeCreator._
import MediaPlayerCreator._
import DragEvent.setDragEvent
import KeyMouseEvent._
import scalafx.Includes._

object SFMain extends JFXApp3 {
  override def start(): Unit = {
    implicit val videoStack = createStackPane
    val videoImageView = createVideoImageView(videoStack)
    stage = createStage(videoStack)
    implicit val stageImp = stage
    implicit val embeddedMediaPlayer = createMediaPlayer(videoImageView)

    videoStack.children = Seq(videoImageView)

    setDragEvent()
    setKeyMouseEvent()
  }
}
