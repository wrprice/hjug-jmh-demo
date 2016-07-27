# An introduction to JMH

This repository of sample Java code, and the accompanying slide deck, is intended to introduce
the [Java Microbenchmark Harness][jmh] (JMH) to intermediate- and advanced-level Java developers.
The presentation slides are intended as the primary introductory material, supported by a sample
Gradle project and related source code.

### How to run the examples

This project requires a Java Development Kit (JDK), version 8 or newer, to be installed and
the `java` executable to be present on your command line executable path.

#### Gradle wrapper

This project uses [Gradle][gradle] 2.x as its build tool, which is a successor to tools such
as Ant and Maven. It includes a small wrapper script that will download the tool's JAR file into
a local directory and execute it without requiring any other software installation besides an
available JDK.  The wrapper comes in two forms: a shell script for Linux/Unix/Mac environments,
and a batch file for Windows command prompts.  Where the following instructions refer to `gradlew`
you may be required to use a slightly different command on your system, such as `./gradlew` in
Linux if the current directory is not a part of your _$PATH_.  All Gradle commands referenced
by this documentation should be run from the project directory (where the `gradlew` scripts reside).

All generated output files and reports will be placed within a `build/` directory created by
Gradle.  To empty this directory and start from a fresh state, use the command `gradlew clean`.

For more information on the various Gradle tasks available when building a Java project, please
refer to the [Gradle Java Plugin documentation][gradleJava].

#### JUnit

Some poor examples of performance tests are implemented as JUnit tests.  The command `gradlew test`
will compile and execute all tests.  You can limit execution to a single test file by specifying
a class name pattern. The following example executes any test class (in any package) where the
class name ends in _PoorPerfTest_ :

        gradlew test --tests *PoorPerfTest

#### JMH

Use the following command to run _all_ JMH benchmarks defined in this project.  Be warned that this
will take a _very long time_ to complete on most systems.

        gradlew jmh

In order to filter which benchmarks get executed in a given run, and therefore reduce execution
time, this project has defined a special property, `jmh.benchmark`, which can be defined using the
`-P` command line flag.  Specify a value that includes all or part of the desired benchmark's
package and/or class name.

        gradlew jmh -Pjmh.benchmark=JmhPerfTest   # runs any test with JmhPerfTest in the class name
        gradlew jmh -Pjmh.benchmark=org.hjug      # runs any test inside/under the org.hjug package

### License

#### Source Code

The source code included in this project is subject to the terms of the
[Mozilla Public License, v. 2.0][MPL2]. A copy of the license is included;
see the `LICENSE` file for specific terms and conditions. If a copy of the
MPL was not distributed with this file, you can obtain one at:
http://mozilla.org/MPL/2.0/

#### Presentation and Documentation

<a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/">
  <img alt="Creative Commons License" style="border-width:0"
       src="https://i.creativecommons.org/l/by-nc-sa/4.0/88x31.png" />
</a><br />
The presentation slides and documentation included in this project are licensed under a
<a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/">Creative Commons
Attribution-NonCommercial-ShareAlike 4.0 International License</a>.

You must credit the author and other contributors (if any) and include the URL of this source
material in any copies or derivative works.

<p style="text-align: center;">https://github.com/wrprice/hjug-jmh-demo</p>

<!-- refs -->
[jmh]: http://openjdk.java.net/projects/code-tools/jmh/
[gradle]: https://gradle.org/
[gradleJava]: https://docs.gradle.org/current/userguide/java_plugin.html
[MPL2]: http://mozilla.org/MPL/2.0/
