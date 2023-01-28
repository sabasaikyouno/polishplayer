package utils

import java.io.File

import com.github.tototoshi.csv.CSVReader
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer

object ResumePlayList {
  def getResumePlayList() = {
    val resumePlayFile = new File("src\\main\\resources\\resume_play.csv")
    val reader = CSVReader.open(resumePlayFile)

    try
      reader.all()
    finally
      reader.close()
  }

  def getResumePosition(implicit embeddedMediaPlayer: EmbeddedMediaPlayer) =
    getResumeRow().fold[Float](0)(_.apply(1).toFloat)

  def getResumeVolume()(implicit embeddedMediaPlayer: EmbeddedMediaPlayer) =
    getResumeRow().fold(0)(_.apply(2).toInt)

  private def getResumeRow()(implicit embeddedMediaPlayer: EmbeddedMediaPlayer) =
    getResumePlayList()
      .find(_.head == embeddedMediaPlayer.media().info().mrl())
}
