import scalafx.application.JFXApp3
import NodeCreator._
import MediaPlayerCreator._
import DragEvent.setDragEvent
import KeyMouseEvent._
import scalafx.Includes._
import SfxTitleBarCreator.createTitleBar
import SfxToolBarCreator.createToolBar
import scalafx.scene.Scene
import ClosedEvent.setClosedEvent

object Main extends JFXApp3 {
  override def start(): Unit = {
    implicit val videoStack = createStackPane
    val videoImageView = createVideoImageView(videoStack)
    stage = createStage(videoStack)
    implicit val stageImp = stage
    implicit val embeddedMediaPlayer = createMediaPlayer(videoImageView)
    implicit val sceneImp: Scene = stage.getScene

    val toolBar = createToolBar()

    videoStack.children = Seq(videoImageView, toolBar, createTitleBar())

    videoStack.requestFocus()

    setDragEvent()
    setKeyMouseEvent(toolBar)
    setClosedEvent()
  }
}
