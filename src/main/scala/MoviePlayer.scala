import java.io.File

import com.github.tototoshi.csv.CSVReader
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer
import ClosedEvent.closedEvent

object MoviePlayer {

  def play(embeddedMediaPlayer: EmbeddedMediaPlayer, filePath: String) = {
    // どこまで再生したかを記録している。
    closedEvent(embeddedMediaPlayer)

    // 動画再生
    embeddedMediaPlayer.media().play(filePath)

    // 前回どこまで再生されたかを取得し、前回の位置にスキップする
    val position = resumePlayList().find(_.head == embeddedMediaPlayer.media().info().mrl())
    if (position.isDefined) {
      embeddedMediaPlayer.controls().setPosition(position.get(1).toFloat)
    }
  }

  private def resumePlayList() = {
    val resumePlayFile = new File("src\\main\\scala\\resume_play.csv")
    val reader = CSVReader.open(resumePlayFile)

    try
      reader.all()
    finally
      reader.close()
  }
}
