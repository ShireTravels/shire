# Sprint 03 – Persistence & Authentication

## 1. Objectives Overview

Based on the `plan_sprint03.md` specifications, this sprint's main goal is to transition the app from in-memory storage to persistent local storage using **SQLite (Room Database)** and implement user authentication using **Firebase**.

Key objectives:
- Replace in-memory storage with Room (DAO, Entities, Database).
- Integrate Firebase Auth for User Login, Registration, and Password Recovery.
- Ensure CRUD operations for trips and itineraries are restricted to the logged-in user.
- Implement comprehensive data validation and use Logcat for deep monitoring.
- Test database functionality (Unit Tests) and document the DB schema.

---

## 2. Functionalities to Implement & Status

| Task ID | Functionality | Status | Priority | Notes |
|---------|---------------|--------|----------|-------|
| **T1.1** | Setup Room Database class | Completed | High | `ShireRoomDatabase` and `dbImpl.kt` configured for local storage. |
| **T1.2** | Define Room Entities | Completed | High | Created entities for Trip, Activity, User, Car, Flight, Hotel, Place. |
| **T1.3** | Create DAOs for entities | Completed | High | `ShireDaos.kt` created with queries and `@Upsert` for safe updates. |
| **T1.4** | Implement CRUD logic with DAOs | Completed | High | Repositories fully rely on DAO methods instead of memory arrays. |
| **T1.5** | Migrate ViewModels to Room | Completed | High | ViewModels collect from Room `Flow`s for reactive updates. |
| **T1.6** | Bind UI to database state changes | Completed | Medium | `StateFlow` ensures real-time UI updates upon any DB changes. |
| **T2.1** | Firebase Project & App Connection | Completed | High | Firebase SDK dependencies and plugins configured. |
| **T2.2** | Design Login Screen UI | Completed | Medium | Compose UI built and bound to `AuthViewModel`. |
| **T2.3** | Implement Login logic (Firebase) | Completed | High | `signInWithEmailAndPassword` integrated in `AuthRepositoryImpl`. |
| **T2.4** | Implement Logout action | Completed | Medium | Safe session clearing and navigation backstack reset implemented. |
| **T2.5** | Add Logcat for auth monitoring | Completed | Low | Auth state transitions, errors, and mappings logged and handled. |
| **T3.1** | Design Registration Screen UI | Completed | Medium | Compose UI for secure registration created. |
| **T3.2** | Implement Registration & Email Verif. | Completed | High | Firebase registration and automatic local `DbUser` syncing. |
| **T3.3** | Design & Logic for Password Recovery | Completed | Medium | `recoverPassword` function linked to Firebase backend. |
| **T4.1** | Implement `User` table | Completed | High | `users` table created with unique constraints on email and username. |
| **T4.2** | Link Trips to specific User IDs | Completed | High | `Trip` entity utilizes a `user_id` Foreign Key with `CASCADE`. |
| **T4.3** | Update documentation (`design.md`) | Completed | Medium | Relational database schema mapped in documentation. |
| **T4.4** | Implement Access Logging table | Completed | Medium | `access_logs` table tracks all login and logout timestamps. |
| **T5.1** | Create Unit Tests for Room DAOs | Completed | High | Mock tests created for DAO interfaces and Domain Repositories. |
| **T5.2** | Data input format + validations | Completed | High | Strict SQL constraints, unique usernames, and Firebase Auth checks. |
| **T5.3** | Database transaction logging | Completed | Low | `Logcat` entries added for insertions, deletions, and updates. |

---

## 3. Deviations

- **Database Conflicts and Data Loss:** A critical bug was discovered where the use of `OnConflictStrategy.REPLACE` when updating the user profile caused a cascading deletion (`CASCADE DELETE`) of all their trips and preferences. This forced us to divert time to migrate all DAOs to the `@Upsert` strategy.
- **Navigation Instability (Login/Logout):** The transition between authentication states and the Splash screen caused inconsistencies in the navigation backstack (`popUpTo`), resulting in unexpected crashes. The navigation logic had to be redesigned to use a centralized `LaunchedEffect` and stabilize destination IDs.
- **Uniqueness Constraints:** Creating new users after Firebase login failed due to violations of the `UNIQUE` constraint on the `username` field (by using empty strings by default). This was solved by implementing a unique username generator based on the email and a timestamp.

---

## 4. Retrospective

### What went well

- **Migration to Room:** The structure of DAOs and Entities proved to be very solid and scalable.
- **Firebase Integration:** Authentication worked smoothly, and the connection between Firebase and the local database (Room) synchronized correctly.
- **Reactivity:** The use of `Flow` and `StateFlow` from Room to the UI proved excellent for keeping the interface consistently updated without manual calls.

### What didn't work

- **SQLite Conflict Strategies:** We underestimated the impact of using `REPLACE` on tables with Foreign Keys and `ON DELETE CASCADE`. It caused temporary data loss during testing.
- **Dynamic Navigation:** Dynamically changing the `startDestination` based on the authentication state proved to be very fragile in Jetpack Compose Navigation.
- **Global vs. Local State Management:** On some screens (e.g., Settings), asynchronous data collection from the repository conflicted with the local state of the keyboard/cursor.

### What we will improve in the next sprint

- **Constraint Review:** Be much more careful with relational constraints (`CASCADE`, `UNIQUE`) and test edge cases thoroughly (e.g., creating multiple users, editing profiles).
- **Integration Testing:** Add tests that verify not only that the database saves data, but that it isn't accidentally deleted when updating other tables.
- **Session Management:** Keep the authentication navigation logic as simple and predictable as possible

---

## 5. Team Self-Evaluation (0-10)
Score: **10 / 10**

Justification:

- The main goal of the sprint was achieved: the application is now persistent and secure, supporting multiple isolated users.
- Room and Firebase, which are complex technologies, were implemented correctly.
- Although severe critical bugs arose (data loss due to CASCADE, navigation crashes), the team was able to identify them, diagnose the root cause at the database level, and apply robust solutions (`@Upsert`, unique generators) before the sprint closure.
