package testit.tests

import testit.tests.JUnitConverterTests
import testit.tests.TestRunnerTests

class AllTests implements Serializable {
    Object[] getSources() {
        return [
            new JUnitConverterTests(),
            new TestRunnerTests()
        ]
    }
}