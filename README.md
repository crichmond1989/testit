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

## Examples

### Test Class

```groovy
import org.junit.Assert

import testit.Test

class SimpleTests implements Serializable {
  @Test
  void twoPlusTwoEqualsFour() {
    final expected = 4
    final actual = 2 + 2
    
    Assert.assertEquals(expected, actual)
  }
}
```

### Test Setup

```groovy
import org.junit.Assert

import testit.Test
import testit.TestSetup

class SimpleTests implements Serializable {
  @TestSetup
  void setup() {
    println("Starting a test case...")
  }
  
  @Test
  void twoPlusTwoEqualsFour() {
    final expected = 4
    final actual = 2 + 2
    
    Assert.assertEquals(expected, actual)
  }
}
```


### Test Teardown

```groovy
import org.junit.Assert

import testit.Test
import testit.TestTeardown

class SimpleTests implements Serializable {
  @TestTeardown
  void teardown() {
    println("Finishing a test case...")
  }
  
  @Test
  void twoPlusTwoEqualsFour() {
    final expected = 4
    final actual = 2 + 2
    
    Assert.assertEquals(expected, actual)
  }
}
```
