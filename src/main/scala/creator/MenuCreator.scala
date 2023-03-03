package creator

import creator.MediaInfoCreator.createMediaInfoStage
import scalafx.scene.Scene
import scalafx.scene.control.Button
import scalafx.scene.layout.VBox
import scalafx.stage.Stage
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer

object MenuCreator {
  def createMenuStage(embeddedMediaPlayer: EmbeddedMediaPlayer) =
    new Stage {
      title = "menu"
      scene = new Scene(createMenuVBox(embeddedMediaPlayer))
    }

  def createMenuVBox(embeddedMediaPlayer: EmbeddedMediaPlayer) =
    new VBox(
      createStageButton("media info", createMediaInfoStage(embeddedMediaPlayer))
    )

  private def createStageButton(buttonText: String, stage: Stage) =
    new Button {
      text = buttonText
      onMouseClicked = _ => stage.show()
    }
}
