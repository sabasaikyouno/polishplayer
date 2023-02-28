package creator

import scalafx.scene.Scene
import scalafx.scene.layout.VBox
import scalafx.scene.text.Text
import scalafx.stage.Stage
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer

object MediaInfoCreator {
  def createMediaInfoStage(embeddedMediaPlayer: EmbeddedMediaPlayer) =
    new Stage {
      title = "media info"
      scene = new Scene(createMediaInfoVBox(embeddedMediaPlayer))
    }

  private def createMediaInfoVBox(embeddedMediaPlayer: EmbeddedMediaPlayer) =
    new VBox(
      new Text("embeddedMediaPlayer.media().info().mrl()")
    )
}
