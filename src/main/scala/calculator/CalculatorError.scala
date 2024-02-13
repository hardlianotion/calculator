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
  case class IOError (file: String) extends CalculatorError


