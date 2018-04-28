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

        final jUnit = new JUnitCore()

        final result = jUnit.run(
            BuildServiceTests.class
        )

        if (result.wasSuccessful())
            script.println("SUCCESS!!!")
        else
            result.getFailures().each { script.println(it.dump()) }
    }
}