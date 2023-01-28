package creator

import creator.MediaPlayerCreator.createMediaPlayer
import scalafx.geometry.Pos
import scalafx.scene.image.ImageView

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
