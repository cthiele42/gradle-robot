/*
 * Copyright 2016 Claas Thiele.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.roboscratch.gradle

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test
import static org.junit.Assert.assertThat

import org.xmlunit.builder.Input
import javax.xml.transform.Source

import static org.junit.Assert.assertTrue
import static org.xmlunit.matchers.EvaluateXPathMatcher.hasXPath as evalXPath
import static org.xmlunit.matchers.HasXPathMatcher.hasXPath;
import static org.hamcrest.CoreMatchers.equalTo
import static org.hamcrest.core.IsNot.not;
import org.gradle.api.GradleException

class RunRobotTest {
    @Test
    public void executeSingleFile() {
        Project project = ProjectBuilder.builder().withProjectDir(new File("").absoluteFile).build()
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
        Project project = ProjectBuilder.builder().withProjectDir(new File("").absoluteFile).build()
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
        Project project = ProjectBuilder.builder().withProjectDir(new File("").absoluteFile).build()
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
        Project project = ProjectBuilder.builder().withProjectDir(new File("").absoluteFile).build()
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
        Project project = ProjectBuilder.builder().withProjectDir(new File("").absoluteFile).build()
        project.pluginManager.apply 'org.roboscratch.robot'

        def  task = project.task("testFailure", type: RunRobot) {
            outputdir = "build/results-failure"
            data_sources = "src/test/acceptancetest/fail/fail.robot"
        }
        task.execute()
    }

    @Test
    public void ignoreFailure() {
        Project project = ProjectBuilder.builder().withProjectDir(new File("").absoluteFile).build()
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
        Project project = ProjectBuilder.builder().withProjectDir(new File("").absoluteFile).build()
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
        Project project = ProjectBuilder.builder().withProjectDir(new File("").absoluteFile).build()
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
        Project project = ProjectBuilder.builder().withProjectDir(new File("").absoluteFile).build()
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
        Project project = ProjectBuilder.builder().withProjectDir(new File("").absoluteFile).build()
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
        Project project = ProjectBuilder.builder().withProjectDir(new File("").absoluteFile).build()
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
        Project project = ProjectBuilder.builder().withProjectDir(new File("").absoluteFile).build()
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
        Project project = ProjectBuilder.builder().withProjectDir(new File("").absoluteFile).build()
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
        Project project = ProjectBuilder.builder().withProjectDir(new File("").absoluteFile).build()
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
        Project project = ProjectBuilder.builder().withProjectDir(new File("").absoluteFile).build()
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
        Project project = ProjectBuilder.builder().withProjectDir(new File("").absoluteFile).build()
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
        Project project = ProjectBuilder.builder().withProjectDir(new File("").absoluteFile).build()
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
        Project project = ProjectBuilder.builder().withProjectDir(new File("").absoluteFile).build()
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
    public void includeSinglePattern() {
        Project project = ProjectBuilder.builder().withProjectDir(new File("").absoluteFile).build()
        project.pluginManager.apply 'org.roboscratch.robot'

        def  task = project.task("includeSinglePattern", type: RunRobot) {
            outputdir = "build/results-includeSinglePattern"
            data_sources = "src/test/acceptancetest/includeAndExclude"
            include = ["tobeincluded"]
        }
        task.execute()

        Source output = Input.fromFile("build/results-includeSinglePattern/output.xml").build()
        assertThat(output, evalXPath("count(/robot/suite/suite/test)", equalTo("1")))
        assertThat(output, hasXPath("/robot/suite/suite/test[@name = 'First Pattern Match']"))
    }

    @Test
    public void includeMultiplePattern() {
        Project project = ProjectBuilder.builder().withProjectDir(new File("").absoluteFile).build()
        project.pluginManager.apply 'org.roboscratch.robot'

        def  task = project.task("includeMultiplePattern", type: RunRobot) {
            outputdir = "build/results-includeMultiplePattern"
            data_sources = "src/test/acceptancetest/includeAndExclude"
            include = ["tobeincluded", "tobeincludedaswell"]
        }
        task.execute()

        Source output = Input.fromFile("build/results-includeMultiplePattern/output.xml").build()
        assertThat(output, evalXPath("count(/robot/suite/suite/test)", equalTo("2")))
        assertThat(output, hasXPath("/robot/suite/suite/test[@name = 'First Pattern Match']"))
        assertThat(output, hasXPath("/robot/suite/suite/test[@name = 'Second Pattern Match']"))
    }

    @Test
    public void excludeSinglePattern() {
        Project project = ProjectBuilder.builder().withProjectDir(new File("").absoluteFile).build()
        project.pluginManager.apply 'org.roboscratch.robot'

        def  task = project.task("excludeSinglePattern", type: RunRobot) {
            outputdir = "build/results-excludeSinglePattern"
            data_sources = "src/test/acceptancetest/includeAndExclude"
            exclude = ["tobeexcluded"]
        }
        task.execute()

        Source output = Input.fromFile("build/results-excludeSinglePattern/output.xml").build()
        assertThat(output, evalXPath("count(/robot/suite/suite/test)", equalTo("3")))
        assertThat(output, hasXPath("/robot/suite/suite/test[@name = 'Second Pattern Match']"))
        assertThat(output, hasXPath("/robot/suite/suite/test[@name = 'No Pattern Match']"))
        assertThat(output, hasXPath("/robot/suite/suite/test[@name = 'Without Tag']"))
    }

    @Test
    public void excludeMultiplePattern() {
        Project project = ProjectBuilder.builder().withProjectDir(new File("").absoluteFile).build()
        project.pluginManager.apply 'org.roboscratch.robot'

        def  task = project.task("excludeMultiplePattern", type: RunRobot) {
            outputdir = "build/results-excludeMultiplePattern"
            data_sources = "src/test/acceptancetest/includeAndExclude"
            exclude = ["tobeexcluded", "tobeexcludedaswell"]
        }
        task.execute()

        Source output = Input.fromFile("build/results-excludeMultiplePattern/output.xml").build()
        assertThat(output, evalXPath("count(/robot/suite/suite/test)", equalTo("2")))
        assertThat(output, hasXPath("/robot/suite/suite/test[@name = 'No Pattern Match']"))
        assertThat(output, hasXPath("/robot/suite/suite/test[@name = 'Without Tag']"))
    }

    @Test
    public void debugFile() {
        String debugPath = "debug.txt"
        String outputPath = "build/results-debugFile"

        Project project = ProjectBuilder.builder().withProjectDir(new File("").absoluteFile).build()
        project.pluginManager.apply 'org.roboscratch.robot'

        def  task = project.task("debugFile", type: RunRobot) {
            outputdir = outputPath
            data_sources = "src/test/acceptancetest/data_sources"
            debugfile = debugPath
        }
        task.execute()

        File debugFile = new File(outputPath, debugPath)
        assertTrue("Debug file not found at " + debugFile.getPath(), debugFile.exists())
        assertTrue("Debug file does not contain 'START SUITE: Data Sources'", debugFile.getText().contains("START SUITE: Data Sources"))
    }

    @Test
    public void outputPath() {
        Project project = ProjectBuilder.builder().withProjectDir(new File("").absoluteFile).build()
        project.pluginManager.apply 'org.roboscratch.robot'

        def  task = project.task("outputPath", type: RunRobot) {
            outputdir = "build/results-outputPath"
            data_sources = "src/test/acceptancetest/data_sources/test.robot"
            outputpath = "changed-output.xml"
        }
        task.execute()

        Source output = Input.fromFile("build/results-outputPath/changed-output.xml").build()
        assertThat(output, evalXPath("count(/robot/suite)", equalTo("1")))
        assertThat(output, not(hasXPath("/robot/suite/suite")))
    }

    @Test
    public void logFile() {
        String outputPath = "build/results-logFile"
        String logFileName = "changed-log.html"

        Project project = ProjectBuilder.builder().withProjectDir(new File("").absoluteFile).build()
        project.pluginManager.apply 'org.roboscratch.robot'

        def  task = project.task("logFile", type: RunRobot) {
            outputdir = outputPath
            data_sources = "src/test/acceptancetest/data_sources/test.robot"
            log = logFileName
        }
        task.execute()

        File logFile = new File(outputPath, logFileName)
        assertTrue("Log file not found at " + logFile.getPath(), logFile.exists())
        assertTrue("Log file does not contain '<html>'", logFile.getText().contains("<html>"))
    }

    @Test
    public void log() {
        String outputPath = "build/results-log"

        Project project = ProjectBuilder.builder().withProjectDir(new File("").absoluteFile).build()
        project.pluginManager.apply 'org.roboscratch.robot'

        def  task = project.task("log", type: RunRobot) {
            outputdir = outputPath
            data_sources = "src/test/acceptancetest/log/log.robot"
        }
        task.execute()

        File logFile = new File(outputPath, "log.html")
        assertTrue("Log file not found at " + logFile.getPath(), logFile.exists())
        assertTrue("Log file does not contain 'Foo42'", logFile.getText().contains("Foo42"))
    }
}
