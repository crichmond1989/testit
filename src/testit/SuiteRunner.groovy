package testit

import java.lang.annotation.Annotation

import testit.StepResult
import testit.Suite
import testit.SuiteResult
import testit.SuiteSetup
import testit.SuiteTeardown
import testit.Test
import testit.TestResult
import testit.TestRunner

class SuiteRunner implements Serializable {
    TestRunner testRunner

    SuiteRunner(TestRunner testRunner = null) {
        this.testRunner = testRunner ?: new TestRunner()
    }

    SuiteResult run(Object source) {
        final tests = []

        final setupResult = setup(source)

        if (setupResult)
            tests += setupResult

        final testMethods = source.class.getDeclaredMethods().findAll { it.isAnnotationPresent(Test.class) }.collect { it.getName() }
        
        tests += testMethods.collect { testRunner.run(source, it) }

        final teardownResult = teardown(source)

        if (teardownResult)
            tests += teardownResult
        
        return new SuiteResult(name: getSuiteName(source), tests: tests)
    }

    String getSuiteName(Object source) {
        final suite = source.class.getAnnotation(Suite.class)

        return suite?.name() ?: source.class.getName()
    }

    TestResult setup(Object source) {
        return invokeByAnnotation(source, SuiteSetup.class)
    }

    TestResult teardown(Object source) {
        return invokeByAnnotation(source, SuiteTeardown.class)
    }

    TestResult invokeByAnnotation(Object source, Class<? extends Annotation> annotation) {
        final method = source.class.getDeclaredMethods().find { it.isAnnotationPresent(annotation) }?.getName()

        if (!method)
            return

        final result = new TestResult(
            classname: source.class.getName(),
            name: annotation.getSimpleName()
        )

        result.recordStart()

        try {
            source."$method"()
        } catch(Throwable error) {
            result.steps += StepResult.errored(error)
        }

        result.recordEnd()
        return result
    }
}