package creator

import event.PinEvent.pinWrite
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

  def createPinVBox = {
    val tf = new TextField()
    val startTimeTf, endTimeTf = new TextField()

    new VBox(
      tf,
      startTimeTf,
      endTimeTf,
      saveButton(tf, startTimeTf, endTimeTf)
    )
  }

  private def saveButton(tf: TextField, startTimeTf: TextField, endTimeTf: TextField) =
    new Button {
      text = "save"
      onMouseClicked = _ => pinWrite(tf.text.value, startTimeTf.text.value.toLong, endTimeTf.text.value.toLong)
    }
}
