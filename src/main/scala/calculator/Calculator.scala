package calculator

import calculator.CalculatorError.ParserError


object Calculator:
  import Parser.*
  import calculator.Symbol.Expression
  
  def value (expression: Expression): Double =
    expression match
      case Symbol.Bracket (subExpression) =>
        value (subExpression)
      case Symbol.Operation (leftOperand, op, rightOperand) =>
        op.compute (value (leftOperand), value (rightOperand))
      case Symbol.Number (value) =>
        value

  def value (expression: String): Either [ParserError, Double] =
    readExpression (expression).map (value)