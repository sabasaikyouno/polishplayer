import scalafx.scene.image.ImageView
import MediaPlayerCreator.createMediaPlayer
import scalafx.geometry.Pos

object TimeThumbnailCreator {
  def createTimeThumbnailImageView() =
    new ImageView {
      fitWidth = 100
      fitHeight = 80
      translateY = -70
      alignmentInParent = Pos.BottomLeft
    }

  def createTimeThumbnailEmbedded(videoImageView: ImageView) =
    createMediaPlayer(videoImageView)
}
