## Calculator
Calculator is a simple file-based calculator, built as a demo to compete for a job.

### Usage
Customer requirements eschew a conventional calculator interface in favour of a file-based input system.  There is a
ready-made file at `<root>\input\defaults.calc` with sample calculations that can be run as default.

## Features
 - decimals are modelled using `Double`, a floating type.  This may confound user expectations which might expect  
   a fixed point type.  In doing this, we trade away uniform precision and gain implementation simplicity and 
   avoidance of overflow errors, slightly simplifying the valuation framework. 
 - operator `^` has precedence over `*, /`, which have precedence over `+, -`,
 - the mathematical operations `+, -, ^, \, ^` have the usual mathematical properties:
   - `*` distributes over `+`,
   - `^` distributes over `*`,

## Caveats
 - Parsing error reporting is woeful: the errors reported will mislead the user as to where the problem lies.
   Researching more libraries, or taking greater pains to analyse the underlying error structure may improve this.
 - To keep the parsing simple, some unnecessary brackets are placed into the expression tree while parsing.
 - We use Li Haoyi's [uTest](https://github.com/com-lihaoyi/utest).  This is a cheap and cheerful framework with a few 
   small surprises.  `assert` error reporting depends on compile-time information, so dynamically generating assertions 
   and test identifiers is not really in question.  It also means that some of the tests here give good output as long 
   as they pass.  The error messages when they fail will be quite confusing.

### Getting started
One-click build and run with default options `./setup_and_run.sh`

### Build, test, run and package
Requirements sbt - sbt 1.9.8 was used to build calculator.

 - compile: `sbt compile`
 - test: `sbt test`
 - run: `sbt run`
 - package: `sbt package`

The package command builds an executable fat jar.
