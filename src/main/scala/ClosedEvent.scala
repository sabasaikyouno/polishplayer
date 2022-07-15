import java.io.File

import com.github.tototoshi.csv.CSVWriter
import scalafx.application.JFXApp3.PrimaryStage
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer
import ResumePlayList.getResumePlayList

object ClosedEvent {

  def setClosedStageEvent()(implicit stage: PrimaryStage, embeddedMediaPlayer: EmbeddedMediaPlayer) =
    stage.showingProperty().addListener( _ => {
      closedEvent(embeddedMediaPlayer)
    })

  def closedEvent(embeddedMediaPlayer: EmbeddedMediaPlayer) = {
    if (embeddedMediaPlayer.media().isValid) {
      val mrl = embeddedMediaPlayer.media().info().mrl()
      val position = getPosition(embeddedMediaPlayer)
      val volume = embeddedMediaPlayer.audio().volume()

      resumePlayWrite(mrl, position, volume)
    }
  }

  private def resumePlayWrite(mrl: String, position: Float, volume: Int) = {
    val resumePlayFile = new File("src\\main\\resources\\resume_play.csv")
    val resumePlayList = getResumePlayList()
    val writer = CSVWriter.open(resumePlayFile)

    try
      writer.writeAll(
        List(List(mrl, position, volume)) ::: resumePlayList.filter(_.head != mrl)
      )
    finally
      writer.close()
  }

  private def getPosition(embeddedMediaPlayer: EmbeddedMediaPlayer): Float = {
    val isEnding = embeddedMediaPlayer.status().position() > 0.95

    if (isEnding) 0 else embeddedMediaPlayer.status().position()
  }
}
