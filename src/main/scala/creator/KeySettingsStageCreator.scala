package creator

import scalafx.stage.{Modality, Stage}

object KeySettingsStageCreator {
  def createSettingsStage() = {
    new Stage {
      title = "settings"
      initModality(Modality.ApplicationModal)
    }
  }
}
