import groovy.xml.XmlUtil

pipeline {
    agent any

    options {
        buildDiscarder(logRotator(numToKeepStr: "10"))
    }

    stages {
        stage("Tests") {
            steps {
                script {
                    final testit = library("testit@$BRANCH_NAME").testit

                    final testFiles = findFiles(glob: "src/testit/tests/*Tests.groovy")
                    final testClasses = testFiles.collect { it.name - ".groovy" }

                    final source = testClasses.collect { testit.tests."$it".new() }
        
                    final destination = "testit/TestResults.xml"

                    final converter = testit.JUnitConverter.new()
                    final runner = testit.TestRunRunner.new()
                    final logger = testit.Logger.new()

                    runner.logger = logger
                    logger.log = { println(it) }

                    final results = runner.run(source)
                    final xml = converter.convertTestRunResult(results)
                    final data = XmlUtil.serialize(xml).trim().replace("\uFEFF", "")

                    writeFile(file: destination, text: data)

                    junit(testResults: destination)
                }
            }
        }
    }
}