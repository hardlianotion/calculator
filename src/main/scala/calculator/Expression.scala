package calculator

import scala.math


type Value = Double

sealed trait Expression

object Expression:
  
  sealed trait Operator:
    def compute (l: Double, r: Double): Double

  case object Add extends Operator:
    def compute (l: Double, r: Double): Double =
      l + r

  case object Subtract extends Operator:
    def compute (l: Double, r: Double): Double =
      l - r

  case object Multiply extends Operator:
    def compute (l: Double, r: Double): Double =
      l * r

  case object Divide extends Operator:
    def compute (l: Double, r: Double): Double =
      l / r

  case object Pow extends Operator:
    def compute (l: Double, r: Double): Double =
      math.pow (l, r)

  case class Number (value: Double) extends Expression

  case class Bracket (expression: Expression) extends Expression

  case class Operation (
    leftOperand: Bracket | Number, 
    op: Operator,
    rightOperand: Bracket | Number) extends Expression
