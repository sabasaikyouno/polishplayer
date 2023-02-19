package utils

import java.io.File

import com.github.tototoshi.csv.{CSVReader, CSVWriter}

object CSVUtils {

  def csvReadAll(pathName: String) = {
    val file = new File(pathName)
    val reader = CSVReader.open(file)

    try
      reader.all()
    finally
      reader.close()
  }

  def csvWriteAll(pathName: String, writeList: Seq[Seq[Any]]) = {
    val keySettingsFile = new File(pathName)
    val writer = CSVWriter.open(keySettingsFile)

    try
      writer.writeAll(writeList)
    finally
      writer.close()
  }
}
