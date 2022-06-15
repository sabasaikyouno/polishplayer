import scalafx.scene.Scene
import scalafx.scene.input.TransferMode
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer

object DragEvent {

  def setDragEvent(scene: Scene, embeddedMediaPlayer: EmbeddedMediaPlayer) = {
    setDragOver(scene)
    setDragDropped(scene, embeddedMediaPlayer)
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
        MoviePlayer.play(embeddedMediaPlayer,f.getAbsolutePath)
      }
      event.consume()
    }

}
