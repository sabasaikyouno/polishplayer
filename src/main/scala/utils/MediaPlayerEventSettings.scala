package utils

import java.io.File

import com.github.tototoshi.csv.CSVReader

object MediaPlayerEventSettings {

  def mediaPlayerEventSettingsList =
    getMediaPlayerEventSettings
      .map(list => (list.head, list(1)))

  private def getMediaPlayerEventSettings = {
    val mediaPlayerEventSettingsFile = new File("src\\main\\resources\\mediaPlayerEventSettings.csv")
    val reader = CSVReader.open(mediaPlayerEventSettingsFile)

    try
      reader.all()
    finally
      reader.close()
  }

  private def findMediaPlayerEventSettings(eventName: String) =
    getMediaPlayerEventSettings.find(_.head == eventName)
}
