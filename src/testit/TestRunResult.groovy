package testit

import testit.SuiteResult

class TestRunResult implements Serializable {
    String name
    List<SuiteResult> suites
}