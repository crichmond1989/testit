import groovy.xml.XmlUtil

import testit.JUnitConverter
import testit.TestRunResult
import testit.TestRunRunner

TestRunResult call(Map args) {
    args = args ?: [:]

    final source = args.source
    
    final destination = args.destination ?: "testit/TestResults.xml"
    final publish = args.publish as Boolean ?: true

    final converter = new JUnitConverter()
    final runner = new TestRunRunner()

    final results = runner.run(source)
    final xml = converter.convertTestRunResult(results)
    final data = XmlUtil.serialize(xml).trim().replace("\uFEFF", "")

    writeFile(file: destination, text: data)

    archiveArtifacts(artifacts: "**")

    if (publish)
        junit(testResults: destination)

    return results
}

return this