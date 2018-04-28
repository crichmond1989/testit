package org.crichmond1989

import groovy.lang.Script
import org.junit.Assert
import org.junit.Test

import org.crichmond1989.BuildService

class BuildServiceTests {
    Script script

    BuildServiceTests() {
        this.script = System.properties.script
    }

    @Test
    void smokeTest() {
        final service = new BuildService(script)

        service.buildHelloNetCore()
    }

    @Test
    void ensureIsReleaseWhenMaster() {
        final service = new BuildService(script)

        service.buildHelloNetCore("master")

        Assert.assertEquals(script.env.isRelease, true)
    }

    @Test
    void ensureIsNotReleaseWhenNotMaster() {
        final service = new BuildService(script)

        service.buildHelloNetCore("dev")

        Assert.assertEquals(script.env.isRelease, false)
    }

    @Test
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