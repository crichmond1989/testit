package testit

import java.lang.annotation.Annotation

import testit.SuiteResult
import testit.Test
import testit.TestRunner

class SuiteRunner implements Serializable {
    TestRunner testRunner

    SuiteRunner(TestRunner testRunner = null) {
        this.testRunner = testRunner ?: new TestRunner()
    }

    SuiteResult run(Object source) {
        final tests = source.class.getDeclaredMethods().findAll { it.isAnnotationPresent(Test.class) }.collect { it.getName() }
        final results = tests.collect { testRunner.run(source, it) }
        
        return new SuiteResult(name: source.class.getName(), tests: results)
    }
}