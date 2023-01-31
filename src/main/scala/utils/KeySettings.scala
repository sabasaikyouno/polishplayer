package utils

import java.io.File

import scalafx.scene.input.KeyCode
import com.github.tototoshi.csv.CSVReader

object KeySettings {

  def keySetting(keyCode: KeyCode): (String, KeyCode) =
    (findKeySettings(keyCode).fold("")(_.apply(1)), keyCode)

  private def getKeySettings = {
    val keySettingsFile = new File("src\\main\\resources\\keySettings.csv")
    val reader = CSVReader.open(keySettingsFile)

    try
      reader.all()
    finally
      reader.close()
  }

  private def findKeySettings(keyCode: KeyCode) =
    getKeySettings.find(_.head == keyCode.name)
}
