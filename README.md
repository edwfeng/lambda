# The Lambda Lab
*by Edward Feng and Anthony Li*

Here's our implementation of the lambda lab. It's:
* Probably not compliant
* Probably not performant
* Probably not simple
* Probably not functional

## Building and Running
We use Gradle to manage our build. To get up and running with the interpreter, simply run:
```shell script
$ ./gradlew run
```
(If on Windows, run `gradlew.bat` instead.)

To build and test:
```shell script
$ ./gradlew build
```

To create a `.jar` file for distribution:
```shell script
$ ./gradlew jar
```
The `.jar` file will be located in the `build/libs` folder.

To generate a Javadoc:
```shell script
$ ./gradlew javadoc
```
The docs will be located in the `build/docs` folder.

## Code Structure
Code for the lambda interpreter and applicator is located in the `src/main` folder, under the `org.bergen.atcs.atics.lambda` package.

The tests in the `src/test` folder mostly test the lambda applicator/runtime.