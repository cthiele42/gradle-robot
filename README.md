# Gradle Plugin for the Robot Framework
A plugin for using the [Robot Framework](http://robotframework.org/) in the [Gradle](http://gradle.org/) build system. The Plugin is enabling the usage of Robot Framework
based acceptance tests out of the box with Gradle.
The plugin adds two tasks to the buildsystem.

## runrobot
The runrobot tasks executes Robot tests.
The task is utilizing the Gradle daemon to speed up the test execution. The typical delay on startup for the Jython
based Robot Framework execution is reduced to less than a second.
More can be found in the wiki [runrobot Task](https://github.com/cthiele42/gradle-robot/wiki/02-runrobot-Task)

##libdoc
The libdoc task is generating keyword library documentation for builtin libraries, Python and Java based libraries and for resources as well.
The libdoc task automatically generates the documentation for all Java keyword libraries found on classpath.
For resources the libdoc task can be started in continuous build mode automatically generating the documentation with each change of the input files.
More in the wiki [libdoc Task](https://github.com/cthiele42/gradle-robot/wiki/03-libdoc-Task)
