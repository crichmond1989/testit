package testit

import testit.StepCategory

class StepResult implements Serializable {
    StepCategory category
    String message
    String type
    Throwable error
    String trace

    static StepResult errored(Throwable error) {
        return new StepResult(
            category: StepCategory.Error,
            message: error.getMessage(),
            type: error.class.getName(),
            error: error,
            trace: error.getStackTrace().join("\n")
        )
    }
    
    static StepResult failed(AssertionError error) {
        return new StepResult(
            category: StepCategory.Failure,
            message: error.getMessage(),
            type: error.class.getName(),
            error: error,
            trace: error.getStackTrace().join("\n")
        )
    }

    static StepResult wrote(String message) {
        return new StepResult(
            category: StepCategory.StandardOutput,
            message: message
        )
    }

    static StepResult wroteError(String message) {
        return new StepResult(
            category: StepCategory.StandardError,
            message: message
        )
    }
}