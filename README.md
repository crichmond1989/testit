# TestIt
A test framework, implemented as a Shared Library, that works with Jenkins/CPS

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

### Advanced Uses

```groovy
stage("Unit Tests") {
  final unitTests = [ new UnitA(), new UnitB() ]
  final unitResult = testit(source: unitTests, destination: "UnitTestResults.xml")
  
  if (unitResult.getFailureCount() || unitResult.getErrorCount()) {
    throw new Exception("Unit Test Failure")
  }
}

stage("System Tests") {
  final systemTests = [ new TestNodeTests(), new TestDatabaseTests() ]
  final systemResult = testit(source: unitTests, destination: "SystemTestResults.xml")
  
  if (systemResult.getFailureCount() || systemResult.getErrorCount()) {
    throw new Exception("System Test Failure")
  }
}

stage("Functional Tests") {
  final functionalTests = [ new FunctionalTestA(), new FunctionalTestB() ]
  
  testit(source: unitTests, destination: "FunctionalTestResults.xml")
}
```

I use this approach with personal and work projects. Some of the work functional tests can take a long time, so it's nice to short-circuit early.

## Examples

### Test Suite Class

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

#### Output
```shell
2 + 2 = 4
```

### Test Setup

```groovy
import org.junit.Assert

import testit.Test
import testit.TestSetup

class SimpleTests implements Serializable {
  @TestSetup
  void testSetup() {
    println("Starting a test case...")
  }
  
  @Test
  void twoPlusTwoEqualsFour() {
    final expected = 4
    final actual = 2 + 2
    
    println("2 + 2 = $actual")
    
    Assert.assertEquals(expected, actual)
  }
}
```

#### Output
```shell
Starting a test case...
2 + 2 = 4
```

### Test Teardown

```groovy
import org.junit.Assert

import testit.Test
import testit.TestSetup
import testit.TestTeardown

class SimpleTests implements Serializable {
  @TestSetup
  void testSetup() {
    println("Starting a test case...")
  }
  
  @TestTeardown
  void testTeardown() {
    println("Finishing a test case...")
  }
  
  @Test
  void twoPlusTwoEqualsFour() {
    final expected = 4
    final actual = 2 + 2
    
    println("2 + 2 = $actual")
    
    Assert.assertEquals(expected, actual)
  }
}
```

#### Output
```shell
Starting a test case...
2 + 2 = 4
Finishing a test case...
```

### Suite Setup

```groovy
import org.junit.Assert

import testit.SuiteSetup
import testit.Test
import testit.TestSetup
import testit.TestTeardown

class SimpleTests implements Serializable {
  @SuiteSetup
  void suiteSetup() {
    println("Starting a test suite...")
  }
  
  @TestSetup
  void testSetup() {
    println("Starting a test case...")
  }
  
  @TestTeardown
  void testTeardown() {
    println("Finishing a test case...")
  }
  
  @Test
  void twoPlusTwoEqualsFour() {
    final expected = 4
    final actual = 2 + 2
    
    println("2 + 2 = $actual")
    
    Assert.assertEquals(expected, actual)
  }
}
```

#### Output
```shell
Starting a test suite...
Starting a test case...
2 + 2 = 4
Finishing a test case...
```

### Suite Teardown

```groovy
import org.junit.Assert

import testit.SuiteSetup
import testit.SuiteTeardown
import testit.Test
import testit.TestSetup
import testit.TestTeardown

class SimpleTests implements Serializable {
  @SuiteSetup
  void suiteSetup() {
    println("Starting a test suite...")
  }
  
  @TestSetup
  void testSetup() {
    println("Starting a test case...")
  }
  
  @TestTeardown
  void testTeardown() {
    println("Finishing a test case...")
  }
  
  @SuiteTeardown
  void suiteTeardown() {
    println("Finishing a test suite...")
  }
  
  @Test
  void twoPlusTwoEqualsFour() {
    final expected = 4
    final actual = 2 + 2
    
    println("2 + 2 = $actual")
    
    Assert.assertEquals(expected, actual)
  }
}
```

#### Output
```shell
Starting a test suite...
Starting a test case...
2 + 2 = 4
Finishing a test case...
Finishing a test suite...
```
