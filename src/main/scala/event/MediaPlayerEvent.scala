package event

import javafx.application.Platform
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.control.{Button, Label, Slider}
import uk.co.caprica.vlcj.player.base.{MediaPlayer, MediaPlayerEventAdapter}
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer
import utils.ButtonImageView.{playButton, stopButton}
import utils.MediaPlayerEventSettings.findMediaPlayerEventSetting
import utils.ResumePlayList.getResumeVolume
import utils.TimeFmt.timeFmt

object MediaPlayerEvent {

  def pause()(implicit embeddedMediaPlayer: EmbeddedMediaPlayer) =
    embeddedMediaPlayer.controls().pause()

  def fastForward()(implicit embeddedMediaPlayer: EmbeddedMediaPlayer) =
    embeddedMediaPlayer.controls().skipTime(findMediaPlayerEventSetting("FastForward").get.toInt)

  def rewind()(implicit embeddedMediaPlayer: EmbeddedMediaPlayer) =
    embeddedMediaPlayer.controls().skipTime(findMediaPlayerEventSetting("Rewind").get.toInt)

  def skipRate(position: Float)(implicit embeddedMediaPlayer: EmbeddedMediaPlayer) =
    embeddedMediaPlayer.controls().setPosition(position)

  def upVolume()(implicit embeddedMediaPlayer: EmbeddedMediaPlayer) =
    embeddedMediaPlayer.audio().setVolume(embeddedMediaPlayer.audio().volume() + findMediaPlayerEventSetting("UpVolume").get.toInt)

  def downVolume()(implicit embeddedMediaPlayer: EmbeddedMediaPlayer) =
    embeddedMediaPlayer.audio().setVolume(embeddedMediaPlayer.audio().volume() + findMediaPlayerEventSetting("DownVolume").get.toInt)

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

  def setMediaPlayerReady()(implicit embeddedMediaPlayer: EmbeddedMediaPlayer) =
    embeddedMediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
      override def mediaPlayerReady(mediaPlayer: MediaPlayer): Unit = {
        embeddedMediaPlayer.audio().setVolume(getResumeVolume())
      }
    })

  def paused(pauseButton: Button)(implicit embeddedMediaPlayer: EmbeddedMediaPlayer) =
    embeddedMediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
      override def paused(mediaPlayer: MediaPlayer): Unit = {
        Platform.runLater(() =>
          pauseButton.graphic = playButton
        )
      }

      override def playing(mediaPlayer: MediaPlayer): Unit = {
        Platform.runLater(() =>
          pauseButton.graphic = stopButton
        )
      }
    })
}
