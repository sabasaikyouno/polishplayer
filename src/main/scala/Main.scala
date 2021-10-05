import java.io.File

import com.github.tototoshi.csv._
import javafx.application.Application
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.image.ImageView
import javafx.scene.input.{DragEvent, KeyCode, MouseButton, TransferMode}
import javafx.scene.layout.{Background, BackgroundFill, CornerRadii, StackPane}
import javafx.scene.paint.Color
import javafx.stage.{Stage, StageStyle}
import uk.co.caprica.vlcj.factory.MediaPlayerFactory
import uk.co.caprica.vlcj.javafx.videosurface.ImageViewVideoSurfaceFactory.videoSurfaceForImageView


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

    // ウィンドウ設定
    videoStack.setPrefSize(800,600)
    videoStack.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)))
    primaryStage.initStyle(StageStyle.UNDECORATED)

    // 動画のサイズ設定(よくわかってないけどそんな感じなはず）
    videoImageView.fitWidthProperty().bind(videoStack.widthProperty())
    videoImageView.fitHeightProperty().bind(videoStack.heightProperty())

    // 表示
    val scene = new Scene(videoStack)
    primaryStage.setTitle("polishplayer")


    // ツールバー、タイトルバーの作成
    val toolBar = ToolbarCreator.create(embeddedMediaPlayer, primaryStage, scene)
    val titleBar = TitlebarCreator.create(primaryStage, videoStack)
    videoStack.getChildren.addAll(videoImageView,toolBar,titleBar)

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
            MoviePlayer.play(embeddedMediaPlayer,f.getAbsolutePath)
          }
        }
        event.consume()
      }
    })

    videoStack.requestFocus()
    primaryStage.setScene(scene)
    primaryStage.show()

    // 全画面モードに入ったときに表示されるテキストを空にしてる
    primaryStage.setFullScreenExitHint("")

    // キーイベント処理
    scene.setOnKeyPressed(e => {
      e.getCode match {
        case KeyCode.SPACE => embeddedMediaPlayer.controls().pause()
        case KeyCode.RIGHT => embeddedMediaPlayer.controls().skipTime(10000)
        case KeyCode.LEFT => embeddedMediaPlayer.controls().skipTime(-10000)
        case KeyCode.UP => embeddedMediaPlayer.audio().setVolume(embeddedMediaPlayer.audio().volume()+5)
        case KeyCode.DOWN => embeddedMediaPlayer.audio().setVolume(embeddedMediaPlayer.audio().volume()-5)
        case KeyCode.M => embeddedMediaPlayer.audio().mute()
        case KeyCode.F11 => primaryStage.setFullScreen(true)
      }
    })

    // 画面クリックイベント
    videoStack.setOnMouseReleased(event => {
      event.getButton match {
        case MouseButton.PRIMARY => videoStack.requestFocus() // 動画にフォーカスさせる
        case MouseButton.SECONDARY =>
          if(toolBar.getOpacity == 0) toolBar.setOpacity(1) else toolBar.setOpacity(0) // 右クリックしたらツールバーを消す(透明にする)、再表示
      }
    })

    // マウスを動かすとツールバーを表示させる
    videoStack.setOnMouseMoved(event => {
      toolBar.setOpacity(1)
    })

    // アプリを終了したときのイベント
    primaryStage.showingProperty().addListener(event => {

      // どこまで再生したかを csv に保存している
      if (embeddedMediaPlayer.media().isValid){

        val video_mrl = embeddedMediaPlayer.media().info().mrl()
        val video_position = embeddedMediaPlayer.status().position()

        // ファイルパス
        val f = new File("src\\main\\scala\\resume_play.csv")

        // ファイルの読み込み
        val reader = CSVReader.open(f)
        val resume_play_csv_List = reader.all()
        reader.close()

        // ファイルの書き込み
        val writer = CSVWriter.open(f)
        writer.writeAll(resume_play_csv_List.filter(l =>
          l.head != video_mrl) ::: List(List(video_mrl, video_position))
        )
        writer.close()
        
      }

    })

  }
}

