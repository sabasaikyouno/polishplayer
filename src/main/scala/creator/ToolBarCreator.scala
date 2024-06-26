package creator

import creator.MenuCreator.createMenuStage
import creator.SettingsCreator.createSettingsStage
import event.MediaPlayerEvent.{paused, timeChanged, volumeChanged}
import scalafx.Includes._
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.geometry.Pos
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label, Slider}
import scalafx.scene.image.ImageView
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.{HBox, VBox}
import scalafx.scene.paint.Color
import scalafx.stage.Stage
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer
import utils.ButtonImageView._
import utils.TimeFmt.timeFmt

object ToolBarCreator {

  def createToolBar(timeThumbnail: ImageView, timeThumbnailEmbedded: EmbeddedMediaPlayer)(implicit stage: PrimaryStage, scene: Scene, embeddedMediaPlayer: EmbeddedMediaPlayer) = {
    val timeLabel = createTimeLabel()
    val timeSlider = createTimeSlider(timeThumbnail, timeThumbnailEmbedded)
    setTimeChanged(timeLabel, timeSlider)

    val pauseButton = createPauseButton()
    setPausedEvent(pauseButton)

    val volumeSlider = createVolumeSlider()
    setVolumeChanged(volumeSlider)

    createToolBarVBox(timeLabel, timeSlider, pauseButton, volumeSlider)
  }

  private def setTimeChanged(timeLabel: Label, timeSlider: Slider)(implicit embeddedMediaPlayer: EmbeddedMediaPlayer) =
    timeChanged(timeLabel, timeSlider)

  private def setVolumeChanged(volumeSlider: Slider)(implicit embeddedMediaPlayer: EmbeddedMediaPlayer) =
    volumeChanged(volumeSlider)

  private def setPausedEvent(pauseButton: Button)(implicit scene: Scene, embeddedMediaPlayer: EmbeddedMediaPlayer) =
    paused(pauseButton)

  private def createToolBarVBox(timeLabel: Label, timeSlider: Slider, pauseButton: Button, volumeSlider: Slider)(implicit stage: PrimaryStage, embeddedMediaPlayer: EmbeddedMediaPlayer) =
    new VBox {
      alignment = Pos.BottomCenter
      children = Seq(timeSlider, createToolBarHBox(timeLabel, pauseButton, volumeSlider))
    }

  private def createToolBarHBox(timeLabel: Label, pauseButton: Button, volumeSlider: Slider)(implicit stage: PrimaryStage, embeddedMediaPlayer: EmbeddedMediaPlayer) =
    new HBox(5) {
      alignment = Pos.Center
      children = Seq(
        volumeSlider,
        timeLabel,
        createBackButton(),
        pauseButton,
        createForWordButton(),
        createFullScreenButton(),
        createSettingsButton(),
        createMediaInfoButton
      )
    }

  private def createTimeLabel() =
    new Label {
      text = timeFmt(0)
      textFill = Color.White
      style = "-fx-background-color:Transparent;-fx-background-radius:0"
    }

  private def createPauseButton()(implicit embeddedMediaPlayer: EmbeddedMediaPlayer) =
    new Button {
      graphic = stopButton
      style = "-fx-background-color:Transparent;-fx-background-radius:0"

      onMouseClicked = _ => embeddedMediaPlayer.controls().pause()
    }

  private def createForWordButton()(implicit embeddedMediaPlayer: EmbeddedMediaPlayer) =
    createButtonHasEvent(forwardButton) { _ =>
      embeddedMediaPlayer.controls().skipTime(10000)
    }

  private def createBackButton()(implicit embeddedMediaPlayer: EmbeddedMediaPlayer) =
    createButtonHasEvent(backButton) { _ =>
      embeddedMediaPlayer.controls().skipTime(-10000)
    }

  private def createFullScreenButton()(implicit stage: PrimaryStage) =
    new Button {
      graphic = fullScreenButton
      style = "-fx-background-color:Transparent;-fx-background-radius:0"

      onMouseClicked = _ => {
        if (stage.isFullScreen) {
          stage.fullScreen = false
          graphic = fullScreenButton
        } else {
          stage.fullScreen = true
          graphic = fullScreenReleaseButton
        }
      }
    }

  private def createSettingsButton(settingsStage: Stage = createSettingsStage) =
    createButtonHasEvent(settingsButton) { _ =>
      settingsStage.show()
    }

  private def createMediaInfoButton(implicit embeddedMediaPlayer: EmbeddedMediaPlayer) = {
    val menuStage = createMenuStage(embeddedMediaPlayer)
    createButtonHasEvent(menuButton) { _ =>
      menuStage.show()
    }
  }

  private def createTimeSlider(timeThumbnail: ImageView, timeThumbnailEmbedded: EmbeddedMediaPlayer)(implicit embeddedMediaPlayer: EmbeddedMediaPlayer) =
    new Slider {
      style = "-fx-background-color:Transparent;-fx-background-radius:0"

      onMouseDragged = _ =>
        embeddedMediaPlayer.controls().setPosition((value.get() / 100).toFloat)

      onMouseClicked = _ =>
        embeddedMediaPlayer.controls().setPosition((value.get() / 100).toFloat)

      onMouseMoved = mouseEvent => {
        timeThumbnail.translateX = mouseEvent.getX.min(width.value - timeThumbnail.fitWidth.value)
        timeThumbnailEmbedded.controls().setPosition((mouseEvent.getX / width.value).toFloat)
      }

      onMouseExited = _ =>
        timeThumbnail.opacity = 0

      onMouseEntered = _ =>
        timeThumbnail.opacity = 1
    }

  private def createVolumeSlider()(implicit embeddedMediaPlayer: EmbeddedMediaPlayer) =
    new Slider {
      prefWidth = 100
      value = embeddedMediaPlayer.audio().volume()
      style = "-fx-background-color:Transparent;-fx-background-radius:0"

      onMouseDragged = _ =>
        embeddedMediaPlayer.audio().setVolume(value.get().toInt)

      onMouseClicked = _ =>
        embeddedMediaPlayer.audio().setVolume(value.get().toInt)
    }

  private def createButtonHasEvent(buttonImageView: ImageView)(onMouseClickedEvent: MouseEvent => Unit) =
    new Button {
      graphic = buttonImageView
      style = "-fx-background-color:Transparent;-fx-background-radius:0"
      onMouseClicked = onMouseClickedEvent
    }
}
