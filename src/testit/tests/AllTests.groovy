package testit.tests

import groovy.transform.CompileStatic

import testit.tests.JUnitConverterTests
import testit.tests.SuiteRunnerTests
import testit.tests.TestRunnerTests

@Grab("com.cloudbees:groovy-cps:1.1")
import com.cloudbees.groovy.cps.NonCPS

@CompileStatic
class AllTests implements Serializable {
    @NonCPS
    List<Object> getSources() {
        return [
            new JUnitConverterTests(),
            new SuiteRunnerTests(),
            new TestRunnerTests()
        ]
    }
}