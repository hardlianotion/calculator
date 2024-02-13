#!/bin/sh

INPUT_FILE=input/defaults.calc

if [ -n "$1" ]; then
  INPUT_FILE="$1"
fi

java -jar ./bin/calculator.jar "${INPUT_FILE}"
