package calculator

import utest.*


object ApplicationTests extends TestSuite:
  import Application.*

  val tests: Tests = Tests.apply:
    test ("We get a fallback call if we can't read from "):
      val noSuchFile = "no-such-file.txt"
      val fallback = "We'll just start"

      assert (true)
      assert (titleArt (noSuchFile, fallback) == fallback)
