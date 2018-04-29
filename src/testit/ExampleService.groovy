package testit

import groovy.lang.Script

class ExampleService implements Serializable {
    Script script

    ExampleService(Script script) {
        this.script = script
    }

    void build(String branch = "master") {
        script.env.isRelease = (branch == "master") as String
        
        script.git(
            branch: branch,
            url: "https://github.com/crichmond1989/hellonetcore"
        )

        script.sh("dotnet restore")
    }
}