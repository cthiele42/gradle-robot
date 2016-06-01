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
import org.xmlunit.builder.Input

import javax.xml.transform.Source

import static org.hamcrest.CoreMatchers.equalTo
import static org.junit.Assert.assertThat
import static org.xmlunit.matchers.EvaluateXPathMatcher.hasXPath as evalXPath
import static org.xmlunit.matchers.HasXPathMatcher.hasXPath

/**
 * Created by cthiele on 27.05.16.
 */
class RerunFailedTest {
    @Test
    public void rerunfailed() {
        Project project = ProjectBuilder.builder().withProjectDir(new File("").absoluteFile).build()
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
        taskrerun.execute()

        Source output = Input.fromFile("build/results-rerunfailed/output.xml").build()
        assertThat(output, evalXPath("count(/robot/suite/suite/test)", equalTo("1")))
        assertThat(output, hasXPath("/robot/suite/suite/test[@name = 'Intentionally Failing']"))

    }
}
