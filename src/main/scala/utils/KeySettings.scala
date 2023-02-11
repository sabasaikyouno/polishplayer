package utils

import java.io.File

import scalafx.scene.input.KeyCode
import com.github.tototoshi.csv.{CSVReader, CSVWriter}
import scalafx.scene.control.TextField
import scalafx.scene.text.Text

object KeySettings {

  def findKeySetting(keyCode: KeyCode): (String, KeyCode) =
    (helpFindKeySettings(keyCode).fold("")(_.apply(1)), keyCode)

  def keySettingsMap =
    getKeySettings
      .groupBy(_(1))
      .mapValues(
        _.map(_.head)
      )

  def keySettingsWrite(settingsMap: Map[String, List[String]]) = {
    val keySettingsFile = new File("src\\main\\resources\\keySettings.csv")
    val writer = CSVWriter.open(keySettingsFile)

    try
      writer.writeAll(settingsMapToList(settingsMap))
    finally
      writer.close()
  }

  def keySettingNodeToRaw(keySettingsNode: (Text, TextField)) = (
    keySettingsNode._1.text.value,
    keySettingsNode._2.text.value.split(" ").toList
  )

  def settingsMapToList(settingsMap: Map[String, List[String]]) =
    settingsMap.flatMap { case (event, keyList) =>
      keyList.map(List(_, event))
    }.toList

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
