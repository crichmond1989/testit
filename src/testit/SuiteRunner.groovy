package testit

import java.lang.annotation.Annotation

import testit.StepResult
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
        
        return new SuiteResult(name: source.class.getName(), tests: tests)
    }

    TestResult setup(Object source) {
        return invokeByAnnotation(source, SuiteSetup.class)
    }

    TestResult teardown(Object source) {
        return invokeByAnnotation(source, SuiteTeardown.class)
    }

    TestResult invokeByAnnotation(Object source, Class<? extends Annotation> annotation) {
        final method = source.class.getDeclaredMethods().find { it.isAnnotationPresent(annotation) }

        if (!method)
            return

        final result = new TestResult(
            classname: source.class.getName(),
            name: annotation.getSimpleName()
        )

        try {
            final methodName = method.getName()

            result.steps += StepResult.wrote("method name: $methodName")

            source."$methodName"()
        } catch(Throwable error) {
            result.steps += StepResult.errored(error)
        }

        return result
    }
}