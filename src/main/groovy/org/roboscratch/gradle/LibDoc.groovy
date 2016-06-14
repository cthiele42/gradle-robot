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
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.FileTree
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.robotframework.RobotFramework

class LibDoc extends DefaultTask {
    @OutputDirectory
    def outputdir = project.file('build/robot-libdoc')

    def setOutputdir(outputPath) {
        outputdir = project.file(outputPath)
    }

    def libs = [:]

    def addLib(libName, output=libName + '.html', enableUptodateCheck=false) {
        libs.put(libName, output)

        if(!enableUptodateCheck) {
            outputs.upToDateWhen {false}
        }
    }

    def addAllProvided() {
        addLib('BuiltIn')
        addLib('Collections')
        addLib('DateTime')
        addLib('Dialogs')
        addLib('OperatingSystem')
        addLib('Process')
        addLib('Screenshot')
        addLib('String')
        addLib('Telnet')
        addLib('XML')
    }

    def addAllJavaLibsFromClasspath() {
        ClassPath classPath = ClassPath.from(Thread.currentThread().contextClassLoader)
        for(ClassPath.ClassInfo classInfo: classPath.getTopLevelClasses('')) {
            Class clazz = Class.forName(classInfo.name)
            if(clazz != null) {
                clazz = clazz.superclass
                while(clazz != null) {
                    if("org.robotframework.javalib.library.AnnotationLibrary".equals(clazz.canonicalName)) {
                        addLib(classInfo.name)
                        break
                    }
                    clazz = clazz.superclass
                }
            }
        }
    }

    def addResource(resourcePath, output=new File(resourcePath).name.replace('.robot', '.html')) {
        addLib(project.file(resourcePath).absolutePath, output, true)

        inputs.file(resourcePath)
    }

    def addAllResourcesFromDirectory(directory) {
        FileTree resourceTree = project.fileTree(dir: directory, include: '**/*.robot')
        resourceTree.each {File file ->
            addLib(file.absolutePath, file.name.replace('.robot', '.html'), true)
        }

        inputs.dir(directory)
    }

    @TaskAction
    def run() {
        project.file(outputdir).mkdirs()

        for( e in libs) {
            def arguments = ["libdoc", e.key, new File(outputdir, e.value).absolutePath]
            def rc = RobotFramework.run((String[])arguments)
            if(rc != 0) {
                throw new GradleException("Generating libdoc for " + e.key + " failed with return code " + rc);
            }
        }

    }
}
