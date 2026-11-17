# QA Automation — Practical Assignment

UI tests for [SauceDemo](https://www.saucedemo.com/) and API tests for
[Restful Booker](https://restful-booker.herokuapp.com/).

**Stack:** Java 17, Maven, JUnit 5, Selenide, REST Assured, Jackson, Allure, Docker.

## Project structure

```
src/test/java/com/example/
├── config/TestConfig.java        # reads config.properties, -Dkey=value overrides it
├── constants/TestData.java       # fixed test data: SauceDemo demo credentials
├── utils/PriceUtils.java         # "$7.99" -> 7.99
├── ui/
│   ├── BaseUiTest.java           # base URL, browser per test, @Tag("ui")
│   ├── LoginTest.java            # Test 1 — login
│   ├── ProductCartTest.java      # Test 2 — sorting and cart
│   └── pages/                    # page objects: locators + user actions
│       ├── LoginPage.java
│       ├── ProductsPage.java
│       └── CartPage.java
└── api/
    ├── BookingApiTest.java       # Test 3 — create and read a booking (+ a negative case), @Tag("api")
    ├── client/BookingClient.java # one method = one HTTP call
    └── model/                    # Java records mapped to JSON
        ├── Booking.java
        ├── BookingDates.java
        └── CreatedBooking.java
src/test/resources/
├── config.properties             # URLs of the systems under test
└── allure.properties             # where Allure writes results
```

## Prerequisites

Local run:
- JDK 17 or newer
- Maven 3.9+
- Google Chrome (Selenide downloads the matching driver itself)

Docker run: only Docker.

## Local execution

| What | Command |
|---|---|
| UI tests | `mvn test -Dgroups=ui` |
| API tests | `mvn test -Dgroups=api` |
| All tests | `mvn test` |

Useful options:
- `-Dselenide.headless=true` — run the browser without a window.
- `-Dsurefire.rerunFailingTestsCount=0` — turn off the single re-run of a failed test.
- Any key from `config.properties` can be overridden, e.g. `-Dapi.baseUrl=http://localhost:3001`.

Allure report (after a test run): `mvn allure:serve`.

## Docker execution

```bash
docker build -t qa-automation-tests .
docker run --rm qa-automation-tests
```

The image contains Maven, JDK 17 and Google Chrome. The container runs all tests with a headless
Chrome (`--no-sandbox` is passed because Chrome runs as root inside the container).

To run one group, pass the Maven command (the Chrome options are needed only for UI tests):

```bash
docker run --rm qa-automation-tests mvn -B test -Dgroups=api
docker run --rm qa-automation-tests mvn -B test -Dgroups=ui -Dselenide.headless=true -Dchromeoptions.args=--no-sandbox
```

To keep the results for the Allure report, mount `target` (PowerShell syntax), then run
`mvn allure:serve` locally:

```bash
docker run --rm -v "${PWD}/target:/app/target" qa-automation-tests
```

## Test approach

**Locators.** SauceDemo marks its elements with `data-test` attributes. They exist for testing
and do not change when styles or layout change, so every UI locator uses them. Locators live
only in page objects, so a markup change is fixed in one place. A product's "Add to cart"
button is found inside that product's card (`findBy(text(name)).$("button")`), so the method
works for any product without a per-product locator.

**Synchronization.** There are no hard waits. Selenide actions (`click`, `setValue`) wait for
the element, and checks (`shouldHave`, `shouldBe`) retry until the condition is met or the
timeout (4 s) passes. For sorting, the expected order is calculated from the page data first,
and then `exactTexts(expected)` waits until the page shows that order. A plain `texts()`
snapshot is taken only after waiting for the products to appear.

**Structure.**
- UI: page objects hold locators and actions and return the page the user lands on;
  assertions stay in the tests, so a test shows what it checks.
- API: `BookingClient` only sends requests; the test checks the status and the body.
  Requests and responses are Java records, so no JSON is built by hand.
- Each UI test gets a fresh browser (`closeWebDriver()` after each test), so cart and
  session state never leak between tests. The API test creates its own booking.
- Tests are grouped with JUnit tags (`ui`, `api`) to run them separately.
- Allure collects Selenide steps with a screenshot on failure and every HTTP request/response.

**Assumptions and limitations.**
- Both systems are public demo sites. They can be slow or reset data, so a failed test is
  re-run once (`surefire.rerunFailingTestsCount=1`). A test that fails twice is a real failure.
- Restful Booker returns `200` (not `201`) for `POST /booking` — the test follows the actual API.
- Restful Booker answers `418` if `Accept` is not exactly `application/json`, so the client sets it explicitly.
- Created bookings are not deleted: `DELETE` needs an auth token, and the API resets its data regularly.
- Prices are compared as `double`: they are only ordered, not calculated.
