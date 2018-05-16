# TestIt
A test framework that works with Jenkins/CPS

## Pipeline Syntax

```groovy
final someTestObjects = [ new TestClassA(), new TestClassB() ]

testit(source: someTestObjects, destination: "TestResults.xml", publish: true)
```

### Parameters
| Parameter | Type | Description | Required/Default |
| - | - | - | - |
| source | `List<Object>` | The test classes | Required |
| destination | `String` | The JUnit output path | `"TestResults.xml"` |
| publish | `boolean` | Should publish results via the JUnit plugin | `true` |

### Dependencies
[JUnit Plugin](https://plugins.jenkins.io/junit)
