package testit

import testit.ResultStatus
import testit.TestResult

@Grab("com.cloudbees:groovy-cps:1.1")
import com.cloudbees.groovy.cps.NonCPS

class SuiteResult implements Serializable {
    String name
    List<TestResult> tests

    @NonCPS
    int getErrorCount() {
        return tests?.findAll { it.getStatus() == ResultStatus.Error }.size() ?: 0
    }

    @NonCPS
    int getFailureCount() {
        return tests?.findAll { it.getStatus() == ResultStatus.Failure }.size() ?: 0
    }

    @NonCPS
    int getSuccessCount() {
        return tests?.findAll { it.getStatus() == ResultStatus.Success }.size() ?: 0
    }
}