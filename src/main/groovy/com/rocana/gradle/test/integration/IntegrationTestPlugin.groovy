/*
 * Copyright (c) 2017 Rocana.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rocana.gradle.test.integration

import org.gradle.api.Plugin
import org.gradle.api.Project

class IntegrationTestPlugin implements Plugin<Project> {

  @Override
  void apply(Project project) {
    project.apply(plugin: 'java')

    project.configurations {
      testIntegration.extendsFrom test
    }

    project.sourceSets {
      test {
        java {
          exclude "**/*IT*.java"
        }
      }

      integrationTest {
        java {
          srcDirs = ["src/test/java"]
          include "**/*IT*.java"
        }

        compileClasspath = project.sourceSets.test.output + project.sourceSets.test.compileClasspath
        runtimeClasspath = project.sourceSets.test.runtimeClasspath
      }
    }

    project.tasks.compileIntegrationTestJava.dependsOn(project.tasks.testClasses)

    project.task(
      "integrationTest",
      dependsOn: ["integrationTestClasses"],
      group: "Verification", description: "Runs the integration tests."
    )
    project.tasks.check.dependsOn(project.tasks.integrationTest)
  }

}
