import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer
import ClosedEvent.closedEvent
import ResumePlayList.getResumePosition

object MoviePlayer {

  def play(embeddedMediaPlayer: EmbeddedMediaPlayer, filePath: String) = {
    // どこまで再生したかを記録している。
    closedEvent(embeddedMediaPlayer)

    // 動画再生
    embeddedMediaPlayer.media().play(filePath)

    // 前回の位置にセットする
    embeddedMediaPlayer.controls().setPosition(getResumePosition(embeddedMediaPlayer))
  }
}
