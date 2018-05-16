# TestIt
A test framework that works with Jenkins/CPS

## Pipeline Syntax
```groovy
final someTestObjects = [ new TestClassA(), new TestClassB() ]

testit(source: someTestObjects, destination: "TestResults.xml")
```
