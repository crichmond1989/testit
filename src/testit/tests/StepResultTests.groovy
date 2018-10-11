package testit.tests

import testit.StepResult
import testit.Test

import org.hamcrest.CoreMatchers
import org.junit.Assert

import java.lang.Exception

class StepResultTests implements Serializable {    
    @Test
    void statusLog_errorShowsMessage() {
        final result = StepResult.errored(new Exception("this is a test"))
        final log = result.statusLog

        Assert.assertThat(log, CoreMatchers.hasItem("Message: this is a test"))
    }

    @Test
    void statusLog_errorShowsType() {
        final result = StepResult.errored(new Exception())
        final log = result.statusLog

        Assert.assertThat(log, CoreMatchers.hasItem("Error: java.lang.Exception"))
    }
}