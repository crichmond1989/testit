package testit

import groovy.lang.Script

@Grab('junit:junit:4.12')
@Grab('org.hamcrest:hamcrest-core:1.3')
@Grab('org.hamcrest:hamcrest-library:1.3')
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

        assert script.env.isRelease == "true"
    }

    @FunTest
    void ensureIsNotReleaseWhenNotMaster() {
        final service = new ExampleService(script)

        service.build("dev")

        assert script.env.isRelease == "false"
    }

    @UnitTest
    void mockEnsureIsReleaseWhenMaster() {
        final mock = new MockScript()

        mock.metaClass.env = [:]
        mock.metaClass.git = { _ -> }
        mock.metaClass.sh = { _ -> }

        final service = new ExampleService(mock)

        service.build()

        assert mock.env.isRelease == "true"
    }

    @UnitTest
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