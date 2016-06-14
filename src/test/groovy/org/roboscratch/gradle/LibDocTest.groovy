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

import com.google.common.reflect.ClassPath
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

import static org.junit.Assert.*

/**
 * Created by cthiele on 12.06.16.
 */
class LibDocTest {
    @Test
    public void generateForBuiltInWithDefaultName() {
        Project project = ProjectBuilder.builder().withProjectDir(new File("").absoluteFile).build()
        project.pluginManager.apply 'org.roboscratch.robot'

        def  task = project.task("generateForBuiltInWithDefaultName", type: LibDoc) {
            addLib('BuiltIn')
        }
        task.execute()

        File docFile = new File('build/robot-libdoc', 'BuiltIn.html')
        assertTrue("Doc file not found at " + docFile.getPath(), docFile.exists())
        assertTrue("Doc file does not contain 'BuiltIn'", docFile.getText().contains("BuiltIn"))
    }

    @Test
    public void generateForBuiltInWithCustomName() {
        Project project = ProjectBuilder.builder().withProjectDir(new File("").absoluteFile).build()
        project.pluginManager.apply 'org.roboscratch.robot'

        def  task = project.task("generateForBuiltInWithCustomName", type: LibDoc) {
            addLib('BuiltIn', 'myBuiltIn.html')
        }
        task.execute()

        File docFile = new File('build/robot-libdoc', 'myBuiltIn.html')
        assertTrue("Doc file not found at " + docFile.getPath(), docFile.exists())
        assertTrue("Doc file does not contain 'BuiltIn'", docFile.getText().contains("BuiltIn"))
    }

    @Test
    public void generateForBuiltInWithCustomOutputdir() {
        Project project = ProjectBuilder.builder().withProjectDir(new File("").absoluteFile).build()
        project.pluginManager.apply 'org.roboscratch.robot'

        def  task = project.task("generateForBuiltInWithCustomOutputdir", type: LibDoc) {
            outputdir = 'build/mydocdir'
            addLib('BuiltIn')
        }
        task.execute()

        File docFile = new File('build/mydocdir', 'BuiltIn.html')
        assertTrue("Doc file not found at " + docFile.getPath(), docFile.exists())
        assertTrue("Doc file does not contain 'BuiltIn'", docFile.getText().contains("BuiltIn"))
    }

    @Test
    public void generateDocForAllProvided() {
        Project project = ProjectBuilder.builder().withProjectDir(new File("").absoluteFile).build()
        project.pluginManager.apply 'org.roboscratch.robot'

        def  task = project.task("generateDocForAllProvided", type: LibDoc) {
            addAllProvided()
        }
        task.execute()

        File docFile = new File('build/robot-libdoc', 'BuiltIn.html')
        assertTrue("Doc file not found at " + docFile.getPath(), docFile.exists())

        docFile = new File('build/robot-libdoc', 'Collections.html')
        assertTrue("Doc file not found at " + docFile.getPath(), docFile.exists())

        docFile = new File('build/robot-libdoc', 'DateTime.html')
        assertTrue("Doc file not found at " + docFile.getPath(), docFile.exists())

        docFile = new File('build/robot-libdoc', 'Dialogs.html')
        assertTrue("Doc file not found at " + docFile.getPath(), docFile.exists())

        docFile = new File('build/robot-libdoc', 'OperatingSystem.html')
        assertTrue("Doc file not found at " + docFile.getPath(), docFile.exists())

        docFile = new File('build/robot-libdoc', 'Process.html')
        assertTrue("Doc file not found at " + docFile.getPath(), docFile.exists())

        docFile = new File('build/robot-libdoc', 'Screenshot.html')
        assertTrue("Doc file not found at " + docFile.getPath(), docFile.exists())

        docFile = new File('build/robot-libdoc', 'String.html')
        assertTrue("Doc file not found at " + docFile.getPath(), docFile.exists())

        docFile = new File('build/robot-libdoc', 'Telnet.html')
        assertTrue("Doc file not found at " + docFile.getPath(), docFile.exists())

        docFile = new File('build/robot-libdoc', 'XML.html')
        assertTrue("Doc file not found at " + docFile.getPath(), docFile.exists())
    }

    @Test
    public void generateDocForJavaLibrary() {
        Project project = ProjectBuilder.builder().withProjectDir(new File("").absoluteFile).build()
        project.pluginManager.apply 'org.roboscratch.robot'

        def  task = project.task("generateDocForJavaLibrary", type: LibDoc) {
            addLib('FirstRobotLibrary')
        }
        task.execute()

        File docFile = new File('build/robot-libdoc', 'FirstRobotLibrary.html')
        assertTrue("Doc file not found at " + docFile.getPath(), docFile.exists())
        assertTrue("Doc file does not contain 'First Robot Library'", docFile.getText().contains("First Robot Library"))
    }

    @Test
    public void generateDocForAllJavaLibraries() {
        Project project = ProjectBuilder.builder().withProjectDir(new File("").absoluteFile).build()
        project.pluginManager.apply 'org.roboscratch.robot'

        def  task = project.task("generateDocForAllJavaLibraries", type: LibDoc) {
            addAllJavaLibsFromClasspath()
        }
        task.execute()

        File docFile = new File('build/robot-libdoc', 'FirstRobotLibrary.html')
        assertTrue("Doc file not found at " + docFile.getPath(), docFile.exists())
        assertTrue("Doc file does not contain 'First Robot Library'", docFile.getText().contains("First Robot Library"))

        docFile = new File('build/robot-libdoc', 'SecondRobotLibrary.html')
        assertTrue("Doc file not found at " + docFile.getPath(), docFile.exists())
        assertTrue("Doc file does not contain 'Second Robot Library'", docFile.getText().contains("Second Robot Library"))
    }

    @Test
    public void generateDocForResource() {
        Project project = ProjectBuilder.builder().withProjectDir(new File("").absoluteFile).build()
        project.pluginManager.apply 'org.roboscratch.robot'

        def  task = project.task("generateDocForResource", type: LibDoc) {
            addResource('src/test/robot-resources/keywordlib1.robot')
        }
        task.execute()

        File docFile = new File('build/robot-libdoc', 'keywordlib1.html')
        assertTrue("Doc file not found at " + docFile.getPath(), docFile.exists())
        assertTrue("Doc file does not contain 'First Keyword from Lib1'", docFile.getText().contains("First Keyword from Lib1"))
    }

    @Test
    public void generateDocForResourceWithCustomName() {
        Project project = ProjectBuilder.builder().withProjectDir(new File("").absoluteFile).build()
        project.pluginManager.apply 'org.roboscratch.robot'

        def  task = project.task("generateDocForResourceWithCustomName", type: LibDoc) {
            addResource('src/test/robot-resources/keywordlib1.robot', 'KeywordLibrary1.html')
        }
        task.execute()

        File docFile = new File('build/robot-libdoc', 'KeywordLibrary1.html')
        assertTrue("Doc file not found at " + docFile.getPath(), docFile.exists())
        assertTrue("Doc file does not contain 'First Keyword from Lib1'", docFile.getText().contains("First Keyword from Lib1"))
    }

    @Test
    public void generateDocForAllResourcesInDirectory() {
        Project project = ProjectBuilder.builder().withProjectDir(new File("").absoluteFile).build()
        project.pluginManager.apply 'org.roboscratch.robot'

        def  task = project.task("generateDocForAllResourcesInDirectory", type: LibDoc) {
            addAllResourcesFromDirectory('src/test/robot-resources')
        }
        task.execute()

        File docFile = new File('build/robot-libdoc', 'keywordlib1.html')
        assertTrue("Doc file not found at " + docFile.getPath(), docFile.exists())
        assertTrue("Doc file does not contain 'First Keyword from Lib1'", docFile.getText().contains("First Keyword from Lib1"))

        docFile = new File('build/robot-libdoc', 'keywordlib2.html')
        assertTrue("Doc file not found at " + docFile.getPath(), docFile.exists())
        assertTrue("Doc file does not contain 'First Keyword from Lib2'", docFile.getText().contains("First Keyword from Lib2"))
    }
}
