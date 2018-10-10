package testit

import groovy.transform.CompileStatic

import testit.ResultStatus
import testit.StepCategory

class StepResult implements Serializable {
    StepCategory category
    String message
    String type
    Throwable error
    String trace

    @CompileStatic
    ResultStatus getStatus() {
        switch(category) {
            case StepCategory.Error:
            case StepCategory.StandardError:
                return ResultStatus.Error

            case StepCategory.Failure:
                return ResultStatus.Failure

            case StepCategory.Complete:
            case StepCategory.StandardOutput:
                return ResultStatus.Success
        }
    }

    @CompileStatic
    String[] getStatusLog() {
        final rows = []

        rows += "Result: ${this.status}"

        if (this.type)
            rows += "Error: ${this.type}"

        if (this.message)
            rows += "Message: ${this.message}"

        if (this.trace)
            rows += "Stack: ${this.trace}"

        return rows
    }

    @CompileStatic
    static StepResult completed() {
        return new StepResult(
            category: StepCategory.Complete
        )
    }

    @CompileStatic
    static StepResult errored(Throwable error) {
        return new StepResult(
            category: StepCategory.Error,
            message: error.getMessage(),
            type: error.class.getName(),
            error: error,
            trace: error.getStackTrace().join("\n")
        )
    }

    @CompileStatic
    static StepResult failed(AssertionError error) {
        return new StepResult(
            category: StepCategory.Failure,
            message: error.getMessage(),
            type: error.class.getName(),
            error: error,
            trace: error.getStackTrace().join("\n")
        )
    }

    @CompileStatic
    static StepResult wrote(String message) {
        return new StepResult(
            category: StepCategory.StandardOutput,
            message: message
        )
    }

    @CompileStatic
    static StepResult wroteError(String message) {
        return new StepResult(
            category: StepCategory.StandardError,
            message: message
        )
    }
}