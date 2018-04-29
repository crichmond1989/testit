package testit

import groovy.util.Node

import testit.StepCategory
import testit.StepResult
import testit.TestResult

class JUnitConverter implements Serializable {
    Node ConvertSuiteResult(SuiteResult result) {
        final tests = result.tests.collect { ConvertTestResult(it) }

        return new Node(null, "testsuite", [
            name: result.name,
            tests: tests.size()
        ], tests)
    }

    Node ConvertTestResult(TestResult result) {
        final steps = result.steps.collect { ConvertStepResult(it) }

        return new Node(null, "testcase", [
            name: result.name,
            classname: result.classname,
            status: result.getStatus()
        ], steps)
    }

    Node ConvertStepResult(StepResult result) {
        switch(result.category) {
            case StepCategory.Error:
                return ConvertError(result)
            case StepCategory.Failure:
                return ConvertFailure(result)
            case StepCategory.StandardError:
                return ConvertStandardError(result)
            case StepCategory.StandardOutput:
                return ConvertStandardOutput(result)
        }
    }

    Node ConvertError(StepResult result) {
        return new Node(null, "error", [
            message: result.message,
            type: result.type
        ])
    }

    Node ConvertFailure(StepResult result) {
        return new Node(null, "failure", [
            message: result.message,
            type: result.type
        ])
    }

    Node ConvertStandardError(StepResult result) {
        return new Node(null, "system-err", null, result.message)
    }

    Node ConvertStandardOutput(StepResult result) {
        return new Node(null, "system-out", null, result.message)
    }
}