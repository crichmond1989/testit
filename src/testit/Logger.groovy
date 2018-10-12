package testit

class Logger implements Serializable {
    Closure log
    int lineWidth = 80

    void logStepResult(String name, StepResult value) {
        final lines = []
        
        if (name) {
            lines += name
            value.statusLog.each { lines += "\t$it" }
        } else {
            lines += value.statusLog
        }

        lines += "-" * lineWidth

        log(lines.join("\n"))
    }

    void logSuiteName(String value) {
        final lines = []
        
        lines += "+" * lineWidth
        lines += "Suite: $value"
        lines += "+" * lineWidth

        log(lines.join("\n"))
    }

    void logTestName(String value) {
        final lines = []
        
        lines += "=" * lineWidth
        lines += "Test Case: $value"
        lines += "=" * lineWidth

        log(lines.join("\n"))
    }

    void logTestResult(String name, TestResult value) {
        logTestName(name)
        value.steps.each { logStepResult(null, it) }
    }
}