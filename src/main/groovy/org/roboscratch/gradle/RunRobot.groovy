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

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.GradleException

import org.robotframework.RobotFramework

class RunRobot extends DefaultTask {
    def outputdir = "build/robot-results"
    def data_sources = "src/test/acceptancetest"
    def variablefiles = null
    def variables = null
    def ignoreFailures = false
    def suites = null
    def suitename = null
    def suitedoc = null
    def metadata = null
    def tags = null
    def tests = null
    def rerunfailed = null
    def include = null
    def exclude = null

/*
    -c, --critical <tag>
    Tests that have the given tag are considered critical.
    -n, --noncritical <tag>
    Tests that have the given tag are not critical.
    -o, --output <file>
    Sets the path to the generated output file.
    -l, --log <file>
    Sets the path to the generated log file.
    -r, --report <file>
    Sets the path to the generated report file.
    -x, --xunit <file>
    Sets the path to the generated xUnit compatible result file.
    --xunitskipnoncritical
    Mark non-critical tests on xUnit compatible result file as skipped.
    -b, --debugfile <file>
    A debug file that is written during execution.
    -T, --timestampoutputs
    Adds a timestamp to all output files.
    --splitlog 	Split log file into smaller pieces that open in browser transparently.
    --logtitle <title>
    Sets a title for the generated test log.
    --reporttitle <title>
    Sets a title for the generated test report.
    --reportbackground <colors>
    Sets background colors of the generated report.
    -L, --loglevel <level>
    Sets the threshold level for logging. Optionally the default visible log level can be given separated with a colon (:).
    --suitestatlevel <level>
    Defines how many levels to show in the Statistics by Suite table in outputs.
    --tagstatinclude <tag>
    Includes only these tags in the Statistics by Tag table.
    --tagstatexclude <tag>
    Excludes these tags from the Statistics by Tag table.
    --tagstatcombine <tags:title>
    Creates combined statistics based on tags.
    --tagdoc <pattern:doc>
    Adds documentation to the specified tags.
    --tagstatlink <pattern:link:title>
    Adds external links to the Statistics by Tag table.
    --removekeywords <all|passed|name:pattern|tag:pattern|for|wuks>
    Removes keyword data from the generated log file.
    --flattenkeywords <for|foritem|name:pattern|tag:pattern>
    Flattens keywords in the generated log file.
    --listener <name:args>
    Sets a listener for monitoring test execution.
    --warnonskippedfiles
    Show a warning when an invalid file is skipped.
    --nostatusrc 	Sets the return code to zero regardless of failures in test cases. Error codes are returned normally.
    --runemptysuite
    Executes tests also if the selected test suites are empty.
    --dryrun 	In the dry run mode tests are run without executing keywords originating from test libraries. Useful for validating test data syntax.
    --exitonfailure
    Stops test execution if any critical test fails.
    --exitonerror 	Stops test execution if any error occurs when parsing test data, importing libraries, and so on.
    --skipteardownonexit
    Skips teardowns is test execution is prematurely stopped.
    --prerunmodifier <name:args>
    Activate programmatic modification of test data.
    --prerebotmodifier <name:args>
    Activate programmatic modification of results.
    --randomize <all|suites|tests|none>
    Randomizes test execution order.
    --console <verbose|dotted|quiet|none>
    Console output type.
    --dotted 	Shortcut for --console dotted.
    --quiet 	Shortcut for --console quiet.
    -W, --consolewidth <width>
    Sets the width of the console output.
    -C, --consolecolors <auto|on|ansi|off>
    Specifies are colors used on the console.
    -K, --consolemarkers <auto|on|off>
    Show markers on the console when top level keywords in a test case end.
    -P, --pythonpath <path>
    Additional locations to add to the module search path.
    -E, --escape <what:with>
    Escapes characters that are problematic in the console.
    -A, --argumentfile <path>
    A text file to read more arguments from.
*/


    def rc = -1

    def parseArrayArg(arguments, argToParse, optStr) {
        if(argToParse != null) {
            if(argToParse instanceof String) {
                arguments += optStr
                arguments += argToParse
            } else {
                argToParse.each {
                    arguments += optStr
                    arguments += it
                }
            }
        }
        return arguments
    }


    @TaskAction
    def run() {
        def arguments = ["-d", outputdir]
        arguments = parseArrayArg(arguments, variablefiles, "-V")
        arguments = parseArrayArg(arguments, variables, "-v")
        arguments = parseArrayArg(arguments, suites, "-s")
        if(suitename != null) {
            arguments += "-N"
            arguments += suitename
        }
        if(suitedoc != null) {
            arguments += "-D"
            arguments += suitedoc

        }
        arguments = parseArrayArg(arguments, metadata, "-M")
        arguments = parseArrayArg(arguments, tags, "-G")
        arguments = parseArrayArg(arguments, tests, "-t")
        if(rerunfailed != null) {
            arguments += "-R"
            arguments += rerunfailed
        }
        arguments = parseArrayArg(arguments, include, "-i")
        arguments = parseArrayArg(arguments, exclude, "-e")

        arguments += data_sources

        rc = RobotFramework.run((String[])arguments)
        if(!ignoreFailures && rc != 0) {
            throw new GradleException("Test failed with return code " + rc);
        }
    }
}
