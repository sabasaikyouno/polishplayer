import scalafx.scene.image.ImageView
import uk.co.caprica.vlcj.factory.MediaPlayerFactory
import uk.co.caprica.vlcj.javafx.videosurface.ImageViewVideoSurfaceFactory.videoSurfaceForImageView

object MediaPlayerCreator {

  def createMediaPlayer(videoImageView: ImageView) = {
    val mediaPlayerFactory = new MediaPlayerFactory()
    val embeddedMediaPlayer = mediaPlayerFactory.mediaPlayers().newEmbeddedMediaPlayer()
    embeddedMediaPlayer.videoSurface().set(videoSurfaceForImageView(videoImageView))

    embeddedMediaPlayer
  }
}
