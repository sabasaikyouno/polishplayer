package creator

import scalafx.application.JFXApp3.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.image.ImageView
import scalafx.scene.layout.{Background, BackgroundFill, CornerRadii, StackPane}
import scalafx.scene.paint.Color
import scalafx.stage.StageStyle

object NodeCreator {

  def createStackPane =
    new StackPane {
      prefWidth = 800
      prefHeight = 600
      background = new Background(Array(new BackgroundFill(Color.Black, CornerRadii.Empty, Insets.Empty)))
    }

  def createVideoImageView(videoStack: StackPane) =
    new ImageView {
      preserveRatio = true
      fitWidth.bind(videoStack.widthProperty())
      fitHeight.bind(videoStack.heightProperty())
    }

  def createStage(videoStack: StackPane) =
    new PrimaryStage {
      initStyle(StageStyle.Undecorated)
      fullScreenExitHint = ""
      scene = new Scene(videoStack)
    }

}
