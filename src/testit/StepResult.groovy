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

            case StepCategory.Complete:
                return ResultStatus.Failure

            case StepCategory.StandardOutput:
            case StepCategory.Success:
                return ResultStatus.Success
        }
    }

    @CompileStatic
    static StepResult completed() {
        return new StepResult(
            category: StepCategory.Success
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