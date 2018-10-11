package testit

class Logger implements Serializable {
    Closure log
    int lineWidth = 80

    void logStepResult(String name, StepResult value) {
        if (name) {
            log(name)
            value.statusLog.each { log("\t$it" )}
        } else {
            value.statusLog.each { log(it) }
        }

        log("-" * lineWidth)
    }

    void logSuiteName(String value) {
        log("+" * lineWidth)
        log(" Suite: $value")
        log("+" * lineWidth)
    }

    void logTestName(String value) {
        log("=" * lineWidth)
        log(" Test Case: $value")
        log("=" * lineWidth)
    }

    void logTestResult(String name, TestResult value) {
        logTestName(name)
        value.steps.each { logStepResult(null, it) }
    }
}