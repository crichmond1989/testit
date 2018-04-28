package org.crichmond1989

import groovy.lang.Script
import org.junit.runner.JUnitCore

import org.crichmond1989.BuildServiceTests

class TestService implements Serializable {
    Script script

    TestService(Script script) {
        this.script = script
    }

    void execute() {
        System.properties.script = script

        final result = JUnitCore.run(
            BuildServiceTests.class
        )

        script.println(result.wasSuccessful())
    }
}