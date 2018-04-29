package testit

import groovy.lang.Script
import org.junit.Assert

import testit.BuildService
import testit.FunTest
import testit.UnitTest

class ExampleServiceTests implements Serializable {
    Script script

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

    @UnitTest
    void shouldPass() {
    }

    @UnitTest
    void shouldFail() {
        Assert.fail("hey it failed")
    }
}