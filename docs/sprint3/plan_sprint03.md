# Sprint 03 – Planning Document: Persistence & Authentication

## 1. Sprint Goal & Objectives

> **Goal:** Transition the app from in-memory storage to persistent local storage using **SQLite (Room Database)** and implement user authentication using **Firebase**. By the end of this sprint, data should be persistent across app sessions and securely linked to individual user accounts.

### Objectives Checklist
- [ ] **Data Persistence:** Replace in-memory storage with Room (DAO, Entities, Database).
- [ ] **Authentication:** Integrate Firebase Auth for User Login, Registration, and Password Recovery.
- [ ] **Data Privacy:** Ensure CRUD operations for trips and itineraries are restricted to the logged-in user.
- [ ] **Robustness:** Implement comprehensive data validation and use Logcat for deep monitoring.
- [ ] **QA & Docs:** Test database functionality (Unit Tests) and document the DB schema.

---

## 2. Work Breakdown & Task Assignment

### T1. Implement SQLite Persistence using Room (5 Points)
- [ ] **T1.1** Create Room Database class.
- [ ] **T1.2** Define Entities for `Trip` and `ItineraryItem` (must contain datetime, text, and integer).
- [ ] **T1.3** Create Data Access Objects (DAOs) for database operations.
- [ ] **T1.4** Implement CRUD operations using DAOs for trips and itineraries.
- [ ] **T1.5** Modify ViewModels to use Room Database instead of in-memory storage.
- [ ] **T1.6** Ensure UI automatically updates when the database changes.

### T2. Login and Logout (3 Points)
- [ ] **T2.1** Connect the Android app to Firebase.
- [ ] **T2.2** Design Android screen (Login form).
- [ ] **T2.3** Implement Firebase login functionality (Auth & Password).
- [ ] **T2.4** Create an action allowing the user to log out securely.
- [ ] **T2.5** Use Logcat to track auth operations and potential errors.

### T3. Register and Recover Password (4 Points)
- [ ] **T3.1** Design Android screen (Register form).
- [ ] **T3.2** Implement Firebase registration (Auth & Password) + Email Verification (using Repository pattern).
- [ ] **T3.3** Implement the UI and actions for password recovery.

### T4. Persist User Information and Trips (3 Points)
- [ ] **T4.1** Persist user information in the local DB (`User` table with at least: login, username, birthdate, address, country, phone, receive_emails boolean). Must validate unique username.
- [ ] **T4.2** Restructure `Trip` table to support multiple users; show only trips belonging to the logged-in user.
- [ ] **T4.3** Update `design.md` documentation with the new database schema and usage details.
- [ ] **T4.4** Persist application access logs: Table logging every User ID login/logout alongside the datetime.

### T5. Testing and Debugging (2 Points)
- [ ] **T5.1** Write Unit Tests for DAOs and database interactions.
- [ ] **T5.2** Implement Data Validation (e.g., prevent duplicate trip names, validate correct dates).
- [ ] **T5.3** Use Logcat effectively to trace runtime database operations and errors.

---

## 3. Sprint Backlog & Planning Table

| ID | Task Description | Responsable | Est. (h) | Estado | Prioridad |
|:---|:---|:---|:---:|:---:|:---|
| **T1.1** | Setup Room Database class | Arnau | 1.0 | `To Do` | High |
| **T1.2** | Define Room Entities (Trip, ItineraryItem) | Arnau | 1.0 | `To Do` | High |
| **T1.3** | Create DAOs for entities | Arnau | 1.0 | `To Do` | High |
| **T1.4** | Implement CRUD logic with DAOs | Arnau | 2.5 | `To Do` | High |
| **T1.5** | Migrate ViewModels to Room | Arnau | 2.0 | `To Do` | High |
| **T1.6** | Bind UI to database state changes | Arnau | 1.5 | `To Do` | Medium |
| **T2.1** | Firebase Project & App Connection | Paul | 0.5 | `To Do` | High |
| **T2.2** | Design Login Screen UI | Paul | 1.0 | `To Do` | Medium |
| **T2.3** | Implement Login logic (Firebase Auth) | Paul | 2.0 | `To Do` | High |
| **T2.4** | Implement Logout action | Paul | 0.5 | `To Do` | Medium |
| **T2.5** | Add Logcat for auth monitoring | Paul | 0.5 | `To Do` | Low |
| **T3.1** | Design Registration Screen UI | Arnau | 1.0 | `To Do` | Medium |
| **T3.2** | Implement Registration & Email Verification | Arnau | 2.5 | `To Do` | High |
| **T3.3** | Design & Logic for Password Recovery | Arnau | 1.5 | `To Do` | Medium |
| **T4.1** | Implement `User` table (local schema + UI validations) | Paul | 1.5 | `To Do` | High |
| **T4.2** | Link Trips to specific User IDs | Paul | 1.5 | `To Do` | High |
| **T4.3** | Update documentation (`design.md`) | Paul | 1.0 | `To Do` | Medium |
| **T4.4** | Implement Application Access Logging table | Paul | 1.0 | `To Do` | Medium |
| **T5.1** | Create Unit Tests for Room DAOs | Paul & Arnau | 2.0 | `To Do` | High |
| **T5.2** | Data input format + boundary validations | Paul & Arnau | 1.5 | `To Do` | High |
| **T5.3** | Database transaction logging (Logcat) | Paul & Arnau | 0.5 | `To Do` | Low |

---

## 4. Definition of Done (DoD)

- [ ] **Architecture:** Fake / In-Memory datasets are completely deleted. The app strictly reads/writes from the Room Database.
- [ ] **Security/Auth:** A user cannot bypass the login screen without valid credentials. Only their valid Trips are displayed.
- [ ] **Stability:** The app doesn't crash on database migrations or asynchronous query executions. Views appropriately wait for database responses.
- [ ] **Documentation:** `design.md` reflects the final relational database schema (User, Trip, ItineraryItem, Access Logs).
- [ ] **Evidence:** A video demonstration covers all CRUD capabilities and Auth functionalities and is uploaded to `/doc/evidence/`.

---

## 5. Identified Risks

- **Firebase Firebase Integration:** Sometimes can be tricky to set up third parties auth services, because not only you have to configure/implement it in your code, but you also have to configure it from the auth provider platform
- **Room Threading Issues:** Trying to access the Room database on the Main Thread (UI thread) might cause an application crash. All DAO calls need correct coroutine context.
- **Constraint Violations:** Updating trips that have relational strictness without handling SQLite exceptions gracefully.
