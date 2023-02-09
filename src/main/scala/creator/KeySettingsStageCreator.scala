package creator

import scalafx.scene.Scene
import scalafx.scene.control.{Button, TextField}
import scalafx.scene.layout.{HBox, VBox}
import scalafx.scene.text.Text
import scalafx.stage.{Modality, Stage}
import utils.KeySettings.keySettingsMap

object KeySettingsStageCreator {
  def createSettingsStage() = {
    val keySettingNode = keySettingsMap.map(makeKeySettingNode)

    new Stage {
      title = "settings"
      initModality(Modality.ApplicationModal)
      scene = new Scene(createKeySettingsVBox(keySettingNode))
    }
  }

  def createKeySettingsVBox(keySettingNodeMap: Map[Text, TextField]) = {
    val hboxList = keySettingNodeMap.map(makeRowHBox).toList :+ saveButton(keySettingNodeMap)

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
      }
    )

  def saveButton(keySettingNodeMap: Map[Text, TextField]) = {
    new Button {
      text = "save"
      onMouseClicked = _ =>
        println(keySettingNodeMap.head._2.text)
    }
  }
}
