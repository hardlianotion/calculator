package calculator


/**
 * CalculationError - the errors Calculator is anticipated to make
 *
 * NOTE - Calculator computes using floating point types, we don't
 *        so ArithmeticException errors are not encountered.
 */
sealed trait CalculatorError

object CalculatorError:
  case class ParserError (message: String, next: String) extends CalculatorError
  case class IOError (message: String, file: String) extends CalculatorError

  private def fakeLog (message: String): Unit =
    System.err.println (message)
    
  private def errorMessage (error: CalculatorError): String =
    error match
      case ParserError (msg, next) => 
        s"ParserError: $msg, while parsing $next"
      case IOError (msg, name) =>
        s"IOError: $msg, while processing $name"
        
  def logError (error: CalculatorError): Unit =
    fakeLog (errorMessage (error))
    


