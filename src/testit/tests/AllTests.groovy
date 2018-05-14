package testit.tests

import groovy.transform.CompileStatic

import testit.tests.JUnitConverterTests
import testit.tests.SuiteRunnerTests
import testit.tests.TestRunnerTests

@CompileStatic
class AllTests implements Serializable {
    List<Object> getSources() {
        return [
            new JUnitConverterTests(),
            new SuiteRunnerTests(),
            new TestRunnerTests()
        ]
    }
}