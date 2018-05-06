package testit.tests

import testit.StepCategory
import testit.Test
import testit.TestRunner
import testit.TestSetup

class TestRunnerTests {
    class SuccessfulAnnotation {
        @TestSetup
        void run() {}
    }

    class UnsuccessfulAnnotation {
        @TestSetup
        void run() {
            throw new Exception()
        }
    }

    class NoAnnotations {}

    @Test
    void invokeByAnnotation_missingAnnotation() {
        final runner = new TestRunner()
        final source = new NoAnnotations()

        final result = runner.invokeByAnnotation(source, TestSetup.class)

        assert !result
    }

    @Test
    void invokeByAnnotation_hasSuccessfulAnnotation() {
        final runner = new TestRunner()
        final source = new SuccessfulAnnotation()

        final result = runner.invokeByAnnotation(source, TestSetup.class)

        assert !result
    }

    @Test
    void invokeByAnnotation_hasUnsuccessfulAnnotation() {
        final runner = new TestRunner()
        final source = new UnsuccessfulAnnotation()

        final result = runner.invokeByAnnotation(source, TestSetup.class)

        assert result.category == StepCategory.Error
    }
}