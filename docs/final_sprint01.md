# Sprint 01 – Execution & Review

## 1. Resultados obtenidos

Comparación con Sprint Goal:

- Sprint orientado a construir un MVP navegable en Android con Compose.
- Resultado principal: objetivo conseguido de forma **parcial-alta**.
- Se consolidó una base funcional de UI con navegación entre pantallas.
- Se incorporaron componentes reutilizables y mejoras de estilo.
- Se añadieron logo/splash y documentación técnica inicial.
- Parte de las tareas de dominio, persistencia y tests quedaron pendientes.

Evidencia en commits recientes:

- Implementación de navegación y nuevas pantallas (`a541c5c`, `93f9ad4`).
- Creación de componentes reutilizables (`e29b708`, `ed10d03`).
- Mejoras visuales y estilo global (`83188ee`, `154b640`).
- Logo y splash screen (`d2d490c`).
- DatePicker y refinamientos de flujo (`7c85049`).
- Actualización de documentación y diagrama de clases (`470b3b5`, `c23ad2f`).

---

## 2. Tareas completadas

| ID | Completada | Comentarios |
|----|------------|-------------|
| 1.1 Añadir documentación inicial | Sí | README y documentos en `docs/` creados y actualizados. |
| 1.2 Crear contributing y completarlo | Sí | `CONTRIBUTING.MD` presente en el repositorio. |
| 1.3 Home | Sí | Pantalla base implementada en Compose. |
| 1.4 Menu | Sí | Barra inferior y navegación principal operativas. |
| 1.5 Navigation | Sí | `AppNavigation` con rutas funcionales. |
| 1.6 Trip | Sí | `TripsScreen` y `TripDetailsScreen` implementadas. |
| 1.7 Itinerary | Parcial | Hay UI e información mock en detalle de viaje; falta lógica de negocio real. |
| 1.8 User preference | Parcial | `ProfileScreen` implementada a nivel de UI. |
| 1.9 Mock data for initial layout | Sí | Mock data integrada en varias pantallas. |
| 1.10 Implementar navegación | Sí | Navegación Compose integrada entre secciones principales. |
| 1.11 Crear diagrama de clases | Sí | `docs/class_diagram.mmd` actualizado. |
| 1.12 Implementar modelo de clases y funciones | Parcial | Existen data classes de UI/mock, falta capa de dominio consolidada. |
| 1.13 Create logo | Sí | Recursos de launcher/logo incorporados. |
| 1.14 Create name | Sí | Nombre del proyecto/app definido como Shire. |
| 1.15 Create repo | Sí | Repositorio creado y operativo. |
| 1.16 Configure repo | Sí | Estructura base y flujo de trabajo de git en uso. |
| 1.17 Crear splash screen | Sí | Recurso `splash_logo.png` y dependencia de splash presentes. |
| 1.18 About page | Sí | `AboutScreen` implementada. |
| 1.19 Terms & Conditions | Sí | `TermsScreen` implementada. |
| 1.20 Preferences screen | Parcial | Pantalla de preferencias en UI, sin persistencia completa. |

---

## 3. Desviaciones

- Se priorizó cerrar una experiencia visual completa y navegable frente a terminar capas de dominio/datos.
- Se invirtió más tiempo del previsto en estilo, componentes reutilizables y consistencia visual.
- Parte del trabajo se centró en estabilizar navegación y estructura de pantallas antes de añadir persistencia.
- Algunos requisitos quedaron en estado parcial por alcance del sprint: lógica real de negocio, almacenamiento y test funcional.

---

## 4. Retrospectiva

### Qué funcionó bien

- Iteración rápida con Jetpack Compose para construir y validar pantallas.
- Buena mejora de reutilización al extraer componentes UI.
- Historial de commits frecuente que facilita el seguimiento del progreso.
- Integración temprana de documentación y diagrama.

### Qué no funcionó

- Estimación optimista para tareas de arquitectura y lógica de dominio.
- Demasiadas decisiones de detalle visual en Sprint 01.
- Cobertura de testing prácticamente inexistente para funcionalidades reales.
- Persisten pequeñas inconsistencias de rutas y responsabilidades entre pantallas.

### Qué mejoraremos en el próximo sprint

- Definir alcance más estricto por sprint (MVP funcional + criterios de cierre medibles).
- Introducir `ViewModel` y repositorios para desacoplar UI y datos.
- Mover mock data fuera de pantallas hacia capa de datos.
- Añadir pruebas unitarias de lógica y pruebas UI de flujos críticos.
- Revisar navegación con constantes de rutas para evitar duplicidades.

---

## 5. Autoevaluación del equipo (0-10)
Nota: **7.5 / 10**

Justificación:

- El sprint entrega una base sólida y demostrable de la app (UI, navegación y experiencia general).
- Se cumplieron la mayoría de objetivos visibles del producto y de documentación.
- Las principales carencias están en arquitectura de dominio, persistencia y tests, que pasan como foco técnico al Sprint 02.