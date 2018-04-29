package testit

class TestResult {
    Boolean success
    String message
    Throwable error

    TestResult(Map args) {
        success = args.success
        message = args.message
        error = args.error
    }

    static TestResult Passed() {
        return new TestResult(success: true)
    }

    static TestResult Failed(String message) {
        return new TestResult(
            success: false,
            message: message
        )
    }

    static TestResult Failed(Throwable error) {
        return new TestResult(
            success: false,
            message: error.getMessage(),
            error: error
        )
    }
}