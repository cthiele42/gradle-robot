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
        taskrerun.execute()

        Source output = Input.fromFile("build/results-rerunfailed/output.xml").build()
        assertThat(output, evalXPath("count(/robot/suite/suite/test)", equalTo("1")))
        assertThat(output, hasXPath("/robot/suite/suite/test[@name = 'Intentionally Failing']"))

    }
}
