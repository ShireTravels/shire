# CONTRIBUTING

Aquest document defineix com col.laborem en el repositori i, especialment, l'estrategia de branques de l'equip.

## Estrategia de branching

Fem servir una variant simple de Git Flow amb les branques seguents:

- `main`: branca estable i preparable per entrega.
- `develop`: branca d'integracio de treball diari.
- `feature/<nom-curt>`: noves funcionalitats.
- `fix/<nom-curt>`: correccions de bugs no critics.
- `hotfix/<nom-curt>`: correccions urgents sobre `main`.
- `release/<versio>`: preparacio d'una versio (opc.)

## Flux de treball

1. Actualitza `develop` localment.
2. Crea una branca des de `develop`:
	 - `feature/...` per funcionalitats
	 - `fix/...` per correccions
3. Fes commits petits i descriptius.
4. Obre una Pull Request (PR) cap a `develop`.
5. Un altre membre revisa i aprova la PR.
6. Quan passa revisio i CI, es fa merge.

Per incidencies critiques en produccio:

1. Crea `hotfix/...` des de `main`.
2. Fes la correccio minima necessaria.
3. Obre PR cap a `main` i cap a `develop` (o mergeja `main` a `develop` despres).

## Convencions de noms

- Format: minuscules i guions (`kebab-case`).
- Exemples:
	- `feature/login-google`
	- `fix/crash-settings-screen`
	- `hotfix/payment-timeout`

## Regles de merge

- No es fa push directe a `main` ni a `develop`.
- Tot canvi entra per PR.
- La PR ha de tenir:
	- descripcio clara del canvi
	- referencia a tasca/issue (si existeix)
	- proves minimes (manuals o automatiques)

## Commits

Recomanat usar Conventional Commits:

- `feat: afegeix filtre per categories`
- `fix: resol crash en obrir perfil`
- `docs: actualitza CONTRIBUTING`

## Notes finals

- Manteniu les branques curtes i enfocades.
- Rebase o merge frequent de `develop` per evitar conflictes grans.
- Esborreu branques de feature/fix un cop fusionades.
