import java.io.File

import com.github.tototoshi.csv.CSVReader
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer

object MoviePlayer {

  def play(embeddedMediaPlayer: EmbeddedMediaPlayer, filePath: String): Unit ={

    val f = new File("src\\main\\scala\\resume_play.csv")

    // ファイルの読み込み
    val reader = CSVReader.open(f)
    val resume_play_csv_List = reader.all()
    reader.close()

    // 動画再生
    embeddedMediaPlayer.media().play(filePath)

    // 前回どこまで再生されたかを取得し、前回の位置にスキップする
    val position = resume_play_csv_List.find(x => x.head == embeddedMediaPlayer.media().info().mrl())
    if (position.isDefined) {
      embeddedMediaPlayer.controls().setPosition(position.get(1).toFloat)
    }

  }
}
