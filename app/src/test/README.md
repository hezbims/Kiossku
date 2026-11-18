> For UI tests based on the Compose Test Rule (Robolectric in `src/test` or
> instrumented tests in `androidTest`), also read
> [skills/page-object-model.md](skills/page-object-model.md).

# 1. How to Organize Test
## 1.1 Group tests by When, not Then

Test cases MUST be organized according to the action, event, or trigger that initiates the behavior being tested.

When represents the starting point of the behavior, not necessarily a UI location. For example:
- A method invocation.
- A user action.
- An event emission.
- A repository operation.
- A state transition trigger.
- A callback invocation.

Then represents the expected outcome and assertions resulting from that behavior.

Do NOT organize test cases based on:
- Where assertions are performed.
- Which property or state is asserted.
- Which component receives the resulting state.
- The location of the expected output.

## 1.2 Why this matters

A single behavior can produce multiple outcomes across different properties, objects, or components.
All assertions verifying the same behavior MUST remain in the same test case.
Do NOT split a test case into multiple tests or directories merely because its expected outcomes belong to different components or properties.

## 1.3 Directory and package structure

Use the following hierarchy when organizing test files:
- System under test / business capability: The functionality being tested.
- When / triggering behavior: The action or event initiating the behavior.
- Test scenarios: Different conditions and expected outcomes for that behavior.

Example:
```
src/test/java/
└── com/example/
└── domain/
    └── use_case
         ├── login/
         │   ├── LoginWithValidCredentialsTest.kt
         │   ├── LoginWithInvalidCredentialsTest.kt
         │   └── LoginWithEmptyCredentialsTest.kt
         │
         └── logout/
             ├── LogoutWithActiveSessionTest.kt
             └── LogoutWithoutActiveSessionTest.kt
```

The directory names MUST describe the triggering behavior, not the location of the expected outcome.

## 1.4 Summary
See the table belows :

| Condition                                                         | Decision                             |
|-------------------------------------------------------------------|--------------------------------------|
| One action with multiple related assertions                       | One test case                        |
| One action with multiple expected outcomes from the same behavior | One test case                        |
| Multiple independent actions                                      | Separate tests cases                 |
| Assertions verify different behaviors                             | Separate tests cases                 |
| A test is too complex or difficult to understand                  | Consider splitting based on behavior |

# 2. Test Cases Naming

## 2.1. Mandatory naming convention

### 2.1.1 Junit 5
MUST have a descriptive JUnit 5 @DisplayName.

Use this format:
```
When [triggering action or condition], and [some conditional action], then [expected outcome]
```

### 2.1.2 Junit 4
Example :

```kotlin
@file:Suppress("FunctionName")
@Test
fun `When valid credentials are submitted then login succeeds`() {
    // ...
}
```

## 2.2 Examples

**Good** :
```kotlin
@Test
@DisplayName("When valid credentials are submitted then login succeeds")
fun loginWithValidCredentials() {
    // ...
}

@Test
@DisplayName("When invalid credentials are submitted then authentication fails")
fun loginWithInvalidCredentials() {
    // ...
}
```

**Bad** :
```kotlin
@Test
@DisplayName("Login success")
fun test1() {
    // ...
}

@Test
@DisplayName("Token")
fun saveToken() {
    // ...
}
```

## 2.3 Parameterized tests 
Example :

```kotlin
@DisplayName("When the refresh token request receives a client error response then it returns an invalid refresh token error")
@ParameterizedTest(name = "When the response status code is {0}")
@ValueSource(ints = [400, 401])
fun returnsInvalidRefreshTokenErrorWhenStatusCodeIs(statusCode: Int) = _runTest {
        // ...
}
```
