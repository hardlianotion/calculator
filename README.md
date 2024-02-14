## Calculator
Calculator is a simple file-based calculator, built as a demo to compete for a job.

### Usage
Customer requirements eschew a conventional calculator interface in favour of a file-based input system.  There is a
ready-made file at `<root>\input\defaults.calc` with sample calculations that can be run as default.

### Features
 - Calculator models decimals using `Double`, a floating type, 
 - operator `^` has precedence over `*, /`, which have precedence over `+, -`,
 - the mathematical operations `+, -, ^, \, ^` have the usual mathematical properties:
   - `*` distributes over `+`,
   - `^` distributes over `*`.

### Caveats
 - No consistent number formatting - output typically shows differences between input and output number formats,
 - Parsing error reporting is poor: reported errors are misleading, further work would need to address this,
 - To keep the parsing simple, some unnecessary brackets are placed into the expression tree while parsing.
 - [uTest](https://github.com/com-lihaoyi/utest)'s `assert` reporting depends on compile-time information, so can't dynamically generate test 
   assertions or identifiers.  So error messages some tests look tidy give good output as, long 
   as they pass.  The error messages if they fail will be quite confusing.

### Build and run 
**From scripts** - the project contains some fragile bash scripts that can be called from 
 - `./build.sh` - clears `./bin` directory, builds a fat jar and places it at `./bin/calculator.jar`.
 - `./run.sh <formula-input.calc>` - if called with an input file with equations on each line, will
   run Calculator over input of the form
   ```
   8 + 9
   7 * 6
   (1 + 5)^(1 + 1) - (1 - 5)^2
   ```
   otherwise, if run without argument, will run Calculator with the file `input/defaults.calc`.
 - `./build-and-run.sh` - builds the fat jar and runs Calculator with the file `input/defaults.calc`.

**Using sbt** - we can also build the project using sbt.

*Prerequisites* - the application is built using
- sbt 1.9.8, but sbt versions >= 1.x.y should be fine,
- scala 3.3.1, but scala versions 3.* should be fine,
- java 21, but has been tested with java versions >= 17.

sbt has the following commands, which take the form `sbt <command-name>`. 

 - clean: remove all intermediate products `sbt clean`
 - compile: build source into class files `sbt compile`
 - test: run test cases `sbt test`
 - run: run the application `sbt run`
 - assemble: build an executable fat jar `sbt assembly`

A fat jar contains all the project dependencies, so can be run independently

`java -jar target/scala-<scala-version>/calculator-assembly-<version>.jar <formula-input.calc>`,

and `<formula-input.calc>` is optional, as before.  
Currently, `<scala-version> = 3.3.1` and `<version> = "0.1.0-SNAPSHOT"`.
