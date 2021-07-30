

import java.io.File

import javafx.application.{Application, Platform}
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.image.{Image, ImageView}
import javafx.scene.layout.{Background, BackgroundFill, BorderPane, CornerRadii, HBox, StackPane, VBox}
import javafx.scene.paint.Color
import javafx.stage.{Stage, StageStyle}
import javafx.scene.control._
import javafx.geometry.{Insets, Pos}
import javafx.scene.input.{DragEvent, KeyCode, KeyEvent, MouseEvent, TransferMode}
import javafx.scene.shape.Rectangle
import javafx.scene.text.TextAlignment
import uk.co.caprica.vlcj.javafx.videosurface.ImageViewVideoSurfaceFactory.videoSurfaceForImageView
import javax.xml.crypto.Data
import uk.co.caprica.vlcj.factory.MediaPlayerFactory
import uk.co.caprica.vlcj.media.{Media, MediaEventListener, MediaParsedStatus, MediaRef, Meta, Picture}
import uk.co.caprica.vlcj.player.base.{MediaPlayer, MediaPlayerEventAdapter, State}
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer
import uk.co.caprica.vlcj.player.embedded.fullscreen.adaptive.AdaptiveFullScreenStrategy

object Main extends App {
  Application.launch(classOf[Main], args:_*)
}

class Main extends Application {

  val mediaPlayerFactory = new MediaPlayerFactory()
  val embeddedMediaPlayer = mediaPlayerFactory.mediaPlayers().newEmbeddedMediaPlayer()
  val timeFmt = new TimeFmt
  val videoImageView = new ImageView()
  videoImageView.setPreserveRatio(true)

  embeddedMediaPlayer.videoSurface().set(videoSurfaceForImageView(videoImageView))


  override def start(primaryStage: Stage): Unit = {
    
    val videoStack = new StackPane()
    val toolBar = new VBox()
    val toolBarHBox = new HBox(5)
    val titleBar = new StackPane()
    val titleButton = new HBox()

    // ウィンドウ設定
    videoStack.setPrefSize(800,600)
    videoStack.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)))
    primaryStage.initStyle(StageStyle.UNDECORATED)

    // 動画のサイズ設定(よくわかってないけどそんな感じなはず）
    videoImageView.fitWidthProperty().bind(videoStack.widthProperty())
    videoImageView.fitHeightProperty().bind(videoStack.heightProperty())

    // ツールバー作成
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

    // 再生バー作成
    val timeSlider = new Slider()
    timeSlider.setOnMouseDragged(event => {
      embeddedMediaPlayer.controls().setPosition((timeSlider.getValue/100).toFloat)
    })
    timeSlider.setOnMouseClicked(event => {
      embeddedMediaPlayer.controls().setPosition((timeSlider.getValue/100).toFloat)
    })

    toolBarHBox.getChildren.addAll(timeLabel,backButton,pauseButton,forwardButton)
    toolBarHBox.setAlignment(Pos.CENTER)

    toolBar.setAlignment(Pos.BOTTOM_CENTER)
    toolBar.getChildren.addAll(timeSlider,toolBarHBox)


    // タイトルバー作成
    // 透明なブロックを一番上においてウィンドウを動かせるようにする
    val rect1 = new Rectangle(0,0,0,40)
    rect1.setFill(Color.TRANSPARENT)
    rect1.widthProperty().bind(primaryStage.widthProperty())

    // 終了ボタン
    val exitButtonImage = new Image(getClass.getResourceAsStream("exit.png"))
    val exitButton = new Button()
    exitButton.setGraphic(new ImageView(exitButtonImage))
    exitButton.setStyle("-fx-background-color:Black;-fx-background-radius:0")
    exitButton.setOnMouseClicked(event => { // 押されたときにアプリを終了する
      Platform.exit()
    })

    // アイコン化(最小化？言い方がよくわからない）
    val iconifiedButtonImage = new Image(getClass.getResourceAsStream("iconified.png"))
    val iconifiedButton = new Button()
    iconifiedButton.setGraphic(new ImageView(iconifiedButtonImage))
    iconifiedButton.setStyle("-fx-background-color:Black;-fx-background-radius:0")
    iconifiedButton.setOnMouseClicked(event => {
      primaryStage.setIconified(true)
      videoStack.requestFocus()
    })

    // ウィンドウの最大化、最大化を元に戻す
    val maximizedButtonImage = new Image(getClass.getResourceAsStream("maximized.png"))
    val minimizedButtonImage = new Image(getClass.getResourceAsStream("minimized.png"))
    val maximizedButton = new Button()
    maximizedButton.setGraphic(new ImageView(maximizedButtonImage))
    maximizedButton.setStyle("-fx-background-color:Black;-fx-background-radius:0")
    maximizedButton.setOnMouseClicked(event => {
      if(primaryStage.isMaximized) {
        primaryStage.setMaximized(false)
        maximizedButton.setGraphic(new ImageView(maximizedButtonImage))
      } else {
        primaryStage.setMaximized(true)
        maximizedButton.setGraphic(new ImageView(minimizedButtonImage))
      }
      videoStack.requestFocus()
    })

    // 重なって押せない要素を押せるようにする
    titleButton.setPickOnBounds(false)
    titleBar.setPickOnBounds(false)

    titleButton.setAlignment(Pos.TOP_RIGHT)
    titleButton.getChildren.addAll(maximizedButton,iconifiedButton,exitButton)

    titleBar.setAlignment(Pos.TOP_LEFT)
    titleBar.getChildren.addAll(rect1,titleButton)

    videoStack.getChildren.addAll(videoImageView,toolBar,titleBar)

    // 表示
    val scene = new Scene(videoStack)
    primaryStage.setTitle("polishplayer")

    // ドラッグ処理
    scene.setOnDragOver(new EventHandler[DragEvent] {
      override def handle(event: DragEvent): Unit = {
        if (event.getGestureSource != scene &&
          event.getDragboard.hasFiles) {
          event.acceptTransferModes(TransferMode.COPY_OR_MOVE:_*)
        }
        event.consume()
      }
    })

    //ドラッグされた動画を再生する
    scene.setOnDragDropped(new EventHandler[DragEvent] {
      override def handle(event: DragEvent): Unit = {
        val db = event.getDragboard
        if (db.hasFiles) {
          db.getFiles.toArray(Array[File]()).toSeq.foreach { f =>
            embeddedMediaPlayer.media().play(f.getAbsolutePath)
          }
        }
        event.consume()
      }
    })

    videoStack.requestFocus()
    primaryStage.setScene(scene)
    primaryStage.show()

    // イベント処理
    embeddedMediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter(){
      // 時間表示
      override def timeChanged(mediaPlayer: MediaPlayer, newTime: Long): Unit = {
        Platform.runLater(() => {
          timeLabel.setText(timeFmt.fmt(newTime))
        })
        timeSlider.setValue((newTime.toDouble/embeddedMediaPlayer.status().length().toDouble)*100)
      }
    })

    // 全画面モードに入ったときに表示するテキストを空にしてる
    primaryStage.setFullScreenExitHint("")

    // キーイベント処理
    scene.setOnKeyPressed(e => {
      e.getCode match {
        case KeyCode.SPACE => embeddedMediaPlayer.controls().pause();if (embeddedMediaPlayer.status().isPlaying){
          pauseButton.setGraphic(new ImageView(playButtonImage))
        } else {
          pauseButton.setGraphic(new ImageView(stopButtonImage))
        }
        case KeyCode.RIGHT => embeddedMediaPlayer.controls().skipTime(10000)
        case KeyCode.LEFT => embeddedMediaPlayer.controls().skipTime(-10000)
        case KeyCode.UP => embeddedMediaPlayer.audio().setVolume(embeddedMediaPlayer.audio().volume()+5)
        case KeyCode.DOWN => embeddedMediaPlayer.audio().setVolume(embeddedMediaPlayer.audio().volume()-5)
        case KeyCode.M => embeddedMediaPlayer.audio().mute()
        case KeyCode.F11 => primaryStage.setFullScreen(true)
      }
    })


    // ウィンドウ移動
    rect1.setOnMousePressed(pressEvent => {
      rect1.setOnMouseDragged(dragEvent => {
        primaryStage.setX(dragEvent.getScreenX - pressEvent.getSceneX)
        primaryStage.setY(dragEvent.getScreenY - pressEvent.getSceneY)
      })
    })

    // フォーカスさせる
    videoStack.setOnMouseReleased(event => {
      videoStack.requestFocus()
    })

  }

}

