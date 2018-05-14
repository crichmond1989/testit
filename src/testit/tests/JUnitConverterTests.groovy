package testit.tests

import groovy.transform.CompileStatic

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

import org.junit.Assert

// JUnit 4 spec: http://llg.cubic.org/docs/junit/

@CompileStatic
class JUnitConverterTests implements Serializable {
    JUnitConverter converter = new JUnitConverter()

    StepResult errorResult = new StepResult(
        category: StepCategory.Error,
        message: "error message",
        type: "java.lang.AssertionError",
        trace: "It failed at some line"
    )
    
    StepResult failureResult = new StepResult(
        category: StepCategory.Failure,
        message: "failure message",
        type: "java.lang.Exception",
        trace: "It errored at some line"
    )

    TestResult failureTestResult = new TestResult(
        classname: "testit.something",
        name: "test",
        start: new Date(),
        end: new Date().plus(1),
        steps: [failureResult]
    )

    StepResult standardErrorResult = new StepResult(
        category: StepCategory.StandardError,
        message: "standard error"
    )

    StepResult standardOutputResult = new StepResult(
        category: StepCategory.StandardOutput,
        message: "standard output"
    )

    SuiteResult suiteResult = new SuiteResult(
        name: "suite",
        tests: [failureTestResult]
    )

    TestRunResult testRunResult = new TestRunResult(
        name: "test run",
        suites: [suiteResult]
    )

    @Test
    void convertTestRunResult_correctTag() {
        final result = converter.convertTestRunResult(testRunResult)

        Assert.assertEquals("testsuites", result.name())
    }

    @Test
    void convertTestRunResult_correctInnerNode() {
        final result = converter.convertTestRunResult(testRunResult)
        final suite = (Node) result.children()[0]

        Assert.assertEquals("testsuite", suite.name())
    }

    @Test
    void convertTestRunResult_correctName() {
        final result = converter.convertTestRunResult(testRunResult)

        Assert.assertEquals(testRunResult.name, result.attribute("name"))
    }

    @Test
    void convertSuiteResult_correctTag() {
        final result = converter.convertSuiteResult(suiteResult)

        Assert.assertEquals("testsuite", result.name())
    }

    @Test
    void convertSuiteResult_correctInnerNode() {
        final result = converter.convertSuiteResult(suiteResult)
        final test = (Node) result.children()[0]

        Assert.assertEquals("testcase", test.name())
    }

    @Test
    void convertSuiteResult_correctName() {
        final result = converter.convertSuiteResult(suiteResult)

        Assert.assertEquals(suiteResult.name, result.attribute("name"))
    }

    @Test
    void convertSuiteResult_correctTestCount() {
        final result = converter.convertSuiteResult(suiteResult)

        Assert.assertEquals(suiteResult.tests.size(), result.attribute("tests"))
    }

    @Test
    void convertTestResult_correctTag() {
        final result = converter.convertTestResult(failureTestResult)

        Assert.assertEquals("testcase", result.name())
    }

    @Test
    void convertTestResult_correctInnerNode() {
        final result = converter.convertTestResult(failureTestResult)
        final failure = (Node) result.children()[0]

        Assert.assertEquals("failure", failure.name())
    }

    @Test
    void convertTestResult_correctClassname() {
        final result = converter.convertTestResult(failureTestResult)

        Assert.assertEquals(failureTestResult.classname, result.attribute("classname"))
    }

    @Test
    void convertTestResult_correctName() {
        final result = converter.convertTestResult(failureTestResult)

        Assert.assertEquals(failureTestResult.name, result.attribute("name"))
    }

    @Test
    void convertTestResult_correctStatus() {
        final result = converter.convertTestResult(failureTestResult)

        Assert.assertEquals("fail", result.attribute("status"))
    }

    @Test
    void convertTestResult_correctTime() {
        final result = converter.convertTestResult(failureTestResult)
        final expectedTime = (failureTestResult.end.getTime() - failureTestResult.start.getTime()) / 1000
        final expectedFormattedTime = converter.timeFormatter.format(expectedTime)

        Assert.assertEquals(expectedFormattedTime, result.attribute("time"))
    }
    
    @Test
    void convertFailure_correctTag() {
        final result = converter.convertFailure(failureResult)

        Assert.assertEquals("failure", result.name())
    }

    @Test
    void convertFailure_correctText() {
        final result = converter.convertFailure(failureResult)

        Assert.assertEquals(failureResult.trace, result.text())
    }

    @Test
    void convertFailure_correctMessage() {
        final result = converter.convertFailure(failureResult)

        Assert.assertEquals(failureResult.message, result.attribute("message"))
    }

    @Test
    void convertFailure_correctType() {
        final result = converter.convertFailure(failureResult)

        Assert.assertEquals(failureResult.type, result.attribute("type"))
    }

    @Test
    void convertStandardError_correctTag() {
        final result = converter.convertStandardError(standardErrorResult)

        Assert.assertEquals("system-err", result.name())
    }

    @Test
    void convertStandardError_correctText() {
        final result = converter.convertStandardError(standardErrorResult)

        Assert.assertEquals(standardErrorResult.message, result.text())
    }
    
    @Test
    void convertStandardOutput_correctTag() {
        final result = converter.convertStandardOutput(standardOutputResult)

        Assert.assertEquals("system-out", result.name())
    }

    @Test
    void convertStandardOutput_correctText() {
        final result = converter.convertStandardOutput(standardOutputResult)

        Assert.assertEquals(standardOutputResult.message, result.text())
    }
}