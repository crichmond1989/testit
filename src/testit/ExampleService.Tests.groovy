package testit

import groovy.lang.Script
import org.junit.Assert

import testit.ExampleService
import testit.FunTest
import testit.UnitTest

class ExampleServiceTests implements Serializable {
    Script script

    @FunTest
    void smokeTest() {
        final service = new ExampleService(script)

        service.build()
    }

    @FunTest
    void ensureIsReleaseWhenMaster() {
        final service = new ExampleService(script)

        service.build("master")

        Assert.assertEquals(script.env.isRelease, true)
    }

    @FunTest
    void ensureIsNotReleaseWhenNotMaster() {
        final service = new ExampleService(script)

        service.build("dev")

        Assert.assertEquals(script.env.isRelease, false)
    }

    @UnitTest
    void shouldPass() {
    }

    @UnitTest
    void shouldFail() {
        Assert.fail("hey it failed")
    }
}