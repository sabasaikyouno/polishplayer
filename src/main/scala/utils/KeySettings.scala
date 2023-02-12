package utils

import java.io.File

import scalafx.scene.input.KeyCode
import com.github.tototoshi.csv.{CSVReader, CSVWriter}
import scalafx.scene.control.TextField
import scalafx.scene.text.Text

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

  def keySettingsWrite(settingsList: List[(String, List[String])]) = {
    val keySettingsFile = new File("src\\main\\resources\\keySettings.csv")
    val writer = CSVWriter.open(keySettingsFile)

    try
      writer.writeAll(settingsListPair(settingsList))
    finally
      writer.close()
  }

  def keySettingNodeToRaw(keySettingsNode: (Text, TextField)) = (
    keySettingsNode._1.text.value,
    keySettingsNode._2.text.value.split(" ").toList
  )

  def settingsListPair(settingsList: List[(String, List[String])]) =
    settingsList.flatMap { case (event, keyList) =>
      keyList.map(List(_, event))
    }

  private def getKeySettings = {
    val keySettingsFile = new File("src\\main\\resources\\keySettings.csv")
    val reader = CSVReader.open(keySettingsFile)

    try
      reader.all()
    finally
      reader.close()
  }

  private def helpFindKeySettings(keyCode: KeyCode) =
    getKeySettings.find(_.head == keyCode.name)
}
