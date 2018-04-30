package testit

import testit.StepCategory

class StepResult implements Serializable {
    StepCategory category
    String message
    String type
    Throwable error
    String trace

    static StepResult Errored(Throwable error) {
        return new StepResult(
            category: StepCategory.Error,
            message: error.getMessage(),
            type: error.class.getName(),
            error: error,
            trace: error.getStackTrace().join("\n")
        )
    }
    
    static StepResult Failed(AssertionError error) {
        return new StepResult(
            category: StepCategory.Failure,
            message: error.getMessage(),
            type: error.class.getName(),
            error: error
        )
    }

    static StepResult Wrote(String message) {
        return new StepResult(
            category: StepCategory.StandardOutput,
            message: message
        )
    }

    static StepResult WroteError(String message) {
        return new StepResult(
            category: StepCategory.StandardError,
            message: message
        )
    }
}