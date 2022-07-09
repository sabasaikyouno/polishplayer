import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.input.TransferMode
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer
import scalafx.Includes._

object DragEvent {

  def setDragEvent()(implicit stage: PrimaryStage, embeddedMediaPlayer: EmbeddedMediaPlayer) = {
    setDragOver(stage.getScene)
    setDragDropped(stage.getScene, embeddedMediaPlayer)
  }

  private def setDragOver(scene: Scene) =
    scene.onDragOver = event => {
      if (event.getGestureSource != scene && event.getDragboard.hasFiles)
        event.acceptTransferModes(TransferMode.CopyOrMove:_*)

      event.consume()
    }

  private def setDragDropped(scene: Scene, embeddedMediaPlayer: EmbeddedMediaPlayer) =
    scene.onDragDropped = event => {
      val db = event.getDragboard
      db.getFiles.forEach { f =>
        MoviePlayer.play(embeddedMediaPlayer, f.getAbsolutePath)
      }
      event.consume()
    }

}
