package testit

import java.lang.Annotation
import org.junit.After
import org.junit.Before

import testit.TestResult

class TestRunner implements Serializable {
    TestResult testRun(Object source, String method) {
        final setupResult = testSetup(source)

        if (!setupResult.success)
            return setupResult

        final bodyResult = testBody(source, method)
        final teardownResult = testTeardown(source)

        if (!bodyResult.success)
            return bodyResult

        if (!teardownResult.success)
            return teardownResult

        return bodyResult
    }

    TestResult testSetup(Object source) {
        return testUtility(source, Before.class)
    }

    TestResult testBody(Object source, String method) {
        try {
            source."$method"()
        } catch (Throwable error) {
            return TestResult.Failed(error)
        }

        return TestResult.Passed()
    }

    TestResult testTeardown(Object source) {
        return testUtility(source, After.class)
    }

    TestResult testUtility(Object source, Class<? extends Annotation> annotation) {
        final method = source.class.getDeclaredMethods().find { it.isAnnotationPresent(annotation) }

        if (!method)
            return TestResult.Passed()

        try {
            method.invoke(source)
        } catch(Throwable error) {
            return TestResult.Failed(error)
        }

        return TestResult.Passed()
    }
}