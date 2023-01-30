package creator

import scalafx.Includes._
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.application.Platform
import scalafx.geometry.Pos
import scalafx.scene.control.Button
import scalafx.scene.image.ImageView
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.{HBox, StackPane}
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle
import scalafx.stage.Screen
import utils.ButtonImageView.{exitButton, iconifiedButton, maximizedButton, minimizedButton}

object SfxTitleBarCreator {

  def createTitleBar()(implicit stage: PrimaryStage, videoStack: StackPane) =
    new StackPane {
      opacity = 0
      onMouseEntered = _ =>
        opacity = 1
      onMouseExited = _ =>
        opacity = 0
      pickOnBounds = false
      alignment = Pos.TopLeft
      children = Seq(createTransparentRect(), createTitleBox())
    }

  private def createTitleBox()(implicit stage: PrimaryStage, videoStack: StackPane) =
    new HBox {
      pickOnBounds = false
      alignment = Pos.TopRight
      children = Seq(createIconifiedButton(), createMaximizedButton(), createExitButton())
    }

  // 透明なブロックを一番上においてウィンドウを動かせるようにする
  private def createTransparentRect()(implicit stage: PrimaryStage) =
    new Rectangle {
      height = 40
      fill = Color.Transparent
      width.bind(stage.width)

      onMousePressed = pressEvent =>
        onMouseDragged = dragEvent => {
          stage.x = dragEvent.getScreenX - pressEvent.getSceneX
          stage.y = dragEvent.getScreenY - pressEvent.getSceneY
        }
    }

  // 終了ボタン
  private def createExitButton() =
    createButtonHasEvent(exitButton) { _ =>
      Platform.exit()
    }

  // アイコン化(最小化？言い方がよくわからない）
  private def createIconifiedButton()(implicit stage: PrimaryStage, videoStack: StackPane) =
    createButtonHasEvent(iconifiedButton) { _ =>
      stage.setIconified(true)
      videoStack.requestFocus()
    }

  // ウィンドウの最大化、最大化を元に戻す
  private def createMaximizedButton()(implicit stage: PrimaryStage, videoStack: StackPane) = {
    val primaryScreenBounds = Screen.primary.visualBounds
    val maximizedButtonImage = maximizedButton
    val minimizedButtonImage = minimizedButton

    new Button {
      graphic = maximizedButtonImage
      style = "-fx-background-color:Black;-fx-background-radius:0"

      onMouseClicked = _ => {
        if (stage.isMaximized) {
          stage.maximized = false
          graphic = maximizedButtonImage
        } else {
          stage.maximized = true
          stage.x = 0
          stage.y = 0
          stage.width = primaryScreenBounds.width
          stage.height = primaryScreenBounds.height
          graphic = minimizedButtonImage
        }
        videoStack.requestFocus()
      }
    }
  }

  private def createButtonHasEvent(buttonImageView: ImageView)(onMouseClickedEvent: MouseEvent => Unit) =
    new Button {
      graphic = buttonImageView
      style = "-fx-background-color:Black;-fx-background-radius:0"
      onMouseClicked = onMouseClickedEvent
    }
}
