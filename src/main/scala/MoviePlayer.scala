import java.io.File

import com.github.tototoshi.csv.CSVReader
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer

object MoviePlayer {

  def play(embeddedMediaPlayer: EmbeddedMediaPlayer, filePath: String) = {

    // 動画再生
    embeddedMediaPlayer.media().play(filePath)

    // 前回どこまで再生されたかを取得し、前回の位置にスキップする
    val position = resumePlayList(filePath).find(_.head == embeddedMediaPlayer.media().info().mrl())
    if (position.isDefined) {
      embeddedMediaPlayer.controls().setPosition(position.get(1).toFloat)
    }
  }

  private def resumePlayList(resumePlayPath: String) = {
    val reader = CSVReader.open(new File(resumePlayPath))

    try
      reader.all()
    finally
      reader.close()
  }
}
