package utils

import scalafx.scene.input.KeyCode
import scalafx.scene.control.TextField
import scalafx.scene.text.Text
import utils.CSVUtils.{csvReadAll, csvWriteAll}

import scala.math.Ordering.Implicits.seqDerivedOrdering

object KeySettings {

  def findKeySetting(keyCode: KeyCode): (String, KeyCode) =
    (helpFindKeySettings(keyCode).fold("")(_.apply(1)), keyCode)

  def keySettingsList =
    getKeySettings
      .groupBy(_(1))
      .mapValues(
        _.map(_.head)
      ).toList.sorted

  def keySettingsWrite(settingsList: List[(String, List[String])]) =
    csvWriteAll("src\\main\\resources\\keySettings.csv", settingsListPair(settingsList))

  def keySettingNodeToRaw(keySettingsNode: (Text, TextField)) = (
    keySettingsNode._1.text.value,
    keySettingsNode._2.text.value.split(" ").toList
  )

  def settingsListPair(settingsList: List[(String, List[String])]) =
    settingsList.flatMap { case (event, keyList) =>
      keyList.map(List(_, event))
    }

  def removeOrAppendKeyText(text: String, key: String) =
    if (text.contains(key)) {
      text.split(" ").diff(List(key)).mkString(" ")
    } else {
      s"$text $key"
    }

  private def getKeySettings =
    csvReadAll("src\\main\\resources\\keySettings.csv")

  private def helpFindKeySettings(keyCode: KeyCode) =
    getKeySettings.find(_.head == keyCode.name)
}
