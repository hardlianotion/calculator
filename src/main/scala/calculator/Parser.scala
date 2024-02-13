package calculator

import scala.util.parsing.combinator.RegexParsers


private [calculator] abstract class ParserImpl extends RegexParsers:
  import Expression.*

  /**
   * Parser implementation is essentially lifted from scala.util.parsing.combinator.RegexParsers.
   *
   * Differences are:
   *  - Wanted to emphasize tree structure of the calculator's intermediate representation
   *  - extension to include a power operator, which has a higher precedence than multiplication
   *
   * Implementation detail:
   *  - multiplyTerm and linearCombination yield a compiler warning because the match statement
   *    is not exhaustive.  This can be avoided by parsing into multiplicative and additive structures
   *    respectively.  But here, we can verify fairly simply for each function that the only possible
   *    parsed candidates appear in the match term, so we are content to leave the typing simple.
   */
  def number: Parser [Number] = """-?\d+(\.\d*)?""".r ^^ { x => Number (x.toDouble) }

  def bracket: Parser [Bracket] = "(" ~> linearCombination <~ ")" ^^ { x => Bracket (x) }

  def atom: Parser [Number | Bracket] = number | bracket

  private [calculator] def powerTerm: Parser [Expression] = rep (atom ~ "^") ~ atom ^^ {
    case list ~ number => list.foldRight (number) {
      (exp, rhs) => Bracket (Operation (exp._1, Pow, rhs))
    }
  }

  def multiplyTerm: Parser [Expression] = powerTerm ~ rep ("*" ~ powerTerm | "/" ~ powerTerm) ^^ {
    case number ~ list => list.foldLeft (number):
      case (expr: Expression, "*" ~ term) => Operation (Bracket (expr), Multiply, Bracket (term))
      case (expr: Expression, "/" ~ term) => Operation (Bracket (expr), Divide, Bracket (term))
  }

  def linearCombination: Parser [Expression] = multiplyTerm ~ rep ("+" ~ multiplyTerm | "-" ~ multiplyTerm) ^^ {
    case number ~ list => list.foldLeft (number):
      case (expr, "+" ~ term) => Operation (Bracket (expr), Add, Bracket (term))
      case (expr, "-" ~ term) => Operation (Bracket (expr), Subtract, Bracket (term))
  }
  
object Parser extends ParserImpl:
  import CalculatorError.*

  def readExpression (input: String): Either [CalculatorError, Expression] =
    parseAll (linearCombination, input) match
      case Success (expression, _) => Right (expression)
      case Failure (error, next) => Left (ParserError (error, next.source.toString))
      case Error (error, next) =>Left (ParserError (error, next.source.toString))