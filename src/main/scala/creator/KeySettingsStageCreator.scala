package creator

import scalafx.scene.Scene
import scalafx.scene.control.TextField
import scalafx.scene.layout.{HBox, VBox}
import scalafx.scene.text.Text
import scalafx.stage.{Modality, Stage}
import utils.KeySettings.keySettingsMap

object KeySettingsStageCreator {
  def createSettingsStage() = {
    new Stage {
      title = "settings"
      initModality(Modality.ApplicationModal)
      scene = new Scene(createSettingsPane())
    }
  }

  def createSettingsPane() =
    new VBox(keySettingsHBoxList: _*)

  def keySettingsHBoxList =
    keySettingsMap.map(makeRowKeySetting).toList

  def makeRowKeySetting(rowKeySetting: (String, List[String])) =
    new HBox(
      new Text(rowKeySetting._1),
      new TextField {
        text = rowKeySetting._2.mkString(" ")
      }
    )
}
