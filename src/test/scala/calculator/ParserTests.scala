package calculator

import calculator.Calculator.ParserError
import utest.*


object ParserTests extends TestSuite:
  import Expression.*

  val tests: Tests = Tests.apply:

    test ("White space is not significant between multiplication elements"):
      val input1 = "14+12"
      val input2 = "14 +12"
      val input3 = "14 + 12"
      val input4 = " 14 + 12"

      val expected = Right (Operation (Bracket (Number (14)), Add, Bracket (Number (12))))
      
      assert (
        Calculator.readExpression (input1) == expected,
        Calculator.readExpression (input2) == expected,
        Calculator.readExpression (input3) == expected,
        Calculator.readExpression (input4) == expected)

    test ("White space is not significant between power elements"):
      val input1 = "14^12"
      val input2 = "14 ^12"
      val input3 = "14 ^ 12"
      val input4 = " 14 ^ 12"

      val expected = Right (Bracket (Operation (Number (14), Pow, Number (12))))

      assert (
        Calculator.readExpression (input1) == expected,
        Calculator.readExpression (input2) == expected,
        Calculator.readExpression (input3) == expected,
        Calculator.readExpression (input4) == expected
      )

    test ("Calculator can read a number correctly"):

      assert (
        Calculator.readExpression ("3.5") == Right (Number (3.5)),
        Calculator.readExpression ("3.") == Right (Number (3)),
        Calculator.readExpression ("-3") == Right (Number (-3))
      )

    test ("Calculator can read nested power expressions"):
      val result = Calculator.readExpression ("3^2^1")
      assert (result == Right (Bracket (Operation (Number (3), Pow, Bracket (Operation (Number (2), Pow, Number (1)))))))

    test ("Calculator can read nested multiply expressions"):
      val result = Calculator.readExpression ("3*2*1")
      assert (result == Right (Operation (Bracket (Operation (Bracket (Number (3)), Multiply, Bracket (Number (2)))), Multiply, Bracket (Number (1)))))

    test ("Calculator can read nested () expressions"):
      val result = Calculator.readExpression ("((((3))))")
      assert (result == Right (Bracket (Bracket (Bracket (Bracket (Number (3)))))))

    test ("Number parser fails on faulty fixed point representations"):
      val result1 = Calculator.readExpression ("3..5")
      val result2 = Calculator.readExpression ("3 5")
      val result3 = Calculator.readExpression ("35f")
      val result4 = Calculator.readExpression ("Nan")

      // FIXME - need to think about these unexpected errors.  Coarsen them perhaps?
      assert (
        result1 == Left (ParserError ("'^' expected but '.' found", "3..5")),
        result2 == Left (ParserError ("'^' expected but '5' found", "3 5")),
        result3 == Left (ParserError ("'^' expected but 'f' found", "35f")),
        result4 == Left (ParserError ("'(' expected but 'N' found", "Nan")))

    test ("Number parser fails on invalid bracket implementations"):
      val result1 = Calculator.readExpression ("()")

      assert (result1 == Left (ParserError ("'(' expected but ')' found", "()")))

    test ("No implied implementations"):
      val result = Calculator.readExpression ("(4 * 5) (6 * 7)")
      assert (result == Left (ParserError ("'^' expected but '(' found","(4 * 5) (6 * 7)")))

