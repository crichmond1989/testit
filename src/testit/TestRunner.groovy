package testit

@Grab('junit:junit:4.12')

import java.lang.annotation.Annotation
import org.junit.After
import org.junit.Before

import testit.StepResult
import testit.TestResult

class TestRunner implements Serializable {
    TestResult testRun(Object source, String method) {
        final result = new TestResult(
            classname: source.class.getName(),
            name: method
        )

        final setupResult = testSetup(source)

        if (setupResult)
            result.steps += setupResult

        if (result.getStatus() == "fail")
            return result

        final bodyResult = testBody(source, method)

        if (bodyResult)
            result.steps += bodyResult

        final teardownResult = testTeardown(source)

        if (teardownResult)
            result.steps += teardownResult

        return result
    }

    StepResult testSetup(Object source) {
        return testUtility(source, Before.class)
    }

    StepResult testBody(Object source, String method) {
        try {
            source."$method"()
        } catch (AssertionError error) {
            return StepResult.Failure(error)
        } catch (Throwable error) {
            return StepResult.Errored(error)
        }
    }

    StepResult testTeardown(Object source) {
        return testUtility(source, After.class)
    }

    StepResult testUtility(Object source, Class<? extends Annotation> annotation) {
        final method = source.class.getDeclaredMethods().find { it.isAnnotationPresent(annotation) }

        if (!method)
            return

        try {
            method.invoke(source)
        } catch(Throwable error) {
            return StepResult.Errored(error)
        }
    }
}