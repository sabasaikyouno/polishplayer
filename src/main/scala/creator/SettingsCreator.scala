package creator

import creator.KeySettingsStageCreator.createKeySettingsStage
import creator.MediaPlayerEventSettingsCreator.createMediaPlayerEventSettingsStage
import scalafx.scene.Scene
import scalafx.scene.control.Button
import scalafx.scene.layout.VBox
import scalafx.stage.{Modality, Stage}

object SettingsCreator {

  def createSettingsStage =
    new Stage {
      title = "settings"
      initModality(Modality.ApplicationModal)
      scene = new Scene(createSettingsVBox)
    }

  private def createSettingsVBox =
    new VBox(
      createSettingButton("keySettings", createKeySettingsStage),
      createSettingButton("mediaPlayerEventSettings", createMediaPlayerEventSettingsStage)
    )

  private def createSettingButton(buttonText: String, stage: Stage) =
    new Button {
      text = buttonText
      onMouseClicked = _ => stage.show()
    }

}
