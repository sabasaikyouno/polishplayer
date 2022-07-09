import TimeFmt.timeFmt
import javafx.application.Platform
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.control.{Label, Slider}
import uk.co.caprica.vlcj.media.MediaRef
import uk.co.caprica.vlcj.player.base.{MediaPlayer, MediaPlayerEventAdapter}
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer

object MediaPlayerEvent {

  def pause()(implicit embeddedMediaPlayer: EmbeddedMediaPlayer) =
    embeddedMediaPlayer.controls().pause()

  def fastForward()(implicit embeddedMediaPlayer: EmbeddedMediaPlayer) =
    embeddedMediaPlayer.controls().skipTime(1000)

  def rewind()(implicit embeddedMediaPlayer: EmbeddedMediaPlayer) =
    embeddedMediaPlayer.controls().skipTime(-1000)

  def upVolume()(implicit embeddedMediaPlayer: EmbeddedMediaPlayer) =
    embeddedMediaPlayer.audio().setVolume(embeddedMediaPlayer.audio().volume() + 5)

  def downVolume()(implicit embeddedMediaPlayer: EmbeddedMediaPlayer) =
    embeddedMediaPlayer.audio().setVolume(embeddedMediaPlayer.audio().volume() - 5)

  def muteVolume()(implicit embeddedMediaPlayer: EmbeddedMediaPlayer) =
    embeddedMediaPlayer.audio().mute()

  def fullScreen()(implicit stage: PrimaryStage) =
    stage.fullScreen = true

  def timeChanged(timeLabel: Label, timeSlider: Slider)(implicit embeddedMediaPlayer: EmbeddedMediaPlayer) =
    embeddedMediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
      override def timeChanged(mediaPlayer: MediaPlayer, newTime: Long): Unit = {
        Platform.runLater(() =>
          timeLabel.text = timeFmt(newTime)
        )
        timeSlider.value = (newTime.toDouble / embeddedMediaPlayer.status().length().toDouble) * 100
      }
    })

  def volumeChanged(volumeSlider: Slider)(implicit embeddedMediaPlayer: EmbeddedMediaPlayer) =
    embeddedMediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
      override def volumeChanged(mediaPlayer: MediaPlayer, volume: Float): Unit = {
        volumeSlider.value = volume * 100
      }
    })
}
