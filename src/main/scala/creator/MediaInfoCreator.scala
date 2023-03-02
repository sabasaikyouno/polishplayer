package creator

import scalafx.scene.Scene
import scalafx.scene.layout.{HBox, VBox}
import scalafx.scene.text.Text
import scalafx.stage.Stage
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer

import scala.util.Try

object MediaInfoCreator {
  def createMediaInfoStage(embeddedMediaPlayer: EmbeddedMediaPlayer) =
    new Stage {
      title = "media info"
      scene = new Scene(createMediaInfoVBox(embeddedMediaPlayer))
      onShowing = _ => scene = new Scene(createMediaInfoVBox(embeddedMediaPlayer))
    }

  private def createMediaInfoVBox(embeddedMediaPlayer: EmbeddedMediaPlayer) =
    new VBox(
      createHBox("mrl ", Try(embeddedMediaPlayer.media().info().mrl()).getOrElse(""))
    )

  private def createHBox(text: String, text2: String) =
    new HBox(
      new Text(text),
      new Text(text2)
    )
}
