package creator

import scalafx.scene.Scene
import scalafx.stage.{Modality, Stage}

object SettingsCreator {

  def createSettingsStage =
    new Stage {
      title = "settings"
      initModality(Modality.ApplicationModal)
      scene = new Scene()
    }
}
