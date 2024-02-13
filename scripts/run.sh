#!/bin/sh

INPUT=input/defaults.calc

if [ -n "$1" ]; then
  INPUT="$1"
fi

java -jar ./bin/calculator.jar "$INPUT"
