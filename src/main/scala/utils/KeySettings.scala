package utils

import java.io.File

import scalafx.scene.input.KeyCode
import com.github.tototoshi.csv.{CSVReader, CSVWriter}

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

  private def settingsMapToList(settingsMap: Map[String, List[String]]) = {
    settingsMap.map { case (event, keyList) =>
      keyList.flatMap(List(_, event))
    }.toList
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
