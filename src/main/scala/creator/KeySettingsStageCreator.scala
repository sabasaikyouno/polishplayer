package creator

import scalafx.scene.Scene
import scalafx.scene.control.{Button, TextField}
import scalafx.scene.layout.{HBox, VBox}
import scalafx.scene.text.Text
import scalafx.stage.{Modality, Stage}
import utils.KeySettings.{keySettingNodeToRaw, keySettingsList, keySettingsWrite}

object KeySettingsStageCreator {
  def createSettingsStage() = {
    new Stage {
      title = "settings"
      initModality(Modality.ApplicationModal)
      scene = new Scene(createKeySettingsVBox)
    }
  }

  def createKeySettingsVBox = {
    val keySettingNodeMap = keySettingsList.map(makeKeySettingNode)
    val hboxList = keySettingNodeMap.map(makeRowHBox) :+ saveButton(keySettingNodeMap)

    new VBox(hboxList: _*)
  }

  def makeRowHBox(row: (Text, TextField)) =
    new HBox(
      row._1,
      row._2
    )

  def makeKeySettingNode(keySetting: (String, List[String])) =
    (
      new Text(keySetting._1),
      new TextField {
        text = keySetting._2.mkString(" ")
        onKeyReleased = key => text = s"${text.value} ${key.getCode.getName}"
        editable = false
      }
    )

  def saveButton(keySettingsNodeMap: List[(Text, TextField)]) = {
    new Button {
      text = "save"
      onMouseClicked = _ => keySettingsWrite(keySettingsNodeMap.map(keySettingNodeToRaw))
    }
  }
}
