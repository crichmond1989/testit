package testit

import groovy.transform.CompileStatic

import java.lang.annotation.Annotation
import java.util.Date

import testit.ReflectionUtils
import testit.ResultStatus
import testit.StepResult
import testit.SuiteClassname
import testit.TestResult
import testit.TestSetup
import testit.TestTeardown

class TestRunner implements Serializable {
    @CompileStatic
    TestResult run(Object source, String method) {
        final result = new TestResult(
            classname: getClassname(source),
            name: method
        )

        result.recordStart()

        final setupResult = setup(source)

        if (setupResult)
            result.steps += setupResult

        if (result.getStatus() != ResultStatus.Success) {
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

    @CompileStatic
    String getClassname(Object source) {
        final suite = source.class.getAnnotation(Suite.class)

        if (suite?.classname())
            return suite.classname()

        final suiteClassname = source.class.getDeclaredMethods().find { it.isAnnotationPresent(SuiteClassname.class) }?.getName()

        if (suiteClassname)
            return ReflectionUtils.invokeStringMethod(source, suiteClassname)

        return source.class.getName()
    }

    @CompileStatic
    StepResult invokeTestMethod(Object source, String method) {
        def catchResult
        
        try {
            ReflectionUtils.invokeMethod(source, method)
            return null
        } catch (AssertionError error) {
            return StepResult.failed(error)
        } catch (Throwable error) {
            return StepResult.errored(error)
        }
    }

    @CompileStatic
    StepResult setup(Object source) {
        return invokeByAnnotation(source, TestSetup.class)
    }

    @CompileStatic
    StepResult teardown(Object source) {
        return invokeByAnnotation(source, TestTeardown.class)
    }

    @CompileStatic
    StepResult invokeByAnnotation(Object source, Class<? extends Annotation> annotation) {
        final method = source.class.getDeclaredMethods().find { it.isAnnotationPresent(annotation) }?.getName()

        if (!method)
            return null

        try {
            ReflectionUtils.invokeMethod(source, method)
            return null
        } catch (Throwable error) {
            return StepResult.errored(error)
        }
    }
}