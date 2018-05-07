package testit.tests

import testit.StepCategory
import testit.Test
import testit.TestRunner
import testit.TestSetup
import testit.TestTeardown

class TestRunnerTests implements Serializable {
    class SuccessfulTestMethod {
        @Test
        void run() {}
    }
    
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

    class ErroredTestMethod {
        @Test
        void run() {
            throw new Exception()
        }
    }

    class FailedTestMethod {
        @Test
        void run() {
            assert 1 == 0
        }
    }

    class TrackStages {
        def invokedSetup = false
        def invokedRun = false
        def invokedTeardown = false

        @TestSetup
        void setup() {
            invokedSetup = true
        }

        @Test
        void run() {
            invokedRun = true
        }

        @TestTeardown
        void teardown() {
            invokedTeardown = true
        }
    }

    class TrackStagesSetupError {
        def invokedSetup = false
        def invokedRun = false
        def invokedTeardown = false

        @TestSetup
        void setup() {
            invokedSetup = true
            
            throw new Exception()
        }

        @Test
        void run() {
            invokedRun = true
        }

        @TestTeardown
        void teardown() {
            invokedTeardown = true
        }
    }

    class NoAnnotations {}

    final runner = new TestRunner()

    @Test
    void run_correctClassname() {
        final source = new SuccessfulTestMethod()
        final result = runner.run(source, "run")

        assert result.classname == source.class.getName()
    }

    @Test
    void run_correctName() {
        final source = new SuccessfulTestMethod()
        final result = runner.run(source, "run")

        assert result.name == "run"
    }

    @Test
    void run_statusPassOnSuccess() {
        final source = new SuccessfulTestMethod()
        final result = runner.run(source, "run")

        assert result.getStatus() == "pass"
    }

    @Test
    void run_statusFailOnError() {
        final source = new ErroredTestMethod()
        final result = runner.run(source, "run")

        assert result.getStatus() == "fail"
    }

    @Test
    void run_statusFailOnFailure() {
        final source = new FailedTestMethod()
        final result = runner.run(source, "run")

        assert result.getStatus() == "fail"
    }

    @Test
    void run_invokedSetup() {
        final source = new TrackStages()
        
        runner.run(source, "run")

        assert source.invokedSetup
    }

    @Test
    void run_invokedRun() {
        final source = new TrackStages()
        
        runner.run(source, "run")

        assert source.invokedRun
    }

    @Test
    void run_invokedTeardown() {
        final source = new TrackStages()
        
        runner.run(source, "run")

        assert source.invokedTeardown
    }

    @Test
    void run_skippedRunOnSetupError() {
        final source = new TrackStagesSetupError()

        runner.run(source, "run")

        assert !source.invokedRun
    }

    @Test
    void run_skippedTeardownOnSetupError() {
        final source = new TrackStagesSetupError()

        runner.run(source, "run")

        assert !source.invokedTeardown
    }

    @Test
    void invokeTestMethod_success() {
        final source = new SuccessfulTestMethod()
        final result = runner.invokeTestMethod(source, "run")

        assert !result
    }

    @Test
    void invokeTestMethod_error() {
        final source = new ErroredTestMethod()
        final result = runner.invokeTestMethod(source, "run")

        assert result.category == StepCategory.Error
    }

    @Test
    void invokeTestMethod_fail() {
        final source = new FailedTestMethod()
        final result = runner.invokeTestMethod(source, "run")

        assert result.category == StepCategory.Failure
    }

    @Test
    void setup_invoked() {
        final source = new TrackStages()
        
        runner.setup(source)

        assert source.invokedSetup
    }

    @Test
    void teardown_invoked() {
        final source = new TrackStages()
        
        runner.teardown(source)

        assert source.invokedTeardown
    }

    @Test
    void invokeByAnnotation_missingAnnotation() {
        final source = new NoAnnotations()
        final result = runner.invokeByAnnotation(source, TestSetup.class)

        assert !result
    }

    @Test
    void invokeByAnnotation_hasSuccessfulAnnotation() {
        final source = new SuccessfulAnnotation()
        final result = runner.invokeByAnnotation(source, TestSetup.class)

        assert !result
    }

    @Test
    void invokeByAnnotation_hasUnsuccessfulAnnotation() {
        final source = new UnsuccessfulAnnotation()
        final result = runner.invokeByAnnotation(source, TestSetup.class)

        assert result.category == StepCategory.Error
    }
}