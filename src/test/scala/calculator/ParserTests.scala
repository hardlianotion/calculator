package calculator

import calculator.CalculatorError.ParserError
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
      val result1 = Parser.readExpression (input1)
      val result2 = Parser.readExpression (input2)
      val result3 = Parser.readExpression (input3)
      val result4 = Parser.readExpression (input4)

      assert (
        result1 == expected,
        result2 == expected,
        result3 == expected,
        result4 == expected)

    test ("White space is not significant between power elements"):
      val input1 = "14^12"
      val input2 = "14 ^12"
      val input3 = "14 ^ 12"
      val input4 = " 14 ^ 12"

      val expected = Right (Bracket (Operation (Number (14), Pow, Number (12))))

      assert (
        Parser.readExpression (input1) == expected,
        Parser.readExpression (input2) == expected,
        Parser.readExpression (input3) == expected,
        Parser.readExpression (input4) == expected
      )

    test ("Parser can read a number correctly"):

      assert (
        Parser.readExpression ("3.5") == Right (Number (3.5)),
        Parser.readExpression ("3.") == Right (Number (3)),
        Parser.readExpression ("-3") == Right (Number (-3))
      )

    test ("Parser can parse simple addition."):
      val result = Parser.readExpression ("3 + 2")

      assert (result == Right (Operation (Bracket (Number (3)), Add, Bracket (Number (2)))))

    test ("Parser can parse simple subtraction."):
      val result = Parser.readExpression ("3 - 2")

      assert (result == Right (Operation (Bracket (Number (3)), Subtract, Bracket (Number (2)))))

    test ("Parser can parse simple multiplication."):
      val result = Parser.readExpression ("3 * 2")

      assert (result == Right (Operation (Bracket (Number (3)), Multiply, Bracket (Number (2)))))

    test ("Parser can parse simple division."):
      val result = Parser.readExpression ("3 / 2")

      assert (result == Right (Operation (Bracket (Number (3)), Divide, Bracket (Number (2)))))

    test ("Parser can parse simple exponentiation."):
      val result = Parser.readExpression ("3 ^ 2")

      assert (result == Right (Bracket (Operation (Number (3), Pow, Number (2)))))

    test ("Parser can read nested power expressions"):
      val result = Parser.readExpression ("3^2^1")
      assert (result == Right (Bracket (Operation (Number (3), Pow, Bracket (Operation (Number (2), Pow, Number (1)))))))

    test ("Parser can read nested negative powers correctly"):
      val result = Parser.readExpression ("3^-2^-1")
      assert (
        result == Right (Bracket (Operation (Number (3), Pow, Bracket (Operation (Number (-2), Pow, Number (-1)))))))

    test ("Parser can read nested multiply expressions"):
      val result = Parser.readExpression ("3*2*1")
      assert (result == Right (Operation (Bracket (Operation (Bracket (Number (3)), Multiply, Bracket (Number (2)))), Multiply, Bracket (Number (1)))))

    test ("Parser can read nested () expressions"):
      val result = Parser.readExpression ("((((3))))")
      assert (result == Right (Bracket (Bracket (Bracket (Bracket (Number (3)))))))

    test ("Parser fails on faulty fixed point representations"):
      val result1 = Parser.readExpression ("3..5")
      val result2 = Parser.readExpression ("3 5")
      val result3 = Parser.readExpression ("35f")
      val result4 = Parser.readExpression ("Nan")

      // FIXME - need to think about these unexpected errors.  Coarsen them perhaps?
      assert (
        result1 == Left (ParserError ("'^' expected but '.' found", "3..5")),
        result2 == Left (ParserError ("'^' expected but '5' found", "3 5")),
        result3 == Left (ParserError ("'^' expected but 'f' found", "35f")),
        result4 == Left (ParserError ("'(' expected but 'N' found", "Nan")))

    test ("Parser fails on invalid bracket implementations"):
      val result1 = Parser.readExpression ("()")

      assert (result1 == Left (ParserError ("'(' expected but ')' found", "()")))

    test ("Parser permits no implied multiplication operators"):
      val result = Parser.readExpression ("(4 * 5) (6 * 7)")
      assert (result == Left (ParserError ("'^' expected but '(' found","(4 * 5) (6 * 7)")))
