import groovy.xml.XmlUtil

import testit.JUnitConverter
import testit.TestRunResult
import testit.TestRunRunner

//exp
import testit.SuiteRunner
import testit.TestRunner

TestRunResult call(Map args) {
    args = args ?: [:]

    final source = args.source
    
    final destination = args.destination ?: "TestResults.xml"
    final publish = args.publish as Boolean ?: true

    final converter = new JUnitConverter()
    final runner = new TestRunRunner(new SuiteRunner(new TestRunner))

    final results = runner.run(source)
    final xml = converter.convertTestRunResult(results)
    final data = XmlUtil.serialize(xml)

    writeFile(file: destination, text: data)

    if (publish)
        junit(testResults: destination)

    return results
}

return this