import scalafx.scene.control.{Button, Label, Slider}
import TimeFmt.timeFmt
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.paint.Color
import ButtonImageView._
import javafx.application.Platform
import scalafx.scene.input.MouseEvent
import javafx.scene.input.KeyCode._
import scalafx.Includes._
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.geometry.Pos
import scalafx.scene.Scene
import scalafx.scene.layout.{HBox, VBox}
import uk.co.caprica.vlcj.player.base.{MediaPlayer, MediaPlayerEventAdapter}
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer

object SfxToolBarCreator {

  def createToolBar()(implicit stage: PrimaryStage, scene: Scene, embeddedMediaPlayer: EmbeddedMediaPlayer) = {
    val timeLabel = createTimeLabel()
    val timeSlider = createTimeSlider()
    setTimeChanged(timeLabel, timeSlider)

    val pauseButton = createPauseButton()
    setSceneEvent(pauseButton)

    createToolBarVBox(timeLabel, timeSlider, pauseButton)
  }

  private def setTimeChanged(timeLabel: Label, timeSlider: Slider)(implicit embeddedMediaPlayer: EmbeddedMediaPlayer) =
    embeddedMediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
      override def timeChanged(mediaPlayer: MediaPlayer, newTime: Long): Unit = {
        Platform.runLater(() =>
          timeLabel.text = timeFmt(newTime)
        )
        timeSlider.value = (newTime.toDouble / embeddedMediaPlayer.status().length().toDouble) * 100
      }
    })

  private def setSceneEvent(pauseButton: Button)(implicit scene: Scene, embeddedMediaPlayer: EmbeddedMediaPlayer) =
    scene.onKeyPressed = key =>
      if (key.getCode == SPACE)
        if (embeddedMediaPlayer.status().isPlaying)
          pauseButton.graphic = playButtonImageView
        else
          pauseButton.graphic = stopButtonImageView

  private def createToolBarVBox(timeLabel: Label, timeSlider: Slider, pauseButton: Button)(implicit stage: PrimaryStage, embeddedMediaPlayer: EmbeddedMediaPlayer) =
    new VBox {
      alignment = Pos.BottomCenter
      children = Seq(timeSlider, createToolBarHBox(timeLabel, pauseButton))
    }

  private def createToolBarHBox(timeLabel: Label, pauseButton: Button)(implicit stage: PrimaryStage, embeddedMediaPlayer: EmbeddedMediaPlayer) =
    new HBox(5) {
      alignment = Pos.Center
      children = Seq(
        createVolumeSlider(),
        timeLabel,
        createBackButton(),
        pauseButton,
        createForWordButton(),
        createFullScreenButton()
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
      graphic = stopButtonImageView
      style = "-fx-background-color:Transparent;-fx-background-radius:0"

      onMouseClicked = _ => {
        embeddedMediaPlayer.controls().pause()
        if (embeddedMediaPlayer.status().isPlaying)
          graphic = playButtonImageView
        else
          graphic = stopButtonImageView
      }
    }

  private def createForWordButton()(implicit embeddedMediaPlayer: EmbeddedMediaPlayer) =
    createButtonHasEvent("forward.png") { _ =>
      embeddedMediaPlayer.controls().skipTime(10000)
    }

  private def createBackButton()(implicit embeddedMediaPlayer: EmbeddedMediaPlayer) =
    createButtonHasEvent("back.png") { _ =>
      embeddedMediaPlayer.controls().skipTime(-10000)
    }

  private def createFullScreenButton()(implicit stage: PrimaryStage) =
    new Button {
      graphic = fullScreenButtonImageView
      style = "-fx-background-color:Transparent;-fx-background-radius:0"

      onMouseClicked = _ => {
        if (stage.isFullScreen) {
          stage.fullScreen = false
          graphic = fullScreenButtonImageView
        } else {
          stage.fullScreen = true
          graphic = fullScreenReleaseButtonImageView
        }
      }
    }

  private def createTimeSlider()(implicit embeddedMediaPlayer: EmbeddedMediaPlayer) =
    new Slider {
      style = "-fx-background-color:Transparent;-fx-background-radius:0"

      onMouseDragged = _ =>
        embeddedMediaPlayer.controls().setPosition((value.get() / 100).toFloat)

      onMouseClicked = _ =>
        embeddedMediaPlayer.controls().setPosition((value.get() / 100).toFloat)
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

  private def createButtonHasEvent(buttonImagePath: String)(onMouseClickedEvent: MouseEvent => Unit) =
    new Button {
      graphic = createImageView(buttonImagePath)
      style = "-fx-background-color:Transparent;-fx-background-radius:0"
      onMouseClicked = onMouseClickedEvent
    }

  private def createImageView(buttonImagePath: String) =
    new ImageView(
      new Image(getClass.getResourceAsStream(buttonImagePath))
    )

}
