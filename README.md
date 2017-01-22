# gradle-integration-test-plugin

An integration testing plugin for Gradle.

## Building

This plugin uses Gradle as its build tool. To build the project, run the
following.

`gradle build`

## Using this Plugin

Apply the plugin to your project using:

```
apply plugin: "com.rocana.gradle.test.integration"

buildscript {
  dependencies {
    classpath "com.rocana.gradle:gradle-integration-test-plugin:some-version"
  }
}
```

This plugin requires no configuration beyond applying the plugin. Once applied,
the following changes are made to the project.

1. The `java` plugin is applied. If it's already applied, Gradle will prevent it
   from being applied twice.
2. A new dependency configuration named `integrationTest` is created that
   extends the standard `test` configuration added by the `java` plugin.
3. A new source set named `integrationTest` is created for Java files contained
   within the directory `src/test/java` with an include restricted to the
   pattern `**/*IT*.java`. The compilation and runtime classpaths contain
   everything the `test` source set contains with the notable addition of the
   test classes, themselves. (See 6 below).
4. The `test` source set is modified to contain an exclude for the pattern used
   in the `integrationTest` source set. That is, any test class name containing
   the string `IT` will _not_ execute during the `test` task, and _will_ execute
   during the `integrationTest` task (see below).
5. Tasks are created for the compilation, resource processing, and execution of
   the integration tests. These tasks are named `compileIntegrationTestClasses`,
   `processIntegrationTestResources`, and `integrationTest`, respectively.
6. The `compileIntegrationTestClasses` test is set to depend on `testClasses` to
   ensure all test classes are compiled before the integration tests. This,
   combined with the `integrationTest` dependency configuration, allows
   integration tests to reference classes from the `test` source set.
7. The `check` task is modified to depend on the newly added `integrationTest`
   task. This means integration tests will run as part of `gradle check` but not
   `gradle test`.

Summary:

* Put your integration tests in `src/test/java` and include `IT` somewhere in
  the class name. You can reference both `main` and `test` classes.
* To run your unit tests, use `gradle test` as you normally would.
* To run your integration tests, use `gradle integrationTest`.
* Your integration tests use the same dependencies as your `test` dependencies,
  but you can add integration test-only dependencies using:
  ```
  dependencies {
    integrationTest "some.group:some-module:1.0.0"
  }
  ```
* Integration tests will automatically run as part of `gradle build` but you
  skip them by running `gradle build -x integrationTest` just as you would skip
  unit tests with `gradle build -x test`.
