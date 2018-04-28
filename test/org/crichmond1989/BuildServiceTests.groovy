package org.crichmond1989

import groovy.lang.Script
import groovy.util.GroovyTestCase
import org.junit.Test

import org.crichmond1989.BuildService

class BuildServiceTests extends GroovyTestCase {
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

        assert script.env.isRelease
    }

    @Test
    void ensureIsNotReleaseWhenNotMaster() {
        final service = new BuildService(script)

        service.buildHelloNetCore("dev")

        assert !script.env.isRelease
    }
}