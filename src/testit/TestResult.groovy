package testit

import groovy.transform.CompileStatic

import java.util.Date

import testit.ResultStatus
import testit.StepCategory

class TestResult implements Serializable {
    String classname
    String name
    Date start
    Date end
    List<StepResult> steps = []

    @CompileStatic
    Double getDurationInSeconds() {
        if (start && end)
            return (end.getTime() - start.getTime()) / 1000d
    }

    @CompileStatic
    ResultStatus getStatus() {
        if (!steps)
            return ResultStatus.Success

        if (steps.any { it.category == StepCategory.Error })
            return ResultStatus.Error

        if (steps.any { it.category == StepCategory.Failure })
            return ResultStatus.Failure

        return ResultStatus.Success
    }

    @CompileStatic
    void recordStart() {
        start = new Date()
    }

    @CompileStatic
    void recordEnd() {
        end = new Date()
    }
}