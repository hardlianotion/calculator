ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.1"

val scalaParserCombinatorsVersion = "2.3.0"
val uTestVersion = "0.8.2"
val zioVersion = "2.1-RC1"

lazy val root = (project in file("."))
  .settings(
    name := "calculator",
    libraryDependencies ++= Seq (
      "com.lihaoyi" %% "utest" % uTestVersion % "test",
      "org.scala-lang.modules" %% "scala-parser-combinators" % scalaParserCombinatorsVersion
    ),
    testFrameworks += new TestFramework("utest.runner.Framework")
  )
