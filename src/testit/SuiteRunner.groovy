package testit

import groovy.transform.CompileStatic

import java.lang.annotation.Annotation

import testit.Logger
import testit.ReflectionUtils
import testit.StepResult
import testit.Suite
import testit.SuiteResult
import testit.SuiteSetup
import testit.SuiteTeardown
import testit.Test
import testit.TestResult
import testit.TestRunner

class SuiteRunner implements Serializable {
    Logger logger
    TestRunner testRunner

    TestRunner getTestRunner() {
        final runner = this.@testRunner ?: new TestRunner()

        runner.logger = this.logger

        return runner
    }

    @CompileStatic
    SuiteResult run(Object source) {
        List<TestResult> tests = []

        final suiteName = getSuiteName(source)

        this.logger?.logSuiteName(suiteName)

        final setupResult = setup(source)

        if (setupResult) {
            tests += setupResult
            this.logger?.logTestResult("Suite Setup", setupResult)
        }

        final testMethods = source.class.getDeclaredMethods().findAll { it.isAnnotationPresent(Test.class) }.collect { it.getName() }
        
        tests += testMethods.collect { this.testRunner.run(source, it) }

        final teardownResult = teardown(source)

        if (teardownResult) {
            tests += teardownResult
            this.logger?.logTestResult("Suite Teardown", teardownResult)
        }
        
        return new SuiteResult(name: suiteName, tests: tests)
    }

    @CompileStatic
    String getSuiteName(Object source) {
        final suite = source.class.getAnnotation(Suite.class)

        if (suite?.name())
            return suite.name()

        final suiteName = source.class.getDeclaredMethods().find { it.isAnnotationPresent(SuiteName.class) }?.getName()

        if (suiteName)
            return ReflectionUtils.invokeStringMethod(source, suiteName)

        return source.class.getName()
    }

    @CompileStatic
    TestResult setup(Object source) {
        return invokeByAnnotation(source, SuiteSetup.class)
    }

    @CompileStatic
    TestResult teardown(Object source) {
        return invokeByAnnotation(source, SuiteTeardown.class)
    }

    @CompileStatic
    TestResult invokeByAnnotation(Object source, Class<? extends Annotation> annotation) {
        final method = source.class.getDeclaredMethods().find { it.isAnnotationPresent(annotation) }?.getName()

        if (!method)
            return null

        final result = new TestResult(
            classname: this.testRunner.getClassname(source),
            name: annotation.getSimpleName()
        )

        result.recordStart()

        try {
            ReflectionUtils.invokeMethod(source, method)
            result.steps += StepResult.completed()
        } catch(Throwable error) {
            result.steps += StepResult.errored(error)
        }

        result.recordEnd()
        return result
    }
}