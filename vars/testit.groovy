import groovy.xml.XmlUtil

import testit.JUnitConverter
import testit.Logger
import testit.TestRunResult
import testit.TestRunRunner

TestRunResult call(Map args) {
    args = args ?: [:]

    final onLog = args.onLog
    final source = args.source
    
    final destination = args.destination ?: "testit/TestResults.xml"
    final publish = args.publish as Boolean ?: true

    final converter = new JUnitConverter()
    final runner = new TestRunRunner()

    if (onLog)
        runner.logger = new Logger(log: onLog)

    final results = runner.run(source)
    final xml = converter.convertTestRunResult(results)
    final data = XmlUtil.serialize(xml).trim().replace("\uFEFF", "")

    writeFile(file: destination, text: data)

    if (publish)
        junit(testResults: destination)

    return results
}

return this