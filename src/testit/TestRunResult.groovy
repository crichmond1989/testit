package testit

import testit.SuiteResult

class TestRunResult implements Serializable {
    String name
    List<SuiteResult> suites

    int getErrorCount() {
        return (int) suites?.collect { it.getErrorCount() }.sum() ?: 0
    }

    int getFailureCount() {
        return (int) suites?.collect { it.getFailureCount() }.sum() ?: 0
    }

    int getSuccessCount() {
        return (int) suites?.collect { it.getSuccessCount() }.sum() ?: 0
    }
}