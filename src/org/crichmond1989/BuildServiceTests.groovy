package org.crichmond1989

import groovy.lang.Script
import org.junit.Assert
import org.junit.Test

import org.crichmond1989.BuildService
import org.crichmond1989.FunTest

class BuildServiceTests implements Serializable {
    Script script

    BuildServiceTests(Script script = null) {
        this.script = script
    }

    @FunTest
    void smokeTest() {
        final service = new BuildService(script)

        service.buildHelloNetCore()
    }

    @FunTest
    void ensureIsReleaseWhenMaster() {
        final service = new BuildService(script)

        service.buildHelloNetCore("master")

        Assert.assertEquals(script.env.isRelease, true)
    }

    @FunTest
    void ensureIsNotReleaseWhenNotMaster() {
        final service = new BuildService(script)

        service.buildHelloNetCore("dev")

        Assert.assertEquals(script.env.isRelease, false)
    }

    @FunTest
    void smokeTestDummy() {
        final service = new BuildService(script)

        service.dummy()
    }

    @Test
    void shouldPass() {
    }

    @Test
    void shouldFail() {
        Assert.fail("hey it failed")
    }
}