package calculator

import scala.io.{BufferedSource, Source}
import scala.util.Try


object Application:
  import CalculatorError.*

  private lazy val defaultSample = "input/defaults.calc"

  private lazy val title = "calculator.txt"
  private lazy val titleFallback = "\nRunning calculator ...\n\n\n"

  private def fromResourceFile (fileName: String): Either [IOError, BufferedSource] =
    fromFileImpl (Source.fromResource (_)) (fileName)

  private def fromDataFile (fileName: String): Either [IOError, BufferedSource] =
    fromFileImpl (Source.fromFile) (fileName)

  private def fromFileImpl (getBufferFromSource: String => BufferedSource) (fileName: String): Either [IOError, BufferedSource] =
    Try {
      getBufferFromSource (fileName)
    }.toEither match
      case Left (error) => Left (IOError (error.getMessage, fileName))
      case Right (result) => Right (result)

  // It's not serious if we've mucked up the ascii art
  private def titleArt (dataFile: String, fallback: String): String =
    val result =
      for
        buffer <- fromResourceFile (title)
      yield
        buffer.getLines.fold (""): (agg, rhs) =>
            s"$agg$rhs\n"

    s"${result.getOrElse (fallback)}"

  @main
  def run (inputFile: String): Unit =
    val inputAttempt = fromDataFile (inputFile)
    
    System.out.print (titleArt (title, titleFallback))
    
    val dataAttempt =
      for
        input <- inputAttempt
      yield
        for 
          entry <- input.getLines
        yield 
          Calculator.value (entry).foreach: result =>
            println (s"$entry = $result")
    
        




