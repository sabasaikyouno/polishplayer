import javafx.application.Platform
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.image.{Image, ImageView}
import javafx.scene.layout.{HBox, StackPane}
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.stage.{Screen, Stage}

object TitlebarCreator {

  def create(primaryStage: Stage, videoStack: StackPane): StackPane = {
    val titleBar = new StackPane()
    val titleButton = new HBox()

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
    val primaryScreenBounds = Screen.getPrimary.getVisualBounds
    maximizedButton.setGraphic(new ImageView(maximizedButtonImage))
    maximizedButton.setStyle("-fx-background-color:Black;-fx-background-radius:0")
    maximizedButton.setOnMouseClicked(event => {
      if(primaryStage.isMaximized) {
        primaryStage.setMaximized(false)
        maximizedButton.setGraphic(new ImageView(maximizedButtonImage))
      } else {
        primaryStage.setMaximized(true)
        primaryStage.setX(0)
        primaryStage.setY(0)
        primaryStage.setWidth(primaryScreenBounds.getWidth)
        primaryStage.setHeight(primaryScreenBounds.getHeight)
        maximizedButton.setGraphic(new ImageView(minimizedButtonImage))
      }
      videoStack.requestFocus()
    })

    // マウスがある時以外タイトルバーを消す(透明になっている)
    // マウスがタイトルバーに入った時タイトルバーを表示
    titleBar.setOnMouseEntered(event => {
      titleBar.setOpacity(1)
    })
    // マウスがタイトルバーから離れたときタイトルバー非表示
    titleBar.setOnMouseExited(event => {
      titleBar.setOpacity(0)
    })

    // 重なって押せない要素を押せるようにする
    titleButton.setPickOnBounds(false)
    titleBar.setPickOnBounds(false)

    titleButton.setAlignment(Pos.TOP_RIGHT)
    titleButton.getChildren.addAll(maximizedButton,iconifiedButton,exitButton)

    titleBar.setAlignment(Pos.TOP_LEFT)
    titleBar.getChildren.addAll(rect1,titleButton)

    // ウィンドウ移動
    rect1.setOnMousePressed(pressEvent => {
      rect1.setOnMouseDragged(dragEvent => {
        primaryStage.setX(dragEvent.getScreenX - pressEvent.getSceneX)
        primaryStage.setY(dragEvent.getScreenY - pressEvent.getSceneY)
      })
    })

    titleBar
  }
}
