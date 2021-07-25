

import java.io.File

import javafx.application.{Application, Platform}
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.image.ImageView
import javafx.scene.layout.{Background, BackgroundFill, BorderPane, CornerRadii, HBox, StackPane}
import javafx.scene.paint.Color
import javafx.stage.Stage
import javafx.scene.control._
import javafx.geometry.{Insets, Pos}
import javafx.scene.input.{DragEvent, KeyCode, KeyEvent, TransferMode}
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
    val toolBar = new StackPane()

    // ウィンドウ設定
    videoStack.setPrefSize(800,600)
    videoStack.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)))

    // 動画のサイズ設定(よくわかってないけどそんな感じなはず）
    videoImageView.fitWidthProperty().bind(videoStack.widthProperty())
    videoImageView.fitHeightProperty().bind(videoStack.heightProperty())


    // 時間表示設定
    val timeLabel = new Label
    timeLabel.setText(timeFmt.fmt(0))
    timeLabel.setTextFill(Color.WHITE)
    toolBar.getChildren.addAll(timeLabel)
    toolBar.setAlignment(Pos.BOTTOM_CENTER)
    videoStack.getChildren.addAll(videoImageView,toolBar)

    // 表示
    val scene = new Scene(videoStack)
    primaryStage.setTitle("vlcj JavaFx")

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
      }
    })

    primaryStage.setScene(scene)
    primaryStage.show()

    // イベント処理
    embeddedMediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter(){
      override def timeChanged(mediaPlayer: MediaPlayer, newTime: Long): Unit = {
        Platform.runLater(() => {
          timeLabel.setText(timeFmt.fmt(newTime))
        })
      }
    })

    // 全画面モードに入ったときに表示するテキストを空にしてる
    primaryStage.setFullScreenExitHint("")

    // キーイベント処理
    scene.setOnKeyPressed(e => {
      e.getCode match {
        case KeyCode.SPACE => embeddedMediaPlayer.controls().pause()
        case KeyCode.RIGHT => embeddedMediaPlayer.controls().skipTime(10000)
        case KeyCode.LEFT => embeddedMediaPlayer.controls().skipTime(-10000)
        case KeyCode.F11 => primaryStage.setFullScreen(true)
      }
    })

  }

}

