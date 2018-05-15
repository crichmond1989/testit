package testit

import java.util.Date

import testit.ResultStatus
import testit.StepCategory

@Grab("com.cloudbees:groovy-cps:1.1")
import com.cloudbees.groovy.cps.NonCPS

class TestResult implements Serializable {
    String classname
    String name
    Date start
    Date end
    List<StepResult> steps = []

    @NonCPS
    Double getDurationInSeconds() {
        if (start && end)
            return (end.getTime() - start.getTime()) / 1000d
    }

    @NonCPS
    ResultStatus getStatus() {
        if (!steps)
            return ResultStatus.Success

        if (steps.any { it.category == StepCategory.Error })
            return ResultStatus.Error

        if (steps.any { it.category == StepCategory.Failure })
            return ResultStatus.Failure

        return ResultStatus.Success
    }

    @NonCPS
    void recordStart() {
        start = new Date()
    }

    @NonCPS
    void recordEnd() {
        end = new Date()
    }
}