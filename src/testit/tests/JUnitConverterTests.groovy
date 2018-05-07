package testit.tests

import java.text.DecimalFormat
import java.util.Date

import testit.JUnitConverter
import testit.StepCategory
import testit.StepResult
import testit.SuiteResult
import testit.Test
import testit.TestResult
import testit.TestRunner
import testit.TestRunResult
import testit.TestSetup

// JUnit 4 spec: http://llg.cubic.org/docs/junit/

class JUnitConverterTests implements Serializable {
    final converter = new JUnitConverter()

    final errorResult = new StepResult(
        category: StepCategory.Error,
        message: "error message",
        type: "java.lang.AssertionError",
        trace: "It failed at some line"
    )
    
    final failureResult = new StepResult(
        category: StepCategory.Failure,
        message: "failure message",
        type: "java.lang.Exception",
        trace: "It errored at some line"
    )

    final failureTestResult = new TestResult(
        classname: "testit.something",
        name: "test",
        start: new Date(),
        end: new Date().plus(1),
        steps: [failureResult]
    )

    final standardErrorResult = new StepResult(
        category: StepCategory.StandardError,
        message: "standard error"
    )

    final standardOutputResult = new StepResult(
        category: StepCategory.StandardOutput,
        message: "standard output"
    )

    final suiteResult = new SuiteResult(
        name: "suite",
        tests: [failureTestResult]
    )

    final testRunResult = new TestRunResult(
        name: "test run",
        suites: [suiteResult]
    )

    @Test
    void convertTestRunResult_correctTag() {
        final result = converter.convertTestRunResult(testRunResult)

        assert result.name() == "testsuites"
    }

    @Test
    void convertTestRunResult_correctInnerNode() {
        final result = converter.convertTestRunResult(testRunResult)

        assert result.children()[0].name() == "testsuite"
    }

    @Test
    void convertTestRunResult_correctName() {
        final result = converter.convertTestRunResult(testRunResult)

        assert result.attribute("name") == testRunResult.name
    }

    @Test
    void convertSuiteResult_correctTag() {
        final result = converter.convertSuiteResult(suiteResult)

        assert result.name() == "testsuite"
    }

    @Test
    void convertSuiteResult_correctInnerNode() {
        final result = converter.convertSuiteResult(suiteResult)

        assert result.children()[0].name() == "testcase"
    }

    @Test
    void convertSuiteResult_correctName() {
        final result = converter.convertSuiteResult(suiteResult)

        assert result.attribute("name") == suiteResult.name
    }

    @Test
    void convertSuiteResult_correctTestCount() {
        final result = converter.convertSuiteResult(suiteResult)

        assert result.attribute("tests") == suiteResult.tests.size()
    }

    @Test
    void convertTestResult_correctTag() {
        final result = converter.convertTestResult(failureTestResult)

        assert result.name() == "testcase"
    }

    @Test
    void convertTestResult_correctInnerNode() {
        final result = converter.convertTestResult(failureTestResult)

        assert result.children()[0].name() == "failure"
    }

    @Test
    void convertTestResult_correctClassname() {
        final result = converter.convertTestResult(failureTestResult)

        assert result.attribute("classname") == failureTestResult.classname
    }

    @Test
    void convertTestResult_correctName() {
        final result = converter.convertTestResult(failureTestResult)

        assert result.attribute("name") == failureTestResult.name
    }

    @Test
    void convertTestResult_correctStatus() {
        final result = converter.convertTestResult(failureTestResult)

        assert result.attribute("status") == "fail"
    }

    @Test
    void convertTestResult_correctTime() {
        final result = converter.convertTestResult(failureTestResult)
        final expectedTime = (failureTestResult.end.getTime() - failureTestResult.start.getTime()) / 1000
        final expectedFormattedTime = converter.timeFormatter.format(expectedTime)

        assert result.attribute("time") == expectedFormattedTime
    }
    
    @Test
    void convertFailure_correctTag() {
        final result = converter.convertFailure(failureResult)

        assert result.name() == "failure"
    }

    @Test
    void convertFailure_correctText() {
        final result = converter.convertFailure(failureResult)

        assert result.text() == failureResult.trace
    }

    @Test
    void convertFailure_correctMessage() {
        final result = converter.convertFailure(failureResult)

        assert result.attribute("message") == failureResult.message
    }

    @Test
    void convertFailure_correctType() {
        final result = converter.convertFailure(failureResult)

        assert result.attribute("type") == failureResult.type
    }

    @Test
    void convertStandardError_correctTag() {
        final result = converter.convertStandardError(standardErrorResult)

        assert result.name() == "system-err"
    }

    @Test
    void convertStandardError_correctText() {
        final result = converter.convertStandardError(standardErrorResult)

        assert result.text() == standardErrorResult.message
    }
    
    @Test
    void convertStandardOutput_correctTag() {
        final result = converter.convertStandardOutput(standardOutputResult)

        assert result.name() == "system-out"
    }

    @Test
    void convertStandardOutput_correctText() {
        final result = converter.convertStandardOutput(standardOutputResult)

        assert result.text() == standardOutputResult.message
    }
}