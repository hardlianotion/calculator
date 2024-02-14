## Calculator
Calculator is a simple file-based calculator, built as a demo to compete for a job.

### Prerequisites 
The application is built using
- [git](https://git-scm.com/downloads),
- [sbt 1.9.8](https://www.scala-sbt.org/download/), but sbt versions >= 1.x.y should be fine,
- [scala 3.3.1](https://www.scala-lang.org/download/3.3.1.html), but scala versions 3.* should be fine,
- [java 21](https://openjdk.org/install/), but has been tested with java versions >= 17.

### Quick Start
Clone the project and switch to the project root:
```
git clone git@github.com:hardlianotion/calculator.git
cd calculator
```

Quick build on a POSIX compliant shell is from a script run from the project root:

`scripts\build-and-run.sh`

This will run commands found in the formula script `input\defaults.calc`.

If you are using Scala 3.3.1, the equivalent build and run sequence using sbt from project root is:
```
sbt assembly
java -jar target/scala-3.3.1/calculator-assembly-0.1.0-SNAPSHOT.jar input/defaults.calc
```

Further build and usage instructions can be found in section *Build and run*.

### Detailed Calculator Description

Calculator takes an formula file as input and processes each line in the file.  Lines that represent simple
mathematical formulas are parsed into an expression tree.  The others are converted into errors.  Calculator values
each expression it produces and prints the value to screen.  Calculator prints out each error it encounters to screen,
with the formula that gives rise to it.

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

### Design
Calculator parses `String`s into `Expression`s.  An `Expression` is a syntax tree that describes a simple 
mathematical formula.

Calculator has a few components (unfortunately, one of them is called `Calculator` - ed)
 - `Parser` - converts a string into an `Expression`, if the string is a mathematical formula that the Parser 
              understands, or an error otherwise,
 - `Calculator` - processes `Expression` depth-first to produce a value.  Calculator can calculate any `Exoression` 
                  produced by `Parser`,
 - `Application` - responsible for input and output to `Parser` and `Calculator`.  `Application` loads file input 
                   into `Parser` and prints `Calculator` output to the screen.
 - `Calculator Error` - describes and logs application errors.  The application recognises parsing errors or IO errors.

### Build and run 
**From scripts** - On a POSIX compatible shell, call the following fragile bash scripts from the root directory: 
 - `scripts/build.sh` - clears `./bin` directory, builds a fat jar and places it at `./bin/calculator.jar`.
 - `scripts/run.sh <formula-input.calc>` - if called with an input file with equations on each line, will
   run Calculator over input of the form
   ```
   8 + 9
   7 * 6
   (1 + 5)^(1 + 1) - (1 - 5)^2
   ```
   otherwise, if run without argument, will run Calculator with the file `input/defaults.calc`.
 - `scripts/build-and-run.sh` - builds the fat jar and runs Calculator with the file `input/defaults.calc`.

**Using sbt** - we can also build the project using sbt.

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
