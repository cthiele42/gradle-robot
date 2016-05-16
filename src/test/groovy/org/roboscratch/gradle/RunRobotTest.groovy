/*
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
        "License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
        software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
*/

package org.roboscratch.gradle

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test
import static org.junit.Assert.assertThat

import org.xmlunit.builder.Input
import javax.xml.transform.Source
import static org.xmlunit.matchers.EvaluateXPathMatcher.hasXPath as evalXPath
import static org.xmlunit.matchers.HasXPathMatcher.hasXPath;
import static org.hamcrest.CoreMatchers.equalTo
import static org.hamcrest.core.IsNot.not;
import org.gradle.api.GradleException

class RunRobotTest {
    @Test
    public void executeSingleFile() {
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'org.roboscratch.robot'

        def  task = project.task("executeSingleFile", type: RunRobot) {
            outputdir = "build/results-singleTest"
            data_sources = "src/test/acceptancetest/data_sources/test.robot"
        }
        task.execute()

        Source output = Input.fromFile("build/results-singleTest/output.xml").build()
        assertThat(output, evalXPath("count(/robot/suite)", equalTo("1")))
        assertThat(output, not(hasXPath("/robot/suite/suite")))
    }

    @Test
    public void executeMultipleFiles() {
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'org.roboscratch.robot'

        def  task = project.task("executeMultipleFiles", type: RunRobot) {
            outputdir = "build/results-multipleTests"
            data_sources = ["src/test/acceptancetest/data_sources/test.robot", "src/test/acceptancetest/data_sources/test2.robot"]
        }
        task.execute()

        Source output = Input.fromFile("build/results-multipleTests/output.xml").build()
        assertThat(output, evalXPath("count(/robot/suite)", equalTo("1")))
        assertThat(output, evalXPath("count(/robot/suite/suite)", equalTo("2")))
    }

    @Test
    public void singleVariableFile() {
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'org.roboscratch.robot'

        def  task = project.task("singleVariableFile", type: RunRobot) {
            outputdir = "build/results-singleVariablefile"
            data_sources = "src/test/acceptancetest/variablefiles/variablefileTest.robot"
            variablefiles = "src/test/resources/variables1.py"
        }
        task.execute()
    }

    @Test
    public void multipleVariableFiles() {
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'org.roboscratch.robot'

        def  task = project.task("multipleVariableFiles", type: RunRobot) {
            outputdir = "build/results-twoVariablefilesTest"
            data_sources = "src/test/acceptancetest/variablefiles/twoVariablefilesTest.robot"
            variablefiles = ["src/test/resources/variables1.py", "src/test/resources/variables2.py"]
        }
        task.execute()
    }

    @Test(expected = GradleException.class)
    public void testFailure() {
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'org.roboscratch.robot'

        def  task = project.task("testFailure", type: RunRobot) {
            outputdir = "build/results-failure"
            data_sources = "src/test/acceptancetest/fail/fail.robot"
        }
        task.execute()
    }

    @Test
    public void ignoreFailure() {
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'org.roboscratch.robot'

        def  task = project.task("ignoreFailure", type: RunRobot) {
            outputdir = "build/results-failure"
            data_sources = "src/test/acceptancetest/fail/fail.robot"
            ignoreFailures = true
        }
        task.execute()
    }

    @Test
    public void singleVariable() {
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'org.roboscratch.robot'

        def  task = project.task("singleVariable", type: RunRobot) {
            outputdir = "build/results-singleVariable"
            data_sources = "src/test/acceptancetest/variables/variableTest.robot"
            variables = "VAR1:valueOfVar1"
        }
        task.execute()
    }

    @Test
    public void multipleVariables() {
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'org.roboscratch.robot'

        def  task = project.task("multipleVariables", type: RunRobot) {
            outputdir = "build/results-twoVariablesTest"
            data_sources = "src/test/acceptancetest/variables/twoVariablesTest.robot"
            variables = ["VAR1:valueOfVar1", "VAR2:valueOfVar2"]
        }
        task.execute()
    }

    @Test
    public void singleSuite() {
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'org.roboscratch.robot'

        def  task = project.task("singleSuite", type: RunRobot) {
            outputdir = "build/results-singleSuite"
            data_sources = "src/test/acceptancetest/suites"
            suites = "suiteTest"
        }
        task.execute()
    }

    @Test
    public void multipleSuites() {
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'org.roboscratch.robot'

        def  task = project.task("multipleSuites", type: RunRobot) {
            outputdir = "build/results-multipleSuites"
            data_sources = "src/test/acceptancetest/suites"
            suites = ["suiteTest", "twoSuites"]
        }
        task.execute()

        Source output = Input.fromFile("build/results-multipleSuites/output.xml").build()
        assertThat(output, evalXPath("count(/robot/suite)", equalTo("1")))
        assertThat(output, evalXPath("count(/robot/suite/suite)", equalTo("2")))
    }

    @Test
    public void customSuiteName() {
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'org.roboscratch.robot'

        def  task = project.task("customSuiteName", type: RunRobot) {
            outputdir = "build/results-customSuiteName"
            data_sources = "src/test/acceptancetest/customSuiteName"
            suitename = "Custom Suite Name"
        }
        task.execute()

        Source output = Input.fromFile("build/results-customSuiteName/output.xml").build()
        assertThat(output, hasXPath("/robot/suite[@name = 'Custom Suite Name']"))
    }

    @Test
    public void customSuiteDoc() {
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'org.roboscratch.robot'

        def  task = project.task("customSuiteDoc", type: RunRobot) {
            outputdir = "build/results-customSuiteDoc"
            data_sources = "src/test/acceptancetest/customSuiteDoc"
            suitedoc = "Custom Suite Documentation"
        }
        task.execute()

        Source output = Input.fromFile("build/results-customSuiteDoc/output.xml").build()
        assertThat(output, hasXPath("/robot/suite/doc[text() = 'Custom Suite Documentation']"))
    }

    @Test
    public void singleMetadata() {
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'org.roboscratch.robot'

        def  task = project.task("singleMetadata", type: RunRobot) {
            outputdir = "build/results-singleMetadata"
            data_sources = "src/test/acceptancetest/metadata"
            metadata = "KEY1:value1"
        }
        task.execute()

        Source output = Input.fromFile("build/results-singleMetadata/output.xml").build()
        assertThat(output, evalXPath("count(/robot/suite/metadata/item)", equalTo("1")))
        assertThat(output, hasXPath("/robot/suite/metadata/item[@name = 'KEY1' and text() = 'value1']"))
    }

    @Test
    public void multipleMetadata() {
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'org.roboscratch.robot'

        def  task = project.task("multipleMetadata", type: RunRobot) {
            outputdir = "build/results-multipleMetadata"
            data_sources = "src/test/acceptancetest/metadata"
            metadata = ["KEY1:value1", "KEY2:value2"]
        }
        task.execute()

        Source output = Input.fromFile("build/results-multipleMetadata/output.xml").build()
        assertThat(output, evalXPath("count(/robot/suite/metadata/item)", equalTo("2")))
        assertThat(output, hasXPath("/robot/suite/metadata/item[@name = 'KEY1' and text() = 'value1']"))
        assertThat(output, hasXPath("/robot/suite/metadata/item[@name = 'KEY2' and text() = 'value2']"))
    }
    @Test
    public void singleTag() {
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'org.roboscratch.robot'

        def  task = project.task("singleTag", type: RunRobot) {
            outputdir = "build/results-singleTag"
            data_sources = "src/test/acceptancetest/tags"
            tags = "TAG1"
        }
        task.execute()

        Source output = Input.fromFile("build/results-singleTag/output.xml").build()
        assertThat(output, evalXPath("count(/robot/suite/suite/test/tags/tag)", equalTo("1")))
        assertThat(output, hasXPath("/robot/suite/suite/test/tags/tag[text() = 'TAG1']"))
    }

    @Test
    public void multipleTags() {
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'org.roboscratch.robot'

        def  task = project.task("multipleTags", type: RunRobot) {
            outputdir = "build/results-multipleTags"
            data_sources = "src/test/acceptancetest/tags"
            tags = ["TAG1", "TAG2"]
        }
        task.execute()

        Source output = Input.fromFile("build/results-multipleTags/output.xml").build()
        assertThat(output, evalXPath("count(/robot/suite/suite/test/tags/tag)", equalTo("2")))
        assertThat(output, hasXPath("/robot/suite/suite/test/tags/tag[text() = 'TAG1']"))
        assertThat(output, hasXPath("/robot/suite/suite/test/tags/tag[text() = 'TAG2']"))
    }

    @Test
    public void singleTest() {
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'org.roboscratch.robot'

        def  task = project.task("singleTest", type: RunRobot) {
            outputdir = "build/results-singleTest"
            data_sources = "src/test/acceptancetest/tests"
            tests = "First Test"
        }
        task.execute()

        Source output = Input.fromFile("build/results-singleTest/output.xml").build()
        assertThat(output, evalXPath("count(/robot/suite/suite/test)", equalTo("1")))
        assertThat(output, hasXPath("/robot/suite/suite/test[@name = 'First Test']"))
    }

    @Test
    public void multipleTests() {
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'org.roboscratch.robot'

        def  task = project.task("multipleTests", type: RunRobot) {
            outputdir = "build/results-multipleTests"
            data_sources = "src/test/acceptancetest/tests"
            tests = ["First Test", "Second Test"]
        }
        task.execute()

        Source output = Input.fromFile("build/results-multipleTests/output.xml").build()
        assertThat(output, evalXPath("count(/robot/suite/suite/test)", equalTo("2")))
        assertThat(output, hasXPath("/robot/suite/suite/test[@name = 'First Test']"))
        assertThat(output, hasXPath("/robot/suite/suite/test[@name = 'Second Test']"))
    }

    @Test
    public void rerunfailed() {
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'org.roboscratch.robot'

        def  task = project.task("rerunfail", type: RunRobot) {
            outputdir = "build/results-rerunfail"
            data_sources = "src/test/acceptancetest/rerunfailed"
            ignoreFailures = true
        }
        task.execute()

        //rerunfailed
        def  taskrerun = project.task("rerunfailed", type: RunRobot) {
            outputdir = "build/results-rerunfailed"
            data_sources = "src/test/acceptancetest/rerunfailed"
            variables = "VAR1:valueOfVar1"
            rerunfailed = "build/results-rerunfail/output.xml"
        }
        task.execute()
    }
}