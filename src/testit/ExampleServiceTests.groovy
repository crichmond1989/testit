package testit

import groovy.lang.Script
import org.junit.Assert

import testit.ExampleService
import testit.FunTest
import testit.MockScript
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

        Assert.assertEquals("true", script.env.isRelease)
    }

    @FunTest
    void ensureIsNotReleaseWhenNotMaster() {
        final service = new ExampleService(script)

        service.build("dev")

        Assert.assertEquals("false", script.env.isRelease)
    }

    @UnitTest
    void unitSmokeTest() {
        final mock = new MockScript()

        mock.metaClass.env = []
        mock.metaClass.git = { _ -> }
        mock.metaClass.sh = { _ -> }

        final service = new ExampleService(mock)

        service.build()

        Assert.assertEquals("true", mock.env.isRelease)
    }
}