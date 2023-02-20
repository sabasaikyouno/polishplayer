package creator

import scalafx.scene.Scene
import scalafx.scene.control.{Button, TextField}
import scalafx.scene.layout.{HBox, VBox}
import scalafx.scene.text.Text
import scalafx.stage.{Modality, Stage}
import utils.MediaPlayerEventSettings.{mediaPlayerEventSettingNodeToRaw, mediaPlayerEventSettingsList, mediaPlayerEventSettingsWrite}

object MediaPlayerEventSettingsCreator {
  def createMediaPlayerEventSettingsStage() = {
    new Stage {
      title = "media player event settings"
      initModality(Modality.ApplicationModal)
      scene = new Scene(createMediaPlayerEventSettingsVBox)
    }
  }

  def createMediaPlayerEventSettingsVBox = {
    val mediaPlayerEventSettingsNodeList = mediaPlayerEventSettingsList.map(makeMediaPlayerEventSettingNode)
    val hboxList = mediaPlayerEventSettingsNodeList.map(makeRowHBox) :+ saveButton(mediaPlayerEventSettingsNodeList)

    new VBox(hboxList: _*)
  }

  def makeRowHBox(row: (Text, TextField)) =
    new HBox(row._1, row._2)

  def makeMediaPlayerEventSettingNode(mediaPlayerEventSettings: (String, String)) = (
    new Text(mediaPlayerEventSettings._1),
    new TextField {
      text = mediaPlayerEventSettings._2
    }
  )

  def saveButton(mediaPlayerEventSettingsNodeList: List[(Text, TextField)]) = {
    new Button {
      text = "save"
      onMouseClicked = _ => mediaPlayerEventSettingsWrite(mediaPlayerEventSettingsNodeList.map(mediaPlayerEventSettingNodeToRaw))
    }
  }
}
