package event

import scalafx.Includes._
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.input.TransferMode
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer
import utils.MoviePlayer

object DragEvent {

  def setDragEvent(timeThumbnailEmbedded: EmbeddedMediaPlayer)(implicit stage: PrimaryStage, embeddedMediaPlayer: EmbeddedMediaPlayer) = {
    setDragOver(stage.getScene)
    setDragDropped(stage.getScene, embeddedMediaPlayer, timeThumbnailEmbedded)
  }

  private def setDragOver(scene: Scene) =
    scene.onDragOver = event => {
      if (event.getGestureSource != scene && event.getDragboard.hasFiles)
        event.acceptTransferModes(TransferMode.CopyOrMove:_*)

      event.consume()
    }

  private def setDragDropped(scene: Scene, embeddedMediaPlayer: EmbeddedMediaPlayer, timeThumbnailEmbedded: EmbeddedMediaPlayer) =
    scene.onDragDropped = event => {
      val db = event.getDragboard
      db.getFiles.forEach { f =>
        MoviePlayer.play(embeddedMediaPlayer, f.getAbsolutePath)
        MoviePlayer.startPaused(timeThumbnailEmbedded, f.getAbsolutePath)
      }
      event.consume()
    }

}
