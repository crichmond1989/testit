package testit

import groovy.transform.CompileStatic

import testit.ResultStatus
import testit.TestResult

class SuiteResult implements Serializable {
    String name
    List<TestResult> tests

    @CompileStatic
    int getErrorCount() {
        return tests?.findAll { it.getStatus() == ResultStatus.Error }.size() ?: 0
    }

    @CompileStatic
    int getFailureCount() {
        return tests?.findAll { it.getStatus() == ResultStatus.Failure }.size() ?: 0
    }

    @CompileStatic
    int getSuccessCount() {
        return tests?.findAll { it.getStatus() == ResultStatus.Success }.size() ?: 0
    }
}