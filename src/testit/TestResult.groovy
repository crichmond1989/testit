package testit

import testit.StepCategory

class TestResult implements Serializable {
    String classname
    String name
    List<StepResult> steps = []

    String getStatus() {
        if (!steps)
            return "pass"

        final hasErrorOrFailure = steps.any { it.category == StepCategory.Error || it.category == StepCategory.Failure }

        return hasErrorOrFailure ? "fail" : "pass"
    }
}