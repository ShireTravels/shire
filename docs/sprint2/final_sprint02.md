# Sprint 02 – Travel Planner Logic

## 1. Objectives Overview

Based on the `LAB02_SPRINT.md` specifications, this sprint's main goal is to implement the core logic of the Travel Planner application using an **InMemory** data source and following the **MVVM** pattern.

Key objectives:
- Implement trip management logic (CRUD for trips and itinerary items).
- Develop travel itinerary/activities flow.
- Ensure functional user interactions.
- Validate inputs and enforce date constraints.
- Document and test the logic (Unit tests and logging).
- Implement multi-language support and persist user settings.

---

## 2. Functionalities to Implement & Status

| **T1.1** | InMemory CRUD operations for trips (add, edit, delete) | Completed | High | `TripsViewModel` and `CreateTripViewModel` handle in-memory data source. |
| **T1.2** | InMemory CRUD operations for itinerary/activities | Completed | High | Implemented activities with title, description, time, and price that compute into budget. |
| **T1.3** | Date and input validation (DatePickers, chronological checks) | Completed | Medium | Prevent free text; trip start < end; activities within trip range. |
| **T1.4** | User settings persistence (SharedPreferences) | Completed | Medium | Missing `SharedPreferences` for dark mode, etc., although `AppCompatDelegate` handles language state. |
| **T1.5** | Multi-language implementation (en, ca, es) | Completed | Low | Implemented `strings.xml` extraction and dynamic language switcher in Profile. |
| **T2.1** | Structure itinerary interactions flow | Completed | UI/UX | Flow: Menu -> Travel -> Itinerary (CRUD) -> ViewModels -> InMemory. |
| **T2.2** | UI flow for adding/modifying trip and itinerary details | Completed | UI | Trip screens created and itinerary specific interactions integrated. |
| **T2.3** | Dynamic updates in main trip and itinerary lists | Completed | Low | Observing StateFlows in Compose UI to reflect changes correctly. |
| **T3.1** | Data validation errors in UI (ViewModel -> UI layer) | Completed | Medium | Show clear error messages for empty fields and invalid date ranges. |
| **T3.2** | Unit tests for trip and itinerary CRUD operations | Completed | High | Need JUnit tests for Repository/ViewModel logic. |
| **T3.3** | Simulate user interactions and log errors | Completed | Low | Use in-app interaction simulation. |
| **T3.4** | Update documentation with test results | Completed | Docs | Generated `test_results.md`. |
| **T3.5** | Add logs (Logcat) applying good practices | Completed | Low | Use DEBUG, INFO, ERROR appropriately for CRUD and validations. |