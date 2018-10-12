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

        runner.logger = this.logger

        return runner
    }

    TestRunResult run(List source) {
        final runner = this.getSuiteRunner()
        final results = source.collect { runner.run(it) }

        final fullResult = new TestRunResult(name: "", suites: results)

        this.logger?.log(fullResult.statusLog)

        return fullResult
    }
}