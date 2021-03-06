package testit

import groovy.transform.CompileStatic
import groovy.util.Node

import java.text.DecimalFormat

import testit.ResultStatus
import testit.StepCategory
import testit.StepResult
import testit.TestResult

// JUnit 4 spec: http://llg.cubic.org/docs/junit/

class JUnitConverter implements Serializable {
    DecimalFormat timeFormatter = new DecimalFormat("#.###")

    @CompileStatic
    Node convertTestRunResult(TestRunResult result) {
        final suites = result.suites.collect { convertSuiteResult(it) }

        return new Node(null, "testsuites", [
            name: result.name,
            tests: result.getSuccessCount(),
            errors: result.getErrorCount(),
            failures: result.getFailureCount()
        ], suites)
    }

    @CompileStatic
    Node convertSuiteResult(SuiteResult result) {
        final tests = result.tests.collect { convertTestResult(it) }

        return new Node(null, "testsuite", [
            name: result.name,
            tests: tests.size(),
            errors: result.getErrorCount(),
            failures: result.getFailureCount()
        ], tests)
    }

    @CompileStatic
    Node convertTestResult(TestResult result) {
        final steps = result.steps.collect { convertStepResult(it) }
        final status = result.getStatus() == ResultStatus.Success ? "pass" : "fail"
        final time = result.getDurationInSeconds()
        final formattedTime = timeFormatter.format(time)

        return new Node(null, "testcase", [
            name: result.name,
            classname: result.classname,
            status: status,
            time: formattedTime
        ], steps)
    }

    @CompileStatic
    Node convertStepResult(StepResult result) {
        switch (result.category) {
            case StepCategory.Error:
                return convertError(result)
            case StepCategory.Failure:
                return convertFailure(result)
            case StepCategory.StandardError:
                return convertStandardError(result)
            case StepCategory.StandardOutput:
                return convertStandardOutput(result)
        }
    }

    @CompileStatic
    Node convertError(StepResult result) {
        return new Node(null, "error", [
            message: result.message,
            type: result.type
        ], result.trace)
    }

    @CompileStatic
    Node convertFailure(StepResult result) {
        return new Node(null, "failure", [
            message: result.message,
            type: result.type
        ], result.trace)
    }

    @CompileStatic
    Node convertStandardError(StepResult result) {
        return new Node(null, "system-err", null, result.message)
    }

    @CompileStatic
    Node convertStandardOutput(StepResult result) {
        return new Node(null, "system-out", null, result.message)
    }
}