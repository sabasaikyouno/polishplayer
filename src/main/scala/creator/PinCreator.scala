package creator

import scalafx.scene.Scene
import scalafx.scene.control.{Button, TextField}
import scalafx.scene.layout.VBox
import scalafx.stage.Stage

object PinCreator {
  def createPinStage =
    new Stage {
      title = "pin"
      scene = new Scene(createPinVBox)
    }

  def createPinVBox =
    new VBox(
      new TextField(),
      new TextField(),
      new Button("save")
    )
}
