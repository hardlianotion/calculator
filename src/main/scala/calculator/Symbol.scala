package calculator

import scala.math


type Value = Double

sealed trait Symbol

object Symbol:
  
  sealed trait Operator extends Symbol:
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

  sealed trait Expression extends Symbol

  case class Number (value: Double) extends Expression

  case class Bracket (expression: Expression) extends Expression

  case class Operation (
    leftOperand: Bracket | Number, 
    op: Operator,
    rightOperand: Bracket | Number) extends Expression
