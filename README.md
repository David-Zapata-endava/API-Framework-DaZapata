# Automation Framework (Java)

This is a lightweight Java testing framework skeleton for API automation using:

- Java 21 (recommended for Lombok)
- Maven
- JUnit 5
- RestAssured

What is included

- `Config` — loads `src/main/resources/config.properties`
- `ApiClient`, `RequestBuilder` — simple HTTP client wrappers around RestAssured
- `ResponseValidator` — small reusable assertions
- `BaseTest` — initializes framework objects for tests
- A sample test: `SampleApiTest` and `TMDBApiTest` that call TheMovieDB API

How to run

Make sure you have Maven and Java 21+ installed. From the project root run:

```bash
mvn test
```

Using Lombok (JDK 21)

This project uses Lombok. To compile with Lombok reliably you should build with JDK 21 (or configure Maven toolchains to point to JDK 21).

Option 1 — set JAVA_HOME locally (macOS zsh):

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v21)
mvn clean test
```

Option 2 — use Maven Toolchains (recommended for CI):

1. Install a JDK 21 on your machine (e.g., Temurin 21 via Homebrew or Adoptium).
2. Copy `toolchains.xml.template` to `~/.m2/toolchains.xml` and update the `<jdkHome>` path to your JDK 21 install.
3. Run `mvn clean test` — Maven will use the configured JDK 21 for compilation.

CI

A GitHub Actions workflow is included in `.github/workflows/ci.yml` to build and test using JDK 21. Push to GitHub and check the Actions tab to see the results.

Next steps

The project includes a set of API tests that exercise TheMovieDB. Some process tests also exercise a writable public test API (JSONPlaceholder) to demonstrate POST/PUT/DELETE flows.

The tests require a valid TMDB API key. Create `src/test/resources/credentials.properties` with:

```
api.key=YOUR_TMDB_API_KEY
# optional: bearer.token=YOUR_TMDB_BEARER_TOKEN
```

JSONPlaceholder (https://jsonplaceholder.typicode.com) is used as a writable public test API for POST/PUT/DELETE examples; no credentials are required for it.
- Optional: add reporting (Allure) and richer logging if you want HTML reports.
