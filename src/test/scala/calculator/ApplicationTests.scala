package calculator

import utest.*


object ApplicationTests extends TestSuite:
  import Application.*

  val tests: Tests = Tests.apply:
    test ("We get fallback back if we can't read from noSuchFile"):
      val noSuchFile = "no-such-file.txt"
      val fallback = "We'll just start"

      assert (titleArt (noSuchFile, fallback) == fallback)
