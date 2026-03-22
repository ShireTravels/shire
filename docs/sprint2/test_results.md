# Sprint 02: Unit Test Results

This document summarizes the execution and outcomes of the unit tests for the domain and repository layers during Sprint 02 (Task T3.4).

## 1. Test Execution Summary

- **Total Tests Executed:** 29
- **Passed:** 29
- **Failed:** 0
- **Skipped:** 0
- **Execution Environment:** Local JVM with `returnDefaultValues = true` to support standard `android.util.Log` calls in basic Unit Tests without needing full instrumentation.

## 2. Tested Components

The test suite thoroughly verified the **InMemory Data Source logic** implementing the Repository pattern across various domains. 
For each repository, the following core CRUD operations were verified successfully:

### 2.1 `ActivityRepositoryImpl`
- `getActivity_returnsCorrectActivity`: Verified by ID retrieval.
- `addActivity_increasesCountAndReturnsValidId`: Validated new ID generation and item placement.
- `updateActivity_modifiesExistingData`: Asserted that changes reflect on consecutive retrievals.
- `deleteActivity_removesActivity`: Confirmed the list size reduction and nil returns for the deleted ID.

### 2.2 `TripRepositoryImpl`
- Verified `getTrip_returnsCorrectTrip`.
- Verified `addTrip_increasesCountAndReturnsValidId`.
- Verified `updateTrip_modifiesExistingData`.
- Verified `deleteTrip_removesTrip`.

### 2.3 Standalone Repositories (`Car`, `Flight`, `Hotel`, `Place`)
All underlying providers have similar test profiles executing the following test cases to ensure memory safety and accurate mutation tracking:
- `testAdd<Entity>_addsToList`
- `testGet<Entity>_existingId_returns<Entity>`
- `testUpdate<Entity>_modifiesExisting<Entity>`
- `testDelete<Entity>_removesFromList`
- `testGet<Entity>s_returnsInitialList`

## 3. Findings & Resolution

During early test phases, an issue was discovered when integrating Logcat capabilities (Task **T3.5**), leading to a `RuntimeException` since `android.util.Log` methods by default in JVM unit tests.
This was successfully resolved by introducing the `testOptions.unitTests.isReturnDefaultValues = true` flag in the Application's `build.gradle.kts`, allowing proper simulated interactions and ensuring successful validation pipelines.

**Conclusion:** All Sprint 02 repository and interaction behaviors meet the expected acceptance criteria.
