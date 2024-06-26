package utils

import event.ClosedEvent.closedEvent
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer
import utils.ResumePlayList.getResumePosition

object MoviePlayer {

  def play(embeddedMediaPlayer: EmbeddedMediaPlayer, filePath: String) = {
    // どこまで再生したかを記録している。
    closedEvent(embeddedMediaPlayer)

    // 動画再生
    embeddedMediaPlayer.media().play(filePath)

    // 前回の位置にセットする
    embeddedMediaPlayer.controls().setPosition(getResumePosition(embeddedMediaPlayer))
  }

  def startPaused(embeddedMediaPlayer: EmbeddedMediaPlayer, filePath: String) =
    embeddedMediaPlayer.media().startPaused(filePath)
}
