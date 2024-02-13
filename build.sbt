ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.1"

val scalaParserCombinatorsVersion = "2.3.0"
val uTestVersion = "0.8.2"

lazy val root = (project in file ("."))
  .settings (
    name := "calculator",
    libraryDependencies ++= Seq (
      "com.lihaoyi" %% "utest" % uTestVersion % "test",
      "org.scala-lang.modules" %% "scala-parser-combinators" % scalaParserCombinatorsVersion
    ),
    testFrameworks += TestFramework ("utest.runner.Framework"),
    scalacOptions += "-target:17",
    javacOptions ++= Seq ("-source", "17", "-target", "17")
  )
