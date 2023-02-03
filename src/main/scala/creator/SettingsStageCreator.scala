package creator

import scalafx.stage.{Modality, Stage}

object SettingsStageCreator {
  def createSettingsStage() = {
    new Stage {
      title = "settings"
      initModality(Modality.ApplicationModal)
    }
  }
}
