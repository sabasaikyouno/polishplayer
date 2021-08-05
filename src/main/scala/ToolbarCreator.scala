import javafx.application.Platform
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.{Button, Label, Slider}
import javafx.scene.image.{Image, ImageView}
import javafx.scene.input.KeyCode
import javafx.scene.layout.{HBox, VBox}
import javafx.scene.paint.Color
import javafx.stage.Stage
import uk.co.caprica.vlcj.player.base.{MediaPlayer, MediaPlayerEventAdapter}
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer

object ToolbarCreator {

  def create(embeddedMediaPlayer: EmbeddedMediaPlayer, primaryStage:Stage, scene: Scene): VBox ={
    val timeFmt = new TimeFmt
    val toolBar = new VBox()
    val toolBarHBox = new HBox(5)

    // ツールバー
    // 時間表示設定
    val timeLabel = new Label
    timeLabel.setText(timeFmt.fmt(0))
    timeLabel.setTextFill(Color.WHITE)

    // 再生、停止ボタン
    val playButtonImage = new Image(getClass.getResourceAsStream("play.png"))
    val stopButtonImage = new Image(getClass.getResourceAsStream("stop.png"))
    val pauseButton = new Button()
    pauseButton.setGraphic(new ImageView(stopButtonImage))
    pauseButton.setStyle("-fx-background-color:Transparent;-fx-background-radius:0")
    pauseButton.setOnMouseClicked(event => {
      embeddedMediaPlayer.controls().pause()
      if (embeddedMediaPlayer.status().isPlaying){
        pauseButton.setGraphic(new ImageView(playButtonImage))
      } else {
        pauseButton.setGraphic(new ImageView(stopButtonImage))
      }
    })

    // forward ボタン
    val forwardButtonImage = new Image(getClass.getResourceAsStream("forward.png"))
    val forwardButton = new Button()
    forwardButton.setGraphic(new ImageView(forwardButtonImage))
    forwardButton.setStyle("-fx-background-color:Transparent;-fx-background-radius:0")
    forwardButton.setOnMouseClicked(event => {
      embeddedMediaPlayer.controls().skipTime(10000)
    })

    // back ボタン
    val backButtonImage = new Image(getClass.getResourceAsStream("back.png"))
    val backButton = new Button()
    backButton.setGraphic(new ImageView(backButtonImage))
    backButton.setStyle("-fx-background-color:Transparent;-fx-background-radius:0")
    backButton.setOnMouseClicked(event => {
      embeddedMediaPlayer.controls().skipTime(-10000)
    })

    // フルスクリーンボタン
    val fullscreenReleaseButtonImage = new Image(getClass.getResourceAsStream("fullscreenRelease.png"))
    val fullscreenButtonImage = new Image(getClass.getResourceAsStream("fullscreen.png"))
    val fullscreenButton = new Button()
    fullscreenButton.setGraphic(new ImageView(fullscreenButtonImage))
    fullscreenButton.setStyle("-fx-background-color:Transparent;-fx-background-radius:0")
    fullscreenButton.setOnMouseClicked(event => {
      if (primaryStage.isFullScreen){
        primaryStage.setFullScreen(false)
        fullscreenButton.setGraphic(new ImageView(fullscreenButtonImage))
      } else {
        primaryStage.setFullScreen(true)
        fullscreenButton.setGraphic(new ImageView(fullscreenReleaseButtonImage))
      }
    })

    // 再生バー作成
    val timeSlider = new Slider()
    timeSlider.setOnMouseDragged(event => {
      embeddedMediaPlayer.controls().setPosition((timeSlider.getValue/100).toFloat)
    })
    timeSlider.setOnMouseClicked(event => {
      embeddedMediaPlayer.controls().setPosition((timeSlider.getValue/100).toFloat)
    })

    // ボリュームバー作成
    val volumeSlider = new Slider()
    volumeSlider.setPrefWidth(100)
    volumeSlider.setOnMouseDragged(event => {
      embeddedMediaPlayer.audio().setVolume(volumeSlider.getValue.toInt)
    })


    toolBarHBox.getChildren.addAll(volumeSlider,timeLabel,backButton,pauseButton,forwardButton,fullscreenButton)
    toolBarHBox.setAlignment(Pos.CENTER)

    toolBar.setAlignment(Pos.BOTTOM_CENTER)
    toolBar.getChildren.addAll(timeSlider,toolBarHBox)

    // イベント処理
    embeddedMediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter(){

      // 時間表示
      override def timeChanged(mediaPlayer: MediaPlayer, newTime: Long): Unit = {
        Platform.runLater(() => {
          timeLabel.setText(timeFmt.fmt(newTime))
        })
        timeSlider.setValue((newTime.toDouble/embeddedMediaPlayer.status().length().toDouble)*100)
      }

      // 再生、停止されたとき pause ボタンの画像を変える
      override def paused(mediaPlayer: MediaPlayer): Unit = {
        Platform.runLater(() => {
          pauseButton.setGraphic(new ImageView(playButtonImage))
        })
      }
      override def playing(mediaPlayer: MediaPlayer): Unit = {
        Platform.runLater(() => {
          pauseButton.setGraphic(new ImageView(stopButtonImage))
        })
      }

      // ボリュームバーの初期位置をセットする
      override def mediaPlayerReady(mediaPlayer: MediaPlayer): Unit = {
        volumeSlider.setValue(embeddedMediaPlayer.audio().volume())
      }

    })

    // キーイベント処理
    scene.setOnKeyPressed(e => {
      // スペースが押されたときに再生停止ボタンを変える
      if(e.getCode == KeyCode.SPACE) {
        if (embeddedMediaPlayer.status().isPlaying){
          pauseButton.setGraphic(new ImageView(playButtonImage))
        } else {
          pauseButton.setGraphic(new ImageView(stopButtonImage))
        }
      }
    })
    toolBar
  }
}
