package testit

import testit.SuiteRunner
import testit.TestRunResult

class TestRunRunner implements Serializable {
    SuiteRunner suiteRunner

    TestRunRunner(SuiteRunner suiteRunner = null) {
        this.suiteRunner = suiteRunner ?: new SuiteRunner()
    }

    TestRunResult run(Object... source) {
        final results = source.collect { suiteRunner.run(it) }
        
        return new TestRunResult(name: "", suites: results)
    }
}