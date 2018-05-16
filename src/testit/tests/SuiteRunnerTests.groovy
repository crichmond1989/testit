package testit.tests

import testit.Suite
import testit.SuiteName
import testit.SuiteRunner
import testit.Test

import org.junit.Assert

class SuiteRunnerTests implements Serializable {    
    @Suite(name = "custom suite")
    class CustomNameSuite {
        @Test
        void run() {}
    }

    class ImpliedSuite {
        @Test
        void run() {}
    }

    @Suite
    class NoNameSuite {
        @Test
        void run() {}
    }

    class SuiteNameFromMethod {
        @SuiteName
        String getSuiteName() {
            return "custom name from field"
        }
    }

    SuiteRunner runner = new SuiteRunner()

    @Test
    void getSuiteName_customNameSuite() {
        final source = new CustomNameSuite()
        final name = runner.getSuiteName(source)

        Assert.assertEquals("custom suite", name)
    }

    @Test
    void getSuiteName_impliedSuite() {
        final source = new ImpliedSuite()
        final name = runner.getSuiteName(source)

        Assert.assertEquals(ImpliedSuite.class.getName(), name)
    }

    @Test
    void getSuiteName_noNameSuite() {
        final source = new NoNameSuite()
        final name = runner.getSuiteName(source)

        Assert.assertEquals(NoNameSuite.class.getName(), name)
    }

    @Test
    void getSuiteName_suiteNameFromMethod() {
        final source = new SuiteNameFromMethod()
        final name = runner.getSuiteName(source)

        Assert.assertEquals(source.getSuiteName(), name)
    }
}