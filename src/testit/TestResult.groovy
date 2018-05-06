package testit

import java.util.Date

import testit.StepCategory

class TestResult implements Serializable {
    String classname
    String name
    Date start
    Date end
    List<StepResult> steps = []

    Double getDurationInSeconds() {
        if (start && end)
            return (end.getTime() - start.getTime()) / 1000
    }

    String getStatus() {
        if (!steps)
            return "pass"

        final hasErrorOrFailure = steps.any { it.category == StepCategory.Error || it.category == StepCategory.Failure }

        return hasErrorOrFailure ? "fail" : "pass"
    }

    void recordStart() {
        start = new Date()
    }

    void recordEnd() {
        end = new Date()
    }
}