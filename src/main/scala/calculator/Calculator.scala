package calculator

import calculator.CalculatorError.ParserError


object Calculator:
  import Parser.*
  import calculator.Expression.CalculationExpression
  
  def value (expression: CalculationExpression): Double =
    expression match
      case Expression.Bracket (subExpression) =>
        value (subExpression)
      case Expression.Operation (leftOperand, op, rightOperand) =>
        op.compute (value (leftOperand), value (rightOperand))
      case Expression.Number (value) =>
        value

  def value (expression: String): Either [ParserError, Double] =
    readExpression (expression).map (value)