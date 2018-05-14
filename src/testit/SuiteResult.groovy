package testit

import groovy.transform.CompileStatic

import testit.ResultStatus
import testit.TestResult

@CompileStatic
class SuiteResult implements Serializable {
    String name
    List<TestResult> tests

    int getErrorCount() {
        return tests?.findAll { it.getStatus() == ResultStatus.Error }.size() ?: 0
    }

    int getFailureCount() {
        return tests?.findAll { it.getStatus() == ResultStatus.Failure }.size() ?: 0
    }

    int getSuccessCount() {
        return tests?.findAll { it.getStatus() == ResultStatus.Success }.size() ?: 0
    }
}