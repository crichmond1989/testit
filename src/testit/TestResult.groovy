package testit

import groovy.transform.CompileStatic

import java.util.Date

import testit.ResultStatus
import testit.StepCategory

@CompileStatic
class TestResult implements Serializable {
    String classname
    String name
    Date start
    Date end
    List<StepResult> steps = []

    Double getDurationInSeconds() {
        if (start && end)
            return (end.getTime() - start.getTime()) / 1000d
    }

    ResultStatus getStatus() {
        if (!steps)
            return ResultStatus.Success

        if (steps.any { it.category == StepCategory.Error })
            return ResultStatus.Error

        if (steps.any { it.category == StepCategory.Failure })
            return ResultStatus.Failure

        return ResultStatus.Success
    }

    void recordStart() {
        start = new Date()
    }

    void recordEnd() {
        end = new Date()
    }
}