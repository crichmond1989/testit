package org.crichmond1989

import groovy.lang.Script

class BuildService implements Serializable {
    Script script

    BuildService(Script script) {
        this.script = script
    }

    void buildHelloNetCore(String branch = "master") {
        script.env.isRelease = branch == "master"
        
        script.git(
            branch: branch,
            url: "https://github.com/crichmond1989/hellonetcore"
        )

        script.bat("dotnet restore")
    }
}