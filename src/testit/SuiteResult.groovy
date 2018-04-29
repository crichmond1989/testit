package testit

import testit.TestResult

class SuiteResult implements Serializable {
    String name
    List<TestResult> tests
}