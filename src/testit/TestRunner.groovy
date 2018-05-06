package testit

import java.lang.annotation.Annotation
import java.util.Date

import testit.StepResult
import testit.TestResult
import testit.TestSetup
import testit.TestTeardown

class TestRunner implements Serializable {
    TestResult run(Object source, String method) {
        final result = new TestResult(
            classname: source.class.getName(),
            name: method
        )

        result.recordStart()

        final setupResult = setup(source)

        if (setupResult)
            result.steps += setupResult

        if (result.getStatus() == "fail") {
            result.recordEnd()
            return result
        }

        final bodyResults = invokeTestMethod(source, method)

        if (bodyResults)
            result.steps += bodyResults

        final teardownResult = teardown(source)

        if (teardownResult)
            result.steps += teardownResult

        result.recordEnd()
        return result
    }

    List<StepResult> invokeTestMethod(Object source, String method) {
        def catchResult
        
        try {
            source."$method"()
        } catch (AssertionError error) {
            catchResult = StepResult.failed(error)
        } catch (Throwable error) {
            catchResult = StepResult.errored(error)
        }

        final results = []

        if (catchResult)
            results += catchResult

        return results
    }

    StepResult setup(Object source) {
        return invokeByAnnotation(source, TestSetup.class)
    }

    StepResult teardown(Object source) {
        return invokeByAnnotation(source, TestTeardown.class)
    }

    StepResult invokeByAnnotation(Object source, Class<? extends Annotation> annotation) {
        final method = source.class.getDeclaredMethods().find { it.isAnnotationPresent(annotation) }?.getName()

        if (!method)
            return

        try {
            source."$method"()
        } catch(Throwable error) {
            return StepResult.errored(error)
        }
    }
}