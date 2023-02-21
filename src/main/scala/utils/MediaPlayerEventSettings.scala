package utils

import scalafx.scene.control.TextField
import scalafx.scene.text.Text
import utils.CSVUtils.{csvReadAll, csvWriteAll}

object MediaPlayerEventSettings {

  def mediaPlayerEventSettingsList =
    getMediaPlayerEventSettings
      .map(list => (list.head, list(1)))

  def mediaPlayerEventSettingsWrite(settingsList: List[List[String]]) =
    csvWriteAll("src\\main\\resources\\mediaPlayerEventSettings.csv", settingsList)

  def mediaPlayerEventSettingNodeToRaw(mediaPlayerEventSettingNode: (Text, TextField)) = List(
    mediaPlayerEventSettingNode._1.text.value,
    mediaPlayerEventSettingNode._2.text.value
  )

  def findMediaPlayerEventSetting(settingName: String) =
    mediaPlayerEventSettingsList.find(_._1 == settingName)

  private def getMediaPlayerEventSettings =
    csvReadAll("src\\main\\resources\\mediaPlayerEventSettings.csv")

  private def findMediaPlayerEventSettings(eventName: String) =
    getMediaPlayerEventSettings.find(_.head == eventName)
}
