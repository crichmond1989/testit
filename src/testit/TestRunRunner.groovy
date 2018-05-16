package testit

import groovy.transform.CompileStatic

import testit.SuiteRunner
import testit.TestRunResult

@CompileStatic
class TestRunRunner implements Serializable {
    SuiteRunner suiteRunner = new SuiteRunner()

    TestRunResult run(List source) {
        final results = source.collect { suiteRunner.run(it) }
        
        return new TestRunResult(name: "", suites: results)
    }
}