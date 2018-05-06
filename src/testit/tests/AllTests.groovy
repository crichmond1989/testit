package testit.tests

import testit.tests.TestRunnerTests

class AllTests implements Serializable {
    List getSources() {
        return [
            new TestRunnerTests()
        ]
    }
}