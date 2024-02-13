package calculator

import scala.io.{BufferedSource, Source}
import scala.util.{Using, Try}


object Application:
  import CalculatorError.*

  private lazy val defaultSample = "input/defaults.calc"

  private lazy val title = "calculator.txt"
  private lazy val titleFallback = "\nRunning calculator ...\n\n\n"

  private def fromResourceFile (fileName: String): Either [IOError, String] =
    fromFileImpl (fileName) (Source.fromResource (_), _.mkString (""))

  private def fromFileImpl [U] (fileName: String) (getBufferFromSource: String => BufferedSource, action: BufferedSource => U) : Either [IOError, U] =
    Using (getBufferFromSource (fileName)) (action).toEither match
      case Left (error) => Left (IOError (error.getMessage, fileName))
      case Right (result) => Right (result)

  // It's not serious if we've mucked up the ascii art
  private def titleArt (dataFile: String, fallback: String): String =
    val result =
      for
        buffer <- fromResourceFile (title)
      yield
        buffer.mkString ("")

    s"${result.getOrElse (fallback)}"

  private def processDataFile (fileName: String) (process: String => Unit): Either [IOError, Unit] =
    fromFileImpl (fileName) (
      Source.fromFile,
      _.getLines
       .foreach: line =>
         process (line)
    )
  @main
  def run (inputFile: String): Unit =
    print (titleArt (title, titleFallback))
    println (s"\nRunning calculator from $inputFile.\n")
    processDataFile (inputFile): line =>
      Calculator
        .value (line)
        .fold (logError, result => println (s"$line = $result"))
