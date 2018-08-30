pipeline {
    agent any

    stages {
        stage("Tests") {
            steps {
                script {
                    final testit = library("testit@$BRANCH_NAME").testit

                    final testFiles = findFiles(glob: "src/testit/tests/*Tests.groovy")
                    final testClasses = testFiles.collect { it.name - ".groovy" }

                    println testClasses

                    final source = testClasses.collect {
                        final test = testit.tests."$it".new()

                        test.script = this

                        return test
                    }
        
                    final destination = "testit/TestResults.xml"

                    final converter = testit.JUnitConverter.new()
                    final runner = testit.TestRunRunner.new()

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