# Sprint 04 – Planning Document: Travel Planner Remote Persistence

## 1. Sprint Goal & Objectives

> **Goal:** Integrate **Retrofit** to connect the app with a REST API for hotel reservations hosted at `http://15.224.84.148:8090`. Implement hotel search, booking, reservation listing & cancellation, and image gallery management for each trip. By the end of this sprint, users should be able to search for hotels, book rooms, save reservations locally, and manage trip-specific image galleries.

### Objectives Checklist
- [ ] **Retrofit Integration:** Add Retrofit dependency, configure HTTP client, create data models and API interfaces.
- [ ] **Hotel Search & Booking:** Implement search screens for London, Paris, Barcelona with date pickers; display hotel and room availability.
- [ ] **Reservation Persistence:** Save booking information locally in the database as a new trip.
- [ ] **Image Management:** Display hotel/room images in search and booking screens; create image gallery feature per trip.
- [ ] **Reservation Management:** List all reservations, allow deletion locally and via API; update "My Trips" screen.
- [ ] **Architecture:** Follow MVVM structure with Repository pattern, use HILT for Dependency Injection, and ROOM for persistence.
- [ ] **Testing:** Create unit tests mocking the remote connection.

---

## 2. Work Breakdown & Task Assignment

### T1. Retrofit Configuration (3 Points)
- [ ] **T1.1** Add Retrofit dependency and configure the HTTP client.
- [ ] **T1.2** Create necessary data models and API interfaces to access the hotel API implementing MVVM structure.
- [ ] **T1.3** Create a repository layer to abstract the API usage.
- [ ] **T1.4** Create unit tests mocking the remote connection.

### T2. Search and Booking Screens (5 Points)
- [ ] **T2.1** Create a screen to search for hotels in London, Paris, or Barcelona, with city, start and end dates (using date pickers).
- [ ] **T2.2** Show hotel and room data returned by the API (typically 3 rooms per hotel).
- [ ] **T2.3** Allow users to book a room and save the reservation info (ID, room, hotel, price, etc) locally in a trip table or create a new trip.
- [ ] **T2.4** Display all images of the hotel and rooms on the booking screen.
- [ ] **T2.5** Implement hotel/room details screen with a button to book a room (follow RemotePersistence.zip example structure).

### T3. Add Images / Gallery to Trip (4 Points)
- [ ] **T3.1** Allow the user to attach multiple images to a trip.
- [ ] **T3.2** Save images locally in the device database or storage.
- [ ] **T3.3** Display trip-specific galleries in the trip details screen.

### T4. List and Cancel Reservations (3 Points)
- [ ] **T4.1** Create a screen to list all local reservations indicating the trip related.
- [ ] **T4.2** Add functionality to delete a reservation locally and via API (if required).
- [ ] **T4.3** Show the associated hotel and room images in the list (follow RemotePersistence.zip example).
- [ ] **T4.4** Update the "My Trips" screen to indicate whether a trip includes a hotel reservation, and display the corresponding reservation details.

### T5. Testing and Debugging (1 Point - Part of above tasks)
- [ ] **T5.1** Write Unit Tests for API calls and repository layer.
- [ ] **T5.2** Implement proper error handling and logging for API responses.
- [ ] **T5.3** Test database persistence with image storage.

---

## 3. Sprint Backlog & Planning Table

| ID | Task Description | Responsable | Est. (h) | Estado | Prioridad |
|:---|:---|:---|:---:|:---:|:---|
| **T1.1** | Add Retrofit dependency & configure HTTP client | Arnau | 1.0 | `To Do` | High |
| **T1.2** | Create data models & API interfaces (MVVM) | Arnau | 1.5 | `To Do` | High |
| **T1.3** | Create repository layer for API abstraction | Arnau | 1.0 | `To Do` | High |
| **T1.4** | Create unit tests mocking remote connection | Arnau | 0.5 | `To Do` | Medium |
| **T2.1** | Create hotel search screen with date pickers | Arnau | 2.0 | `To Do` | High |
| **T2.2** | Display hotel and room data from API | Arnau | 1.5 | `To Do` | High |
| **T2.3** | Implement booking logic & save reservation locally | Arnau | 2.5 | `To Do` | High |
| **T2.4** | Display hotel and room images in screens | Arnau | 1.5 | `To Do` | High |
| **T2.5** | Implement hotel/room details screen with booking button | Paul | 1.5 | `To Do` | High |
| **T3.1** | Allow users to attach multiple images to trips | Paul | 1.5 | `To Do` | Medium |
| **T3.2** | Save images locally in device database/storage | Paul | 2.0 | `To Do` | High |
| **T3.3** | Display trip-specific galleries in details screen | Paul | 1.5 | `To Do` | Medium |
| **T4.1** | Create screen to list all reservations | Paul | 1.5 | `To Do` | High |
| **T4.2** | Implement delete reservation locally & via API | Paul | 1.5 | `To Do` | High |
| **T4.3** | Show hotel/room images in reservation list | Paul | 1.0 | `To Do` | Medium |
| **T4.4** | Update "My Trips" screen with reservation info | Paul | 1.5 | `To Do` | Medium |
| **T5.1** | Write unit tests for API calls | Arnau & Paul | 1.0 | `To Do` | High |
| **T5.2** | Implement error handling & logging | Arnau & Paul | 1.0 | `To Do` | Medium |
| **T5.3** | Test database persistence with images | Arnau & Paul | 0.5 | `To Do` | Medium |

---

## 4. Definition of Done (DoD)

- [ ] **Architecture:** MVVM pattern is followed with Repository layer for API abstraction and HILT for Dependency Injection.
- [ ] **Database:** ROOM persistence is implemented for storing reservations and images locally.
- [ ] **Retrofit Integration:** All API calls are properly implemented with error handling and fallback mechanisms.
- [ ] **Folder Structure:** Project includes at least: `view/`, `viewmodel/`, `repo/`, `di/`, `data/` folders.
- [ ] **Date Handling:** All date and time fields are implemented using date pickers.
- [ ] **Images:** Hotel, room, and trip images are properly displayed and stored locally.
- [ ] **Testing:** Unit tests cover API calls and critical business logic.
- [ ] **Delivery:** GitHub repository with v4.x.x release tag, Sprint.md with task assignments, and video demonstration in `/docs/evidence/v4.x.x/`.

---

## 5. API Reference

**Base URL:** `http://15.224.84.148:8090`

**Main Endpoints:**
- **Search Hotels:** GET `/api/hotels/availability?city={city}&startDate={date}&endDate={date}`
  - Cities: London, Paris, Barcelona
  - Returns: List of hotels with available rooms
- **Get Hotel Details:** GET `/api/hotels/{hotelId}`
  - Returns: Hotel information and room details with images
- **Create Reservation:** POST `/api/reservations`
  - Payload: { hotelId, roomId, startDate, endDate, userName }
  - Returns: Reservation confirmation
- **List Reservations:** GET `/api/reservations?userId={userId}`
- **Cancel Reservation:** DELETE `/api/reservations/{reservationId}`

---

## 6. Identified Risks

- **API Connectivity:** Network unavailability during testing; implement proper error handling and offline mode support.
- **Image Loading:** Large image files may cause UI lag; implement lazy loading and caching mechanisms.
- **Database Sync:** Keeping local reservations and remote API state in sync; implement proper transaction handling.
- **Date Picker Implementation:** Ensure date pickers are properly integrated across different Android API levels.
- **Image Storage:** Device storage limitations for large image collections; implement proper cleanup and compression.

---

## 7. Task Division Summary

**Arnau (Retrofit Configuration & Search/Booking - ~13.5 hours):**
- T1: Retrofit Configuration (dependencies, models, interfaces, unit tests)
- T2.1-T2.4: Search and Booking Screens (search UI, API data display, booking logic, images display)

**Paul (Details Screen & Reservation Management - ~13.5 hours):**
- T2.5: Hotel/Room Details Screen (with booking button)
- T3: Image Gallery Management (attach, save, display trip images)
- T4: Reservation Listing & Cancellation (list screen, delete functionality, "My Trips" updates)
- Shared: T5.1, T5.2, T5.3 (testing, error handling, database persistence testing)

---

## 8. Resources & References

- **Retrofit Documentation:** https://square.github.io/retrofit/
- **MVVM Pattern:** Android Architecture Components
- **HILT Dependency Injection:** https://developer.android.com/training/dependency-injection/hilt-android
- **ROOM Database:** https://developer.android.com/topic/libraries/architecture/room
- **Image Loading:** Glide or Coil libraries recommended
- **Class Example:** RemotePersistence.zip (provided by professor)

---

## 9. LEFT TO DO
- [ ] Finalize API endpoint specifications with professor
- [ ] Set up Retrofit client configuration
- [ ] Design database schema for reservations and images
- [ ] Create UI mockups for search and booking screens
