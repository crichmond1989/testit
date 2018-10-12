package testit

import groovy.transform.CompileStatic

import testit.SuiteResult

class TestRunResult implements Serializable {
    String name
    List<SuiteResult> suites

    @CompileStatic
    String getStatusLog() {
        final errors = errorCount
        final failures = failureCount
        final success = successCount

        final total = errors + failures + success

        return "Total tests: $total. Passed: $success. Failed: $failures. Errored: $errors"
    }

    @CompileStatic
    int getErrorCount() {
        return (int) suites?.collect { it.getErrorCount() }.sum() ?: 0
    }

    @CompileStatic
    int getFailureCount() {
        return (int) suites?.collect { it.getFailureCount() }.sum() ?: 0
    }

    @CompileStatic
    int getSuccessCount() {
        return (int) suites?.collect { it.getSuccessCount() }.sum() ?: 0
    }
}