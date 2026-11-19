> For UI tests based on the Compose Test Rule (Robolectric in `src/test` or
> instrumented tests in `androidTest`), also read
> [skills/page-object-model.md](skills/page-object-model.md).

# 1. How to Organize Tests

## 1.1 Group tests by When, not Then

Test cases MUST be organized according to the action, event, or trigger that
initiates the behavior being tested.

When represents the starting point of the behavior, not necessarily a UI
location. For example:

- A method invocation.
- A user action.
- An event emission.
- A repository operation.
- A state transition trigger.
- A callback invocation.

Then represents the expected outcome and assertions resulting from that
behavior.

Do NOT organize test cases based on:

- Where assertions are performed.
- Which property or state is asserted.
- Which component receives the resulting state.
- The location of the expected output.

## 1.2 Why this matters

A single behavior can produce multiple outcomes across different properties,
objects, or components. All assertions verifying the same behavior MUST remain
in the same test case.

Do NOT split a test case into multiple tests or directories merely because its
expected outcomes belong to different components or properties.

## 1.3 Directory and package structure

The package/directory MUST describe the system under test (SUT) or the business
capability being tested.

There are two accepted approaches for file granularity. Both are valid as long
as test cases are still grouped by their triggering behavior. Both approaches
use the same SUT folder; they differ only in how test files are split inside it.

The approach is a **per-SUT decision**, not a repository-wide decision:

- Each SUT MUST consistently use exactly one approach.
- Different SUTs MAY use different approaches in the same repository.
- A repository MAY contain both Approach 1 and Approach 2.
- Do NOT force all SUTs in a repository to use the same approach.

### Approach 1 — one file per SUT

Use this approach when the SUT is small and all scenarios share the same setup.
Each SUT has one test file containing all of its triggering behaviors.

```text
src/test/java/
└── com/example/
    └── domain/
        ├── checkout/
        │   └── CheckoutTest.kt
        └── refund/
            └── RefundTest.kt
```

`CheckoutTest.kt` contains all triggering behaviors for the checkout SUT:

```kotlin
@DisplayName("Checkout")
class CheckoutTest {

    @Test
    @DisplayName("When the cart is empty then checkout is rejected")
    fun rejectsCheckoutWhenCartIsEmpty() {
        // ...
    }

    @Test
    @DisplayName("When payment succeeds then the order is created")
    fun createsOrderWhenPaymentSucceeds() {
        // ...
    }

    @Test
    @DisplayName("When payment fails then the order is not created")
    fun doesNotCreateOrderWhenPaymentFails() {
        // ...
    }
}
```

### Approach 2 — one file per triggering behavior

Use this approach when the SUT has several independent triggering behaviors,
different fixtures, or the single file becomes too large to understand. Each
triggering behavior gets its own test file inside the SUT folder.

```text
src/test/java/
└── com/example/
    └── domain/
        └── checkout/
            ├── CheckoutWithEmptyCartTest.kt
            ├── CheckoutWhenPaymentSucceedsTest.kt
            └── CheckoutWhenPaymentFailsTest.kt
```

Each file represents one triggering behavior and contains all expected outcomes
for that behavior:

```kotlin
@DisplayName("Checkout - empty cart")
class CheckoutWithEmptyCartTest {

    @Test
    @DisplayName("When the cart is empty then checkout is rejected")
    fun rejectsCheckoutWhenCartIsEmpty() {
        // ...
    }
}
```

### Mixing approaches in one repository

A repository can use both approaches at the same time. The tree below is valid
because each SUT chooses one approach and stays consistent within that SUT:

```text
src/test/java/
└── com/example/
    └── domain/
        ├── checkout/                 # Approach 1
        │   └── CheckoutTest.kt
        │
        ├── refund/                   # Approach 2
        │   ├── RefundWhenPaymentSucceedsTest.kt
        │   └── RefundWhenPaymentFailsTest.kt
        │
        └── report/                   # Approach 1
            └── ReportTest.kt
```

The same SUT MUST NOT mix both approaches. For example, `checkout` MUST NOT
contain both `CheckoutTest.kt` and `CheckoutWhenPaymentFailsTest.kt`.

### Rules for both approaches

- The package/directory MUST represent the SUT or business capability.
- Approach 1 MUST keep all tests for one SUT in one file.
- Approach 2 MUST keep all tests for one triggering behavior in one file.
- Each SUT MUST consistently use exactly one approach.
- Different SUTs MAY use different approaches in the same repository.
- Do NOT enforce one approach across the entire repository.
- A file MUST NOT be split only because the expected outcomes differ.
- Each test method MUST cover one trigger and all of its outcomes.
- If a file needs different setup/fixtures or becomes difficult to navigate,
  switch that SUT to Approach 2 and split it by triggering behavior.
- File names MUST describe the trigger or capability, never the expected
  outcome alone.
- Do NOT mix different SUTs in one file merely because one test happens to use
  both.

## 1.4 Summary

| Condition                                                         | Decision                                        |
|-------------------------------------------------------------------|-------------------------------------------------|
| One action with multiple related assertions                       | One test case                                   |
| One action with multiple expected outcomes from the same behavior | One test case                                   |
| Multiple independent actions                                      | Separate test cases                             |
| Assertions verify different behaviors                             | Separate test cases                             |
| A test is too complex or difficult to understand                  | Consider splitting based on behavior            |
| A test file becomes too large or needs different setup/fixtures   | Use Approach 2 and split by triggering behavior |
| Expected outcomes differ but the trigger and setup are the same   | Keep them in the same file                      |
| Different SUTs need different file granularity                    | Allowed; choose the approach per SUT             |

## 1.5 Test framework selection

- Use **JUnit 5 (Jupiter)** for regular JVM unit tests.
- Use **JUnit 4 + Robolectric** only when the test requires Robolectric or the
  Compose Test Rule. The Compose Test Rule is JUnit 4 based.
- Do NOT add new JUnit 4 tests unless Robolectric or the Compose Test Rule
  requires them.

# 2. Test Cases Naming

## 2.1 Mandatory naming convention

### 2.1.1 JUnit 5

MUST have a descriptive JUnit 5 `@DisplayName` on every `@Test` and
`@ParameterizedTest`.

Use this format:

```text
When [triggering action or condition], and [some conditional action], then [expected outcome]
```

The `and [some conditional action]` part is optional. Use it only when the
scenario needs an additional condition.

Rules:

- `@DisplayName` is the authoritative test name.
- Function names MUST be descriptive camelCase and MUST NOT use names such as
  `test1`, `testToken`, or `saveToken`.
- Test names and `@DisplayName` values MUST be written in English.
- A class-level `@DisplayName` is optional and SHOULD describe the SUT or
  capability, not a single scenario.

Example:

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

### 2.1.2 JUnit 4

JUnit 4 tests MUST use backtick function names in the `When ... then ...`
format, together with `@file:Suppress("FunctionName")`.

Example:

```kotlin
@file:Suppress("FunctionName")

@Test
fun `When valid credentials are submitted then login succeeds`() {
    // ...
}
```

Do NOT add `@DisplayName` to JUnit 4 tests.

## 2.2 Examples

**Good**:

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

**Bad**:

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

**Bad**:

```kotlin
@Test
@DisplayName("When the response is received then the token is saved")
fun testTokenIsSaved() {
    // ...
}

@Test
@DisplayName("When the response is received then the session becomes valid")
fun testSessionBecomesValid() {
    // ...
}
```

The last example is bad because both tests describe the same trigger and the
same behavior. Both assertions MUST stay in one test case.

## 2.3 Parameterized tests

Use a parameterized test when the same trigger and expected outcome apply to
multiple input values.

Example:

```kotlin
@DisplayName("When the refresh token request receives a client error response then it returns an invalid refresh token error")
@ParameterizedTest(name = "When the response status code is {0}")
@ValueSource(ints = [400, 401])
fun returnsInvalidRefreshTokenErrorWhenStatusCodeIs(statusCode: Int) {
    // ...
}
```

Rules:

- `@DisplayName` describes the overall behavior and expected outcome.
- `@ParameterizedTest(name = ...)` describes the condition for each invocation.
- Do NOT use a parameterized test when different inputs produce different
  expected outcomes. Use separate test methods instead.

## 2.4 File and class naming

- Test classes MUST end with `Test`.
- Test class names MUST use PascalCase.
- For Approach 1, use `<Sut>Test` such as `CheckoutTest`.
- For Approach 2, use `<Capability><TriggerGroup>Test`, such as
  `CheckoutWhenPaymentFailsTest`.
- Package names MUST be lowercase. Use snake_case for multi-word package
  segments when needed.

# 3. Compose UI Tests and Page Objects

Compose Test Rule based UI tests MUST follow
[skills/page-object-model.md](skills/page-object-model.md).

The encapsulation rule applies to node lookup, actions, and assertions:

- Test classes MAY call `composeRule.setContent(...)` in test setup.
- Test classes MAY call `composeRule.waitForIdle()` for synchronization, or
  delegate synchronization to an assertion method in a Page Object.
- Test classes MUST NOT call matchers such as `onNodeWithText`,
  `performClick`, `assertIsDisplayed`, or other Compose Test APIs directly.

# 4. Checklist

- [ ] The package/directory describes the SUT or business capability.
- [ ] Each SUT consistently uses exactly one approach.
- [ ] The repository may mix Approach 1 and Approach 2 across different SUTs.
- [ ] No file is split only because the expected outcomes differ.
- [ ] Every JUnit 5 test has a complete `When ... then ...` `@DisplayName`.
- [ ] Every JUnit 4 test uses a backtick `When ... then ...` function name and
      `@file:Suppress("FunctionName")`.
- [ ] JUnit 4 is used only for Robolectric or Compose Test Rule tests.
- [ ] All assertions for one behavior are in the same test case.
- [ ] Parameterized tests are used only for different inputs with the same
      expected outcome.
- [ ] Test names and `@DisplayName` values are written in English.
- [ ] Test classes end with `Test`.
- [ ] Compose UI tests use Page Objects and do not call Compose matchers,
      actions, or assertions directly.
