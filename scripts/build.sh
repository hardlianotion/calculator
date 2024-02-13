#!/bin/sh
#
# WARNING - tHis script will delete the contents of your bin directory.
#
# ASSUMPTION - sbt is installed 1.*.  The build has been tested with sbt 1.9.8

if [ ! -d ./bin ]; then
  mkdir ./bin
else
  rm -rf ./bin/*
fi

sbt clean test assembly

mv target/scala-3.3.1/calculator-assembly*.jar bin/calculator.jar
