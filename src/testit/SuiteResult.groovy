package testit

import testit.TestResult

class SuiteResult implements Serializable {
    String name
    Map<String, TestResult> tests
}