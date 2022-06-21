import scalafx.application.JFXApp3.PrimaryStage
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
}
