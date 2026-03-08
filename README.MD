# Shire

## Descripcio del projecte

`Shire` es una aplicacio mobil Android desenvolupada com a projecte de l'assignatura d'Aplicacions Mobils.
L'objectiu del projecte es dissenyar i implementar una app funcional, mantenible i escalable, aplicant bones practiques de desenvolupament, arquitectura i treball en equip.

El repositori inclou:

- Codi font de l'aplicacio Android (modul `app/`)
- Configuracio de compilacio amb Gradle
- Documentacio funcional i tecnica a `docs/`

## Tecnologia utilitzada

- Android (Kotlin)
- Gradle (Kotlin DSL)
- Android Studio

## Equip

Els membres del grup som:

- Paul Lazaro
- Arnau Cribillers

## Estructura del repositori

- `app/`: projecte Android i codi font principal
- `docs/`: diagrames i documents de planificacio/disseny
- `CONTRIBUTING.MD`: guia de contribucio i estrategia de branching

## Estat del projecte

**Versio actual:** `v1.0.0` (prototip funcional de Sprint 01)

**Data d'actualitzacio:** 08/03/2026

### Que esta implementat ara mateix

- Aplicacio Android amb **Jetpack Compose** i **Material 3**.
- Navegacio principal implementada amb `NavHost` (`AppNavigation.kt`) i barra inferior (`BottomNavBar`).
- Pantalles principals disponibles: `TripsScreen`, `HomeScreen`, `HomeScreenFlights`, `HomeScreenRent`, `CreateTripScreen`, `TripDetailsScreen`, `TripGalleryScreen`, `HotelDetailsScreen`, `ProfileScreen`, `AboutScreen`, `TermsScreen`.
- Components UI reutilitzables creats a `ui/components` (cards, botons, camps, barra de progres, etc.).
- Tema visual i configuracio base de disseny a `ui/theme`.
- Documentacio inicial disponible a `docs/` (`DESIGN.MD`, diagrames `.mmd`, planificacio i tancament de sprint).

### Estat funcional

- L'app es pot compilar i executar com a prototip navegable.
- Gran part de les pantalles fan servir **mock data** embeguda (sense backend ni base de dades).
- El flux de creacio de viatge existeix a nivell d'UI, amb passos i seleccio visual d'opcions.
- Hi ha funcionalitats encara no connectades a logica real (accions marcades amb `TODO` en algunes pantalles).

### Limitacions actuals

- No hi ha capa de persistencia (Room, API REST o equivalent) integrada.
- No hi ha autenticacio ni gestio d'usuaris real.
- Encara no s'ha consolidat una arquitectura amb `ViewModel` + repositoris per tota l'app.
- Els tests presents son els generats per defecte (`ExampleUnitTest` i `ExampleInstrumentedTest`), sense cobertura funcional del producte.

### Proxims passos recomanats (Sprint 02)

- Unificar i netejar rutes de navegacio.
- Extreure mock data cap a una capa de dades/repositori.
- Introduir `ViewModel` per separar estat i logica de la UI.
- Definir model de domini estable per viatges, reserves i preferencies.
- Afegir tests unitaris i de UI sobre fluxos critics.