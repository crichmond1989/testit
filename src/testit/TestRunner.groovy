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

        final bodyResults = testBody(source, method)

        if (bodyResults)
            result.steps += bodyResults

        final teardownResult = testTeardown(source)

        if (teardownResult)
            result.steps += teardownResult

        return result
    }

    StepResult testSetup(Object source) {
        return testUtility(source, Before.class)
    }

    List<StepResult> testBody(Object source, String method) {
        final testStdBuf = new ByteArrayOutputStream()
        final testOut = new PrintStream(testStdBuf)
        final originalOut = System.out

        final testErrBuf = new ByteArrayOutputStream()
        final testErr = new PrintStream(testErrBuf)
        final originalErr = System.err

        System.out = testOut
        System.err = testErr

        def catchResult
        
        try {
            source."$method"()
        } catch (AssertionError error) {
            catchResult = StepResult.Failed(error)
        } catch (Throwable error) {
            catchResult = StepResult.Errored(error)
        }

        System.out = originalOut
        System.err = originalErr

        final results = []

        final testOutString = testStdBuf.toString()

        if (testOutString)
            results += StepResult.Wrote(testOutString)

        final testErrString = testErrBuf.toString()

        if (testErrString)
            results += StepResult.WroteError(testErrString)

        if (catchResult)
            results += catchResult

        return results
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