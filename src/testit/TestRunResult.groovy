package testit

import testit.SuiteResult

class TestRunResult implements Serializable {
    String name
    List<SuiteResult> suites

    int getErrorCount() {
        return suites?.collect { it.getErrorCount() }.sum() ?: 0
    }

    int getFailureCount() {
        return suites?.collect { it.getFailureCount() }.sum() ?: 0
    }

    int getSuccessCount() {
        return suites?.collect { it.getSuccessCount() }.sum() ?: 0
    }
}