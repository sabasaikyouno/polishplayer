package event

import utils.CSVUtils.csvReadAll

object PinEvent {
  def pinWrite(text: String, startTime: Long, endTime: Long) = {

  }

  def getPins =
    csvReadAll("src\\main\\resources\\pin.csv")
}
