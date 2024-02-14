package calculator

import utest.*
import math.pow

object ComputationTests extends TestSuite:
  import Symbol.*

  val tests: Tests = Tests.apply:
    test ("Addition works properly"):
      val expr = Operation (Number (3), Add, Number (2))
      val expected = 3 + 2
      assert (Calculator.value (expr) == expected)

    test ("Subtraction works properly"):
      val expr = Operation (Number (3), Subtract, Number (2))
      val expected = 3 - 2
      assert (Calculator.value (expr) == expected)

    test ("Multiplication works properly"):
      val expr = Operation (Number (3), Multiply, Number (2))
      val expected = 3 * 2
      assert (Calculator.value (expr) == expected)

    test ("Division works properly"):
      val expr = Operation (Number (3), Divide, Number (2))
      val expected = 3.0 / 2.0
      assert (Calculator.value (expr) == expected)

    test ("Exponentiation works properly"):
      val expr = Operation (Number (3), Pow, Number (2))
      val expected = pow (3, 2)
      assert (Calculator.value (expr) == expected)

    test ("Can compute nested powers correctly"):
      val expr = Bracket (Operation (Number (5), Pow, Bracket (Operation (Number (3), Pow, Number (2)))))
      val input = "5^3^2"
      
      val result1 = Calculator.value (input) 
      val result2 = Calculator.value (expr)
      val expected = pow (5.0, pow (3.0, 2.0))
      
      assert (result1 == Right (expected), result2 == expected)
      
    test ("Can compute iterated multiplication correctly"):
      val expr = Operation (
                  Number (5), Multiply, 
                  Bracket (Operation (Number (4), Multiply, Bracket (Operation (Number (3), Multiply, Number (2))))))
      val input = "5*4*3*2"

      val result1 = Calculator.value (input)
      val result2 = Calculator.value (expr)
      val expected = 5.0 * 4.0 * 3.0 * 2.0

      assert (result1 == Right (expected), result2 == expected)
    
    test ("Divide by zero gives Infinity"):
      val expr = Operation (Number (5), Divide, Number (0))
      val result = Calculator.value (expr)
      val negExpr = Operation (Number (-5), Divide, Number (0))
      val negResult = Calculator.value (negExpr)
      
      assert (result == Double.PositiveInfinity)
      assert (negResult == Double.NegativeInfinity)
    
    test ("zero divided by zero gives NaN"):
      assert (Calculator.value ("0 / 0").map (_.isNaN) == Right (true))
      
    test ("multiplication has higher precedence than addition"):
      val plusThenTimesNoParens = "3 + 4 * 5"
      val plusThenTimesParens = "3 + (4 * 5)"
      val timesThemPlusParens = "4 * 5 + 3"
      
      assert (Calculator.value (plusThenTimesNoParens) == Calculator.value (plusThenTimesParens))
      assert (Calculator.value (timesThemPlusParens) == Calculator.value (plusThenTimesParens))
    
    test ("exponentiation has higher precedence than multiplication"):
      val powThenTimesNoParens = "3^4 * 5"
      val timesThenPowParens = "5 * (3^4)"
      val timesThenPowNoParens = "5 * 3^4"

      assert (Calculator.value (powThenTimesNoParens) == Calculator.value (timesThenPowParens))
      assert (Calculator.value (powThenTimesNoParens) == Calculator.value (timesThenPowNoParens))

    test ("multiplication distributes over addition"):
      val bracketed = "(3 + 4) * 5"
      val distributed = "3*5 + 4*5"
    
      assert (Calculator.value (bracketed) == Calculator.value (distributed))
    
    test ("exponentiation distributes over multiplication"):
      val bracketed = "(3 * 4) ^ 5"
      val distributed = "3^5 * 4^5"

      assert (Calculator.value (bracketed) == Calculator.value (distributed))
    