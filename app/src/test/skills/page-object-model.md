# Skill: Creating Page Object Models (Compose UI Tests)

This skill is used when writing UI tests based on the Compose Test Rule.
Test case naming and organization conventions still follow
[README.md](../README.md) — this skill only governs the structure of
Page Objects and Component Objects.

## 1. Scope

Applies to all Compose Test Rule based UI tests, whether they run on the
JVM via Robolectric (`src/test`) or as instrumented tests (`androidTest`).

## 2. When to Use a Page Object

### 2.1 Use a POM when

- One screen is tested by more than one test case.
- The same interactions (click, fill form, verify element) are reused
  across many test cases.

### 2.2 Do not use a POM when

- A test only verifies 1–2 nodes and the screen is not used by other tests.
  Writing a Page Object for that case only adds boilerplate with no benefit.

## 3. Concepts

| Term             | Meaning                                                                               |
|------------------|---------------------------------------------------------------------------------------|
| Page Object      | A class representing a full screen, e.g. `LoginPom`, `HomePom`.                       |
| Component Object | A class for a widget reused across screens, e.g. `FilterPanelCom`, `BottomNavBarCom`. |

Basic rules:

1. One Page Object may use several Component Objects.
2. All node lookup, `performXxx` actions, and `assertXxx` verifications are
   encapsulated inside the Page/Component Object. Test classes MAY call
   `composeRule.setContent(...)` in setup and `composeRule.waitForIdle()` for
   synchronization, but MUST NOT call matchers, actions, or assertions
   directly.
3. When the UI changes (text, node structure, testTag), the fix only needs
   to happen in one place: the Page/Component Object.

## 4. Writing Rules

1. Receive the `ComposeTestRule` through the constructor:

   ```kotlin
   class LoginPom(private val composeRule: ComposeTestRule)
   ```

2. Action methods start with the `actionXxxx` prefix and describe a user
   action:

   ```kotlin
   fun actionClickLoginButton()
   fun actionTypeEmail(email: String)
   ```

3. Assertion methods start with the `assertXxx` prefix (not `assertionXxx`),
   consistent with the Compose Test style (`assertIsDisplayed`,
   `assertTextEquals`):

   ```kotlin
   fun assertLoginButtonIsDisplayed()
   fun assertErrorMessageIsDisplayed(message: String)
   ```

   Note: the `When ... then ...` convention in README.md applies to test
   case names. Methods inside a POM use their own convention
   (`actionXxxx` / `assertXxx`).

4. Matchers used by more than one method are stored as class properties
   instead of being rewritten in every method:

   ```kotlin
   private val loginNode = composeRule.onNodeWithText("Login")
   ```

   This is safe because `onNodeWithText` does not search the UI immediately
   — the lookup only runs when `performXxx` or `assertXxx` is called.

5. Declare each Page Object once as a test class property. Use `by lazy` and
   access the property only after the Compose rule is applied and the test
   content has been set:

   ```kotlin
   private val homePom by lazy { HomePom(composeRule) }
   private val loginPom by lazy { LoginPom(composeRule) }
   ```

   Do NOT instantiate the same Page Object repeatedly inside individual test
   methods. Access lazy Page Objects only after `composeRule.setContent(...)`
   has run. Do NOT share Page Object instances across test class instances;
   JUnit 4 creates a new test instance for each test method, so lazy properties
   are re-created for every test.

6. Actions that trigger a state change (navigation, data load, UI change)
   must call `composeRule.waitForIdle()` before the method returns.

7. Find nodes in the following order of priority. Use the first option
   that identifies the target node:

   1. `onNode(hasRole(role) and hasText(text))`
   2. `onNode(hasRole(role))`
   3. `onNodeWithContentDescription(description)`
   4. `onNodeWithText(text)` — resolve the text from a string resource if exists, otherwise use the literal string.
   5. `onNodeWithTag(tag)` — last resort

   Note: `hasRole(...)` only works if the production node exposes a role
   via semantics (e.g. Button does it implicitly). If the node has no
   role, fall through to the next option.

8. File and class names: `<ScreenName>Pom.kt` (class `<ScreenName>Pom`)
   for Page Objects, `<WidgetName>Com.kt` (class `<WidgetName>Com`) for
   Component Objects. Put them in the `pageobject` package under the same
   feature as the tests that use them:

   ```
   ui/navigation/
   ├── SessionRedirectTest.kt
   └── pageobject/
       ├── HomePom.kt
       └── LoginPom.kt
   ```

   Component Objects shared across features go in the `component` package.

## 5. Examples

### 5.1 Simple Page Objects (assertions only)

A simple screen that only contains a single text node.

```kotlin
// ui/navigation/pageobject/HomePom.kt
class HomePom(private val composeRule: ComposeTestRule) {

    private val homeNode = composeRule.onNodeWithText("Home")

    fun assertIsDisplayed() {
        homeNode.assertIsDisplayed()
    }

    fun assertDoesNotExist() {
        homeNode.assertDoesNotExist()
    }
}
```

```kotlin
// ui/navigation/pageobject/LoginPom.kt
class LoginPom(private val composeRule: ComposeTestRule) {

    private val loginNode = composeRule.onNodeWithText("Login")

    fun assertIsDisplayed() {
        loginNode.assertIsDisplayed()
    }

    fun assertDoesNotExist() {
        loginNode.assertDoesNotExist()
    }
}
```

### 5.2 Component Object with actions

A filter panel that is part of a home screen, modeled as a Component
Object.

```kotlin
// ui/home/pageobject/FilterPanelCom.kt
class FilterPanelCom(private val composeRule: ComposeTestRule) {

    private val buyOption = composeRule.onNodeWithText("Buy")

    fun actionClickBuy() {
        buyOption.performClick()
        composeRule.waitForIdle()
    }

    fun actionClickOption(option: String) {
        composeRule.onNodeWithText(option).performClick()
        composeRule.waitForIdle()
    }

    fun assertBuyOptionIsDisplayed() {
        buyOption.assertIsDisplayed()
    }
}
```

### 5.3 Test cases using the Page Objects

Naming follows the JUnit 4 convention in README.md: the
`When ... then ...` format with backticks, together with
`@file:Suppress("FunctionName")`.

```kotlin
@file:Suppress("FunctionName")

@GraphicsMode(GraphicsMode.Mode.NATIVE)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class SessionRedirectTest {

    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var sessionManager : SessionManager
    private val homePom by lazy { HomePom(composeRule) }
    private val loginPom by lazy { LoginPom(composeRule) }

    @Before
    fun setup() {
        sessionManager = SessionManager()

        composeRule.setContent { TestGraph() }
        composeRule.waitForIdle()
    }

    @Test
    fun `When the session stays valid then the home screen is displayed`() {
        homePom.assertIsDisplayed()
    }

    @Test
    fun `When the session becomes invalid then the login screen is displayed`() {
        sessionManager.markSessionInvalid()
        composeRule.waitForIdle()

        loginPom.assertIsDisplayed()
    }

    @Test
    fun `When the session is restored while on the login screen then the home screen stays hidden`() {
        sessionManager.markSessionInvalid()
        composeRule.waitForIdle()

        sessionManager.markSessionValid()
        composeRule.waitForIdle()

        loginPom.assertIsDisplayed()
        homePom.assertDoesNotExist()
    }
}
```
