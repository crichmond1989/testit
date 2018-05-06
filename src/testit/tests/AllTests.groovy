package testit.tests

import testit.tests.TestRunnerTests

class AllTests implements Serializable {
    Object[] getSources() {
        return [
            new TestRunnerTests()
        ]
    }
}