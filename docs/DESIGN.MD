# Design

## Analisi de l'arquitectura escollida

L'arquitectura actual del projecte, observant `ui/components` i `ui/screens`, segueix un enfocament de presentacio amb **Jetpack Compose** basat en composicio de pantalles i navegacio declarativa. Es una arquitectura orientada a UI (UI-first), adequada per a un sprint inicial i prototipat rapid.

### 1. Estructura per capes de presentacio

- **Components reutilitzables** (`ui/components`):
	Inclou peces com `ShireButton`, `ShireTextField`, `ShireCard`, `TripCard`, `DestinationCard`, `StepProgressBar`, `BottomNavBar` i `HeaderShire`.
	Aquesta capa aporta consistencia visual i evita duplicar codi de disseny.

- **Pantalles de funcionalitat** (`ui/screens`):
	Inclou pantalles completes com `HomeScreen`, `TripsScreen`, `CreateTripScreen`, `TripDetailsScreen`, `TripGalleryScreen`, `ProfileScreen`, `AboutScreen`, `TermsScreen` i `HotelDetailsScreen`.
	Cada pantalla compon components i concentra la logica de flux de la UI.

### 2. Navegacio centralitzada

La navegacio esta centralitzada a `AppNavigation.kt` amb `NavHost` i `rememberNavController`, dins d'un `Scaffold` amb `BottomNavBar` compartida.

Patrons destacats:
- Navegacio per pestanyes amb `launchSingleTop`, `restoreState` i `popUpTo` per evitar duplicats.
- Rutes parametritzades per detalls (`trip_details/{tripId}`, `trip_gallery/{tripId}`).
- Pantalles filles que tornen enrere amb `popBackStack()`.

### 3. Gestio d'estat

Predomina l'estat local de Compose amb `remember { mutableStateOf(...) }` i `mutableIntStateOf(...)`.

Exemples:
- Formularis de cerca a `HomeScreen`.
- Wizard multi-pas a `CreateTripScreen` amb control de pas actual, viatgers i dates.
- Estat de pestanya a `TripDetailsScreen`.
- Preferencies locals a `ProfileScreen`.

### 4. Models i dades

Els models i dades mock estan incrustats a les propies pantalles (`UpcomingTrip`, `MockHotel`, `MockFlight`, `MockCar`, `MockSpot`, etc.).

Implicacions:
- Avantatge: velocitat de desenvolupament i facilitat per provar dissenys.
- Cost: acoblament entre vista i dades, i menor escalabilitat si creix el projecte.

### 5. Fortaleses de la solucio actual

- Bona separacio entre components reutilitzables i pantalles.
- Navegacio Compose funcional i coherent en la majoria de casos.
- Reutilitzacio visual efectiva (botons, targetes, camps de text, barra de progrés).
- Arquitectura clara per a una fase de MVP/prototip.

### 6. Riscos i punts de millora

- No s'observa capa `ViewModel` ni capa de domini/dades en aquests paquets.
- Part de la logica de negoci es troba directament als composables de pantalla.
- Inconsistencies de rutes:
	- Es navega a `"hoteles"` des de `HomeScreenFlights` i `HomeScreenRent`, pero aquesta ruta no esta definida a `AppNavigation`.
	- Conviuen `"tripsScreen"` i `"trips"` com a rutes separades per la mateixa pantalla.

### 7. Conclusio

L'arquitectura escollida es **modular en la capa de presentacio** i ben orientada a iteracio rapida de UI amb Compose. Per evolucionar cap a una base mes robusta, el pas natural seria introduir `ViewModel` per pantalla, extreure dades mock a repositoris i unificar contractes de navegacio (rutes constants i sense duplicats).