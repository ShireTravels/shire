# Shire Travels - Technical Design Documentation

This document describes the technical architecture and data persistence layer implemented in Shire Travels.

## 1. Persistence Architecture

The application uses **SQLite** as the primary local storage engine, managed through the **Room Persistence Library**. The architecture is fully reactive, utilizing Kotlin **Flow** to ensure real-time UI updates when the database state changes.

### Key Components:
- **Room Database:** `ShireRoomDatabase` manages the database instance and provides access to DAOs.
- **DAOs (Data Access Objects):** Define SQL queries and map them to Kotlin functions. Most retrieval operations return `Flow<T>` or `Flow<List<T>>`.
- **Entities:** Data classes annotated with `@Entity` representing the database tables.
- **Type Converters:** `ShireTypeConverters` handles complex types like `LocalDate`, `LocalTime`, and `HashMap` by converting them to/from JSON or primitive types.

## 2. Database Schema

### User Management
#### `users` Table
Stores user profile information and links local data to Firebase Authentication.
| Column | Type | Description |
|:---|:---|:---|
| `id` | INTEGER (PK) | Primary Key (Auto-generated) |
| `name` | TEXT | User's full name |
| `email` | TEXT (Unique) | User's email address |
| `password_hash`| TEXT | (Legacy) Local password hash |
| `created_at` | INTEGER | Timestamp of account creation |
| `login` | TEXT | User login identifier |
| `username` | TEXT (Unique) | Unique display username |
| `birthdate` | TEXT | Date of birth |
| `address` | TEXT | Physical address |
| `country` | TEXT | Country of residence |
| `phone` | TEXT | Contact phone number |
| `receive_emails`| INTEGER | Boolean (0/1) for email preferences |

### Trip & Itinerary
#### `trips` Table
Stores the main trip information. Linked to a specific user.
| Column | Type | Description |
|:---|:---|:---|
| `id` | INTEGER (PK) | Primary Key (Auto-generated) |
| `user_id` | INTEGER (FK) | Reference to `users.id` (On Delete Cascade) |
| `title` | TEXT | Trip name/destination |
| `start_date` | TEXT | Start date (dd/MM/yyyy) |
| `end_date` | TEXT | End date (dd/MM/yyyy) |
| `price` | REAL | Total estimated price |
| `description` | TEXT | Notes and details |

#### `activities` Table
Individual itinerary items within a trip.
| Column | Type | Description |
|:---|:---|:---|
| `id` | INTEGER (PK) | Primary Key (Auto-generated) |
| `trip_id` | INTEGER (FK) | Reference to `trips.id` (On Delete Cascade) |
| `title` | TEXT | Activity name |
| `description` | TEXT | Activity details |
| `date` | TEXT | Activity date |
| `time` | TEXT | Activity time |
| `price` | REAL | Cost of the activity |

### Logging & Preferences
#### `access_logs` Table
Audit trail for user sessions.
| Column | Type | Description |
|:---|:---|:---|
| `id` | INTEGER (PK) | Primary Key (Auto-generated) |
| `user_id` | INTEGER (FK) | Reference to `users.id` |
| `action` | TEXT | "LOGIN" or "LOGOUT" |
| `timestamp` | INTEGER | Event timestamp |

#### `preferences` Table
Application settings for a specific user.
| Column | Type | Description |
|:---|:---|:---|
| `user_id` | INTEGER (PK/FK) | Reference to `users.id` |
| `language` | TEXT | UI Language (EN, ES, etc.) |
| `currency` | TEXT | Display currency (EUR, USD, etc.) |
| `theme` | TEXT | App theme (LIGHT, DARK) |

### Catalog Tables
- `hotels`: Repository of available hotels.
- `flights`: Repository of flight options.
- `cars`: Rental car options.
- `places`: Tourist attractions and points of interest.

## 3. Relationships & Constraints
- **Multi-user Support:** All `trips`, `access_logs`, and `preferences` are strictly linked to a `user_id`.
- **Cascading Deletes:** Deleting a user automatically removes all their trips, logs, and preferences. Deleting a trip automatically removes its associated activities.
- **Uniqueness:** Email and Username are enforced as unique at the database level.

## 4. Reactive Data Flow
1. **DAO Retrieval:** Returns `Flow<T>`.
2. **Repository:** Maps DB entities to Domain models and exposes them as `Flow`.
3. **ViewModel:** Collects the `Flow` in `viewModelScope` and updates a `StateFlow` for the UI.
4. **UI (Compose):** Observes the `StateFlow` and recomposes automatically on data changes.
