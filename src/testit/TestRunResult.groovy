package testit

import testit.SuiteResult

@Grab("com.cloudbees:groovy-cps:1.1")
import com.cloudbees.groovy.cps.NonCPS

class TestRunResult implements Serializable {
    String name
    List<SuiteResult> suites

    @NonCPS
    int getErrorCount() {
        return (int) suites?.collect { it.getErrorCount() }.sum() ?: 0
    }

    @NonCPS
    int getFailureCount() {
        return (int) suites?.collect { it.getFailureCount() }.sum() ?: 0
    }

    @NonCPS
    int getSuccessCount() {
        return (int) suites?.collect { it.getSuccessCount() }.sum() ?: 0
    }
}