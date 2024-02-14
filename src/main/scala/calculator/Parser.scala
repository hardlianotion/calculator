package calculator

import scala.util.parsing.combinator.RegexParsers

import calculator.Expression.CalculationExpression


private [calculator] abstract class ParserImpl extends RegexParsers:
  import Expression.*

  /**
   * Parser implementation is essentially lifted from scala.util.parsing.combinator.RegexParsers.
   *
   * Differences are:
   *  - Wanted to emphasize tree structure of the calculator's intermediate representation
   *  - extension to include a power operator, which has a higher precedence than multiplication
   */
  def number: Parser [Number] = """-?\d+(\.\d*)?""".r ^^ { x => Number (x.toDouble) }

  def bracket: Parser [Bracket] = "(" ~> linearCombination <~ ")" ^^ { x => Bracket (x) }

  def atom: Parser [Number | Bracket] = number | bracket

  private [calculator] def powerTerm: Parser [CalculationExpression] = rep (atom ~ "^") ~ atom ^^ {
    case list ~ number => list.foldRight (number) {
      (exp, rhs) => Bracket (Operation (exp._1, Pow, rhs))
    }
  }

  def multiplicativeSymbol: Parser [MultiplicativeOperator] = """[*/]""".r ^^ {
    case "*" => Multiply
    case "/" => Divide
  }
  def multiplyTerm: Parser [CalculationExpression] = powerTerm ~ rep (multiplicativeSymbol ~ powerTerm) ^^ {
    case number ~ list => list.foldLeft (number) { (agg, rhs) =>
        Operation (Bracket (agg), rhs._1, Bracket (rhs._2))
    }
  }

  def additiveSymbol: Parser [AdditiveOperator] = """[+-]""".r ^^ {
      case "+" => Add
      case "-" => Subtract
    }

  def linearCombination: Parser [CalculationExpression] = multiplyTerm ~ rep (additiveSymbol ~ multiplyTerm) ^^ {
    case number ~ list => list.foldLeft (number) { (agg, rhs) =>
      Operation (Bracket (agg), rhs._1, Bracket (rhs._2))
    }
  }
  
object Parser extends ParserImpl:
  import CalculatorError.*

  def readExpression (input: String): Either [ParserError, CalculationExpression] =
    parseAll (linearCombination, input) match
      case Success (expression, _) => Right (expression)
      case Failure (error, next) => Left (ParserError (error, next.source.toString))
      case Error (error, next) =>Left (ParserError (error, next.source.toString))