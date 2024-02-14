package calculator

import scala.math


type Value = Double

sealed trait Expression

object Expression:
  
  sealed trait Operator extends Expression:
    def compute (l: Double, r: Double): Double

  case object Pow extends Operator:
    def compute (l: Double, r: Double): Double =
      math.pow (l, r)

  sealed trait AdditiveOperator extends Operator
  case object Add extends AdditiveOperator:
    def compute (l: Double, r: Double): Double =
      l + r

  case object Subtract extends AdditiveOperator:
    def compute (l: Double, r: Double): Double =
      l - r

  sealed trait MultiplicativeOperator extends Operator
  case object Multiply extends MultiplicativeOperator:
    def compute (l: Double, r: Double): Double =
      l * r

  case object Divide extends MultiplicativeOperator:
    def compute (l: Double, r: Double): Double =
      l / r

  sealed trait CalculationExpression extends Expression
  case class Number (value: Double) extends CalculationExpression

  case class Bracket (expression: CalculationExpression) extends CalculationExpression

  case class Operation (
    leftOperand: Bracket | Number, 
    op: Operator,
    rightOperand: Bracket | Number) extends CalculationExpression
