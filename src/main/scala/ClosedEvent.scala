import java.io.File

import com.github.tototoshi.csv.{CSVReader, CSVWriter}
import scalafx.application.JFXApp3.PrimaryStage
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer

object ClosedEvent {

  def setClosedStageEvent()(implicit stage: PrimaryStage, embeddedMediaPlayer: EmbeddedMediaPlayer) =
    stage.showingProperty().addListener( _ => {
      closedEvent(embeddedMediaPlayer)
    })

  def closedEvent(embeddedMediaPlayer: EmbeddedMediaPlayer) = {
    if (embeddedMediaPlayer.media().isValid) {
      val videoMrl = embeddedMediaPlayer.media().info().mrl()
      val videoPosition = embeddedMediaPlayer.status().position()

      resumePlayWrite(videoMrl, videoPosition)
    }
  }

  private def resumePlayWrite(videoMrl: String, videoPosition: Float) = {
    val resumePlayFile = new File("src\\main\\scala\\resume_play.csv")
    val resumePlayList = getResumePlayList(resumePlayFile)
    val writer = CSVWriter.open(resumePlayFile)

    try
      writer.writeAll(
        List(List(videoMrl, videoPosition)) ::: resumePlayList.filter(_.head != videoMrl)
      )
    finally
      writer.close()
  }

  private def getResumePlayList(resumePlayFile: File) = {
    val reader = CSVReader.open(resumePlayFile)

    try
      reader.all()
    finally
      reader.close()
  }

}
