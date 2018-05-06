package testit

import groovy.util.Node

import testit.StepCategory
import testit.StepResult
import testit.TestResult

class JUnitConverter implements Serializable {
    Node convertTestRunResult(TestRunResult result) {
        final suites = result.suites.collect { convertSuiteResult(it) }

        return new Node(null, "testsuites", [name: result.name], suites)
    }

    Node convertSuiteResult(SuiteResult result) {
        final tests = result.tests.collect { convertTestResult(it) }

        return new Node(null, "testsuite", [
            name: result.name,
            tests: tests.size()
        ], tests)
    }

    Node convertTestResult(TestResult result) {
        final steps = result.steps.collect { convertStepResult(it) }

        return new Node(null, "testcase", [
            name: result.name,
            classname: result.classname,
            status: result.getStatus(),
            time: result.getDurationInSeconds()
        ], steps)
    }

    Node convertStepResult(StepResult result) {
        switch(result.category) {
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

    Node convertError(StepResult result) {
        return new Node(null, "error", [
            message: result.message,
            type: result.type
        ], result.trace)
    }

    Node convertFailure(StepResult result) {
        return new Node(null, "failure", [
            message: result.message,
            type: result.type
        ], result.trace)
    }

    Node convertStandardError(StepResult result) {
        return new Node(null, "system-err", null, result.message)
    }

    Node convertStandardOutput(StepResult result) {
        return new Node(null, "system-out", null, result.message)
    }
}