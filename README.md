# selenium-test-framework

A UI regression suite for two public demo sites, written with Selenium 4, TestNG and the Page Object Model. It runs headless Chrome in parallel and produces an Allure report with a screenshot attached to any test that fails.

## Why I wrote it

Most of the Selenium examples I came across are one file with a `main` method, hardcoded sleeps and locators pasted next to assertions. I wanted something closer to what I would actually hand over to a team: pages kept apart from tests, explicit waits everywhere, configuration that CI can override without editing a file, and a report a non-developer can open.

I also picked scenarios beyond a login form on purpose. Elements that appear late, content behind an iframe boundary and native browser dialogs are where most of the flakiness in real suites comes from, so those are the parts worth showing.

## Running it

You need JDK 17, Maven and a local Chrome install. Chromedriver is resolved at runtime by WebDriverManager, so there is nothing to download by hand.

```bash
mvn test
```

The whole suite takes about 30 seconds on my machine, running three classes in parallel.

To watch it in a real browser window:

```bash
mvn test -DHEADLESS=false
```

Any key in `src/test/resources/config.properties` can be overridden by an environment variable or a `-D` system property. The environment name is the key uppercased with dots replaced by underscores, so `explicit.wait.seconds` becomes `EXPLICIT_WAIT_SECONDS`. That is how the GitHub Actions job forces headless mode without touching the file.

To generate and open the Allure report after a run:

```bash
mvn allure:serve
```

## What is covered

On saucedemo.com: a successful login, four rejected logins driven by a data provider (locked account, wrong password, missing username, missing password), adding each of three products to the cart, and the cart badge count.

On the-internet.herokuapp.com: a checkbox that gets removed and an input that becomes enabled after a delay, reading content across an iframe boundary, walking into nested frames and back out through `parentFrame()`, and handling alert, confirm and prompt dialogs.

That comes to 14 tests.

## Project structure

```
src/main/java/io/github/alikursady/qa/
  config/Config.java          settings with env and -D overrides
  driver/DriverFactory.java   browser options, headless profile
  driver/DriverManager.java   one WebDriver per thread
  pages/BasePage.java         PageFactory init, waits, shared helpers
  pages/saucedemo/            LoginPage, ProductsPage, CartPage
  pages/internet/             DynamicControlsPage, FramePage,
                              NestedFramesPage, JavaScriptAlertsPage
src/test/java/io/github/alikursady/qa/
  BaseTest.java               driver lifecycle, screenshot on failure
  data/LoginData.java         data providers
  tests/                      the four test classes
testng.xml                    suite definition, parallel by class
```

The framework lives under `src/main` and only the tests live under `src/test`, which keeps the page objects reusable and stops test-only helpers leaking into them.

## Known gaps

The suite only runs against Chrome. There is a Firefox branch in `DriverFactory` but I have not exercised it, and it is not in CI.

Clicks on saucedemo go through a helper that checks whether the click did anything and falls back to dispatching the event on the element directly. On headless Chrome under Linux the app accepts a WebDriver click without running its handler; it never happens on Windows, and running one browser at a time made it rarer but did not stop it. The fallback is a real compromise: a dispatched click skips hit testing, so from the second attempt onwards the test would no longer notice an element being covered by an overlay. The first attempt is still a genuine click, which is what keeps that check alive on the normal path.

Both target sites are third-party and I do not control them. If either is down or slow the build fails, and it will look like a product bug rather than an environment one. There is no retry logic, so a single network blip fails the run.

The iframe test reads text instead of typing it. The TinyMCE editor on the-internet is served in readonly mode (`mce-content-readonly`), so a typing scenario is not possible on that page. I found this the slow way, after the test failed with `invalid element state`.

Screenshots are attached to the Allure report only. They are not written to a folder on disk, so if you skip the report you lose them.

Allure history is not carried between runs, so the report shows the current run without trends or flakiness data.

There is no Selenium Grid or Docker setup, no cross-browser matrix and no mobile emulation. Parallelism is per class, so the four test classes are the ceiling; splitting by method would need per-method driver isolation I have not set up.

Assertions are plain TestNG, so a test stops at the first mismatch instead of collecting all of them. Test data lives in code rather than in CSV or JSON files, which is fine at this size but would not scale.

Nothing here handles secrets. `config.properties` only holds demo credentials that are public anyway, so there is no encryption or vault integration to look at.

## License

MIT, see [LICENSE](LICENSE).
