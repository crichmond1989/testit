package testit

import groovy.lang.Script

import testit.ExampleService
import testit.MockScript
import testit.Suite
import testit.SuiteSetup
import testit.Test
import testit.TestTeardown

@Suite
class ExampleServiceTests implements Serializable {
    Script script

    @SuiteSetup
    void suiteSetup() {
        script.println("the suite is setup")
    }

    @TestTeardown
    void testTeardown() {
        script.println("test finished")
    }

    @Test
    void smokeTest() {
        final service = new ExampleService(script)

        service.build()
    }

    @Test
    void ensureIsReleaseWhenMaster() {
        final service = new ExampleService(script)

        service.build("master")

        assert script.env.isRelease == "true"
    }

    @Test
    void ensureIsNotReleaseWhenNotMaster() {
        final service = new ExampleService(script)

        service.build("dev")

        assert script.env.isRelease == "false"
    }

    @Test
    void mockEnsureIsReleaseWhenMaster() {
        final mock = new MockScript()

        mock.metaClass.env = [:]
        mock.metaClass.git = { _ -> }
        mock.metaClass.sh = { _ -> }

        final service = new ExampleService(mock)

        service.build()

        assert mock.env.isRelease == "true"
    }

    @Test
    void mockEnsureIsNotReleaseWhenNotMaster() {
        final mock = new MockScript()

        mock.metaClass.env = [:]
        mock.metaClass.git = { _ -> }
        mock.metaClass.sh = { _ -> }

        final service = new ExampleService(mock)

        service.build("dev")

        assert mock.env.isRelease == "false"
    }
}