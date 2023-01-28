import scalafx.application.JFXApp3
import creator.NodeCreator._
import creator.MediaPlayerCreator._
import event.DragEvent.setDragEvent
import event.KeyMouseEvent._
import scalafx.Includes._
import creator.SfxTitleBarCreator.createTitleBar
import creator.SfxToolBarCreator.createToolBar
import scalafx.scene.Scene
import event.ClosedEvent.setClosedStageEvent
import event.MediaPlayerEvent.setMediaPlayerReady
import creator.TimeThumbnailCreator._

object Main extends JFXApp3 {
  override def start(): Unit = {
    implicit val videoStack = createStackPane
    val videoImageView = createVideoImageView(videoStack)
    stage = createStage(videoStack)
    implicit val stageImp = stage
    implicit val embeddedMediaPlayer = createMediaPlayer(videoImageView)
    implicit val sceneImp: Scene = stage.getScene


    val timeThumbnail = createTimeThumbnailImageView()
    val timeThumbnailEmbedded = createTimeThumbnailEmbedded(timeThumbnail)

    val toolBar = createToolBar(timeThumbnail, timeThumbnailEmbedded)

    videoStack.children = Seq(videoImageView, timeThumbnail, toolBar, createTitleBar())

    videoStack.requestFocus()

    setDragEvent(timeThumbnailEmbedded)
    setKeyMouseEvent(toolBar, timeThumbnail)
    setClosedStageEvent()
    setMediaPlayerReady()
  }
}
