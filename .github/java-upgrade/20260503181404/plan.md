# Upgrade Plan: helpdesk-backend (20260503181404)

- **Generated**: 2026-05-03 18:14:04
- **HEAD Branch**: N/A
- **HEAD Commit ID**: N/A

## Available Tools

**JDKs**
- JDK 17: not available (baseline will be skipped)
- JDK 21: **<TO_BE_INSTALLED>** (required by final validation and target runtime)
- JDK 25.0.2: C:\Program Files\Java\jdk-25.0.2\bin (available; can be used for wrapper execution if JDK 21 is installed)

**Build Tools**
- Maven Wrapper: 3.9.14 (available, compatible with Java 21)
- Maven system install: not available (wrapper will be used)

## Guidelines

> Note: You can add any specific guidelines or constraints for the upgrade process here if needed, bullet points are preferred.

## Options

- Working branch: appmod/java-upgrade-20260503181404
- Run tests before and after the upgrade: true

## Upgrade Goals

- Upgrade Java runtime to Java 21 (latest LTS)

## Technology Stack

| Technology/Dependency    | Current | Min Compatible | Why Incompatible |
| ------------------------ | ------- | -------------- | ---------------------------------------------- |
| Java                     | 17      | 21             | User requested latest LTS runtime              |
| Spring Boot              | 4.0.6   | 4.0.6          | Already compatible with Java 21                |
| Maven Wrapper            | 3.9.14  | 3.9.0          | Compatible with Java 21                        |
| maven-compiler-plugin    | managed | 3.11.0+       | Required for Java 21 bytecode and source support |
| jakarta.persistence      | 3.x     | N/A            | Already on Jakarta namespace for Spring Boot 4 |

## Derived Upgrades

- Java 21 is required because the user requested the latest LTS runtime.
- Maven Wrapper 3.9.14 is already compatible with Java 21, so no wrapper upgrade is required.
- No Spring Boot upgrade is required because the project already uses Spring Boot 4.0.6, which supports Java 21.

## Upgrade Steps

- Step 1: Setup Environment
  - **Rationale**: The local system has JDK 25 only; Java 21 is required to validate the target runtime and final build.
  - **Changes to Make**:
    - Install JDK 21 on the machine.
    - Ensure the Maven wrapper can execute against JDK 21.
    - Confirm `./mvnw -version` reports Java 21 and Maven 3.9.14.
  - **Verification**: `./mvnw -version` with JDK 21, expected result: Java 21 and Maven 3.9.14.

- Step 2: Setup Baseline
  - **Rationale**: Establish current baseline behavior if the current JDK is available.
  - **Changes to Make**:
    - Detect that JDK 17 is not installed locally.
    - Skip baseline execution because the base runtime is unavailable.
  - **Verification**: skipped due to missing JDK 17.

- Step 3: Bump Java version to 21
  - **Rationale**: The project is already on Spring Boot 4.0.6 and only requires a Java property bump to target the latest LTS runtime.
  - **Changes to Make**:
    - Update `<java.version>` in `pom.xml` from `17` to `21`.
    - Confirm no additional plugin or dependency changes are required for Java 21.
  - **Verification**: `./mvnw clean test-compile -q` with JDK 21, expected result: success.

- Step 4: Final Validation
  - **Rationale**: Confirm the project compiles and passes all tests on the target Java 21 runtime.
  - **Changes to Make**:
    - Run full test suite with Java 21.
    - Fix any test failures or compile issues caused by the Java 21 upgrade.
  - **Verification**: `./mvnw clean test -q` with JDK 21, expected result: all tests pass.

## Key Challenges

- **Target Java runtime installation**
  - **Challenge**: The local environment only has JDK 25 installed, while the target is Java 21.
  - **Strategy**: Install JDK 21 and validate the Maven wrapper against it before making pom changes.

- **Baseline runtime unavailable**
  - **Challenge**: JDK 17 is not installed locally, so an exact baseline execution on the current runtime cannot be performed.
  - **Strategy**: Skip baseline and ensure the final validation is performed on Java 21.

- **JDK compatibility scan**
  - **Challenge**: No direct internal or removed-JDK API usage was found in the project sources during scan.
  - **Strategy**: Proceed with the source-version bump and rely on Maven compilation/test to surface any remaining compatibility issues.
