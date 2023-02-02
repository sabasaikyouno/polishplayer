package creator

import scalafx.stage.Stage

object SettingsStageCreator {
  def createSettingsStage() = {
    new Stage {
      title = "settings"
    }
  }
}
