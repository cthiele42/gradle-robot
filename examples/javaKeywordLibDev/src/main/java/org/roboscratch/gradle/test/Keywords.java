package org.roboscratch.gradle.test;

import org.robotframework.javalib.annotation.*;

@RobotKeywords
public class Keywords {
    @RobotKeyword("For testing ...")
    public String test() {
        return "test keyword called ...";
    }
}

