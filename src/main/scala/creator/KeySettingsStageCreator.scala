package creator

import scalafx.scene.Scene
import scalafx.scene.control.{Button, TextField}
import scalafx.scene.layout.{HBox, VBox}
import scalafx.scene.text.Text
import scalafx.stage.{Modality, Stage}
import utils.KeySettings.keySettingsMap

object KeySettingsStageCreator {
  def createSettingsStage() = {
    new Stage {
      title = "settings"
      initModality(Modality.ApplicationModal)
      scene = new Scene()
    }
  }

  def createSettingsVBox(rowNodeList: List[HBox]) =
    new VBox(rowNodeList: _*)

  def makeRowNode(row: (Text, TextField)) =
    new HBox(
      row._1,
      row._2
    )

  def makeRow(keySetting: (String, List[String])) =
    (
      new Text(keySetting._1),
      new TextField {
        text = keySetting._2.mkString(" ")
      }
    )

  def saveButton(rowList: Map[Text, TextField]) = {
    new Button()
  }

  def m: Unit = {
    val keySettings = keySettingsMap
    val rowList = keySettings.map(makeRow)
    val hboxList = rowList.map(makeRowNode).toList
    val vbox = createSettingsVBox(hboxList)
  }
}
