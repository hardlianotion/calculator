package calculator

import util.Try


object Calculator:
  import ExpressionParser.*

  /**
   * CalculationError - the errors Calculator is anticipated to make
   * 
   * NOTE - Calculator computes using floating point types, we don't
   *        so ArithmeticException errors are not encountered.
   */
  sealed trait CalculatorError
  
  case class ParserError (message: String, next: String) extends CalculatorError
  case class IOError (file: String) extends CalculatorError

  def readExpression (input: String): Either [CalculatorError, Expression] =
    ExpressionParser.parseAll (linearCombination, input) match
      case Success (expression, _) => Right (expression)
      case Failure (error, next) => Left (ParserError (error, next.source.toString))
      case Error (error, next) =>Left (ParserError (error, next.source.toString))

  def value (expression: Expression): Double =
    expression match
      case Expression.Bracket (subExpression) =>
        value (subExpression)
      case Expression.Operation (leftOperand, op, rightOperand) =>
        op.compute (value (leftOperand), value (rightOperand))
      case Expression.Number (value) =>
        value
        
  def value (expression: String): Either [CalculatorError, Double] =
    readExpression (expression).map (value)