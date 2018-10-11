package testit.tests

import testit.Logger
import testit.ResultStatus
import testit.StepCategory
import testit.Suite
import testit.SuiteClassname
import testit.Test
import testit.TestRunner
import testit.TestSetup
import testit.TestTeardown

import org.hamcrest.CoreMatchers
import org.junit.Assert

class TestRunnerTests implements Serializable {
    class OnlyTestMethod {
        @Test
        void run() {}
    }
    
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

    class TrackLogger extends Logger {
        String[] trace = []
        
        @Override
        void logStepResult(String name, StepResult value) {
            trace += "logStepResult: $name".toString()
        }

        @Override
        void logTestName(String value) {
            trace += "logTestName: $value".toString()
        }
    }

    class TrackStages {
        boolean invokedSetup = false
        boolean invokedRun = false
        boolean invokedTeardown = false

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
        boolean invokedSetup = false
        boolean invokedRun = false
        boolean invokedTeardown = false

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

    @Suite(classname = "custom.from.annotation")
    class SuiteClassnameFromAnnotation {}

    class SuiteClassnameFromMethod {
        @SuiteClassname
        String getClassname() {
            return "custom.classname"
        }
    }

    TestRunner getRunner(Logger logger = null) { new TestRunner(logger: logger) }

    @Test
    void run_correctClassname() {
        final source = new SuccessfulTestMethod()
        final result = runner.run(source, "run")

        Assert.assertEquals(source.class.getName(), result.classname)
    }

    @Test
    void run_correctName() {
        final source = new SuccessfulTestMethod()
        final result = runner.run(source, "run")

        Assert.assertEquals("run", result.name)
    }

    @Test
    void run_log_noSetup() {
        final source = new OnlyTestMethod()
        final _runner = getRunner(new TrackLogger())

        runner.run(source, "run")

        Assert.assertThat(_runner.logger.trace, CoreMatchers.not(CoreMatchers.hasItem("logStepResult: Test Setup")))
    }

    @Test
    void run_log_noTeardown() {
        final source = new OnlyTestMethod()
        final _runner = getRunner(new TrackLogger())

        runner.run(source, "run")

        Assert.assertThat(_runner.logger.trace, CoreMatchers.not(CoreMatchers.hasItem("logStepResult: Test Teardown")))
    }

    @Test
    void run_log_setup() {
        final source = new OnlyTestMethod()
        final _runner = getRunner(new TrackLogger())

        runner.run(source, "run")

        Assert.assertThat(_runner.logger.trace, CoreMatchers.hasItem("logStepResult: Test Setup"))
    }

    @Test
    void run_log_teardown() {
        final source = new OnlyTestMethod()
        final _runner = getRunner(new TrackLogger())

        runner.run(source, "run")

        Assert.assertThat(_runner.logger.trace, CoreMatchers.hasItem("logStepResult: Test Teardown"))
    }

    @Test
    void run_log_testCase() {
        final source = new OnlyTestMethod()
        final _runner = getRunner(new TrackLogger())

        runner.run(source, "run")

        Assert.assertThat(_runner.logger.trace, CoreMatchers.hasItem("logTestName: run"))
    }

    @Test
    void run_statusPassOnSuccess() {
        final source = new SuccessfulTestMethod()
        final result = runner.run(source, "run")

        Assert.assertEquals(ResultStatus.Success, result.getStatus())
    }

    @Test
    void run_statusFailOnError() {
        final source = new ErroredTestMethod()
        final result = runner.run(source, "run")

        Assert.assertEquals(ResultStatus.Error, result.getStatus())
    }

    @Test
    void run_statusFailOnFailure() {
        final source = new FailedTestMethod()
        final result = runner.run(source, "run")

        Assert.assertEquals(ResultStatus.Failure, result.getStatus())
    }

    @Test
    void run_invokedSetup() {
        final source = new TrackStages()
        
        runner.run(source, "run")

        Assert.assertTrue(source.invokedSetup)
    }

    @Test
    void run_invokedRun() {
        final source = new TrackStages()
        
        runner.run(source, "run")

        Assert.assertTrue(source.invokedRun)
    }

    @Test
    void run_invokedTeardown() {
        final source = new TrackStages()
        
        runner.run(source, "run")

        Assert.assertTrue(source.invokedTeardown)
    }

    @Test
    void run_skippedRunOnSetupError() {
        final source = new TrackStagesSetupError()

        runner.run(source, "run")

        Assert.assertFalse(source.invokedRun)
    }

    @Test
    void run_skippedTeardownOnSetupError() {
        final source = new TrackStagesSetupError()

        runner.run(source, "run")

        Assert.assertFalse(source.invokedTeardown)
    }

    @Test
    void getClassname_default() {
        final source = new NoAnnotations()
        final name = runner.getClassname(source)

        Assert.assertEquals(NoAnnotations.class.getName(), name)
    }

    @Test
    void getClassname_fromAnnotation() {
        final source = new SuiteClassnameFromAnnotation()
        final name = runner.getClassname(source)

        Assert.assertEquals("custom.from.annotation", name)
    }

    @Test
    void getClassname_fromMethod() {
        final source = new SuiteClassnameFromMethod()
        final name = runner.getClassname(source)

        Assert.assertEquals(source.getClassname(), name)
    }

    @Test
    void invokeTestMethod_success() {
        final source = new SuccessfulTestMethod()
        final result = runner.invokeTestMethod(source, "run")

        Assert.assertEquals(StepCategory.Complete, result.category)
    }

    @Test
    void invokeTestMethod_error() {
        final source = new ErroredTestMethod()
        final result = runner.invokeTestMethod(source, "run")

        Assert.assertEquals(StepCategory.Error, result.category)
    }

    @Test
    void invokeTestMethod_fail() {
        final source = new FailedTestMethod()
        final result = runner.invokeTestMethod(source, "run")

        Assert.assertEquals(StepCategory.Failure, result.category)
    }

    @Test
    void setup_invoked() {
        final source = new TrackStages()
        
        runner.setup(source)

        Assert.assertTrue(source.invokedSetup)
    }

    @Test
    void teardown_invoked() {
        final source = new TrackStages()
        
        runner.teardown(source)

        Assert.assertTrue(source.invokedTeardown)
    }

    @Test
    void invokeByAnnotation_missingAnnotation() {
        final source = new NoAnnotations()
        final result = runner.invokeByAnnotation(source, TestSetup.class)

        Assert.assertNull(result)
    }

    @Test
    void invokeByAnnotation_hasSuccessfulAnnotation() {
        final source = new SuccessfulAnnotation()
        final result = runner.invokeByAnnotation(source, TestSetup.class)

        Assert.assertEquals(StepCategory.Complete, result.category)
    }

    @Test
    void invokeByAnnotation_hasUnsuccessfulAnnotation() {
        final source = new UnsuccessfulAnnotation()
        final result = runner.invokeByAnnotation(source, TestSetup.class)

        Assert.assertEquals(StepCategory.Error, result.category)
    }
}