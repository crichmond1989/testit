package testit

import groovy.transform.CompileStatic

import testit.Logger
import testit.SuiteRunner
import testit.TestRunResult

class TestRunRunner implements Serializable {
    Logger logger
    SuiteRunner suiteRunner

    SuiteRunner getSuiteRunner() {
        final runner = this.@suiteRunner ?: new SuiteRunner()

        runner.logger = logger

        return runner
    }

    @CompileStatic
    TestRunResult run(List source) {
        final results = source.collect { suiteRunner.run(it) }
        
        return new TestRunResult(name: "", suites: results)
    }
}