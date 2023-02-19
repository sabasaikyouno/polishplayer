package utils

import utils.CSVUtils.csvReadAll

object MediaPlayerEventSettings {

  def mediaPlayerEventSettingsList =
    getMediaPlayerEventSettings
      .map(list => (list.head, list(1)))

  private def getMediaPlayerEventSettings =
    csvReadAll("src\\main\\resources\\mediaPlayerEventSettings.csv")

  private def findMediaPlayerEventSettings(eventName: String) =
    getMediaPlayerEventSettings.find(_.head == eventName)
}
