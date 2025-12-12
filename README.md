# 🍹 MixologyCloud - Android Cloud 2025

Projet académique réalisé dans le cadre du module **Android Cloud 2025**. Cette application native démontre une architecture robuste pour générer et sauvegarder des recettes de cocktails aléatoires.

## 🏗 Architecture & Conception
* **UI Layer** : Jetpack Compose (Single Activity), Navigation Compose.
* **Data Layer** : Repository Pattern isolant les sources (API vs BDD).
* **Data Mapping** : Séparation stricte des modèles (`CocktailDto` ↔ `CocktailEntity` ↔ `CocktailData` ↔ `CocktailUi`).

## 🛠 Stack Technique
* **Langage** : Kotlin
* **API** : TheCocktailDB (Random Endpoint)
* **Persistence** : Room Database (Offline support avec timestamp d'insertion)
* **Network** : Retrofit / OkHttp
* **Cloud** : Firebase Messaging & Remote Config (via Repository)

## ✨ Fonctionnalités
1.  **Homepage** : Présentation du groupe.
2.  **Bar Virtuel (API + BDD)** :
    * Tirage aléatoire de cocktails.
    * Sauvegarde automatique en base locale.
    * Liste groupée par date d'ajout (Header dynamique).
    * Détails complets (Instructions, Verre, Catégorie).
3.  **Configuration Cloud** : Écran dédié piloté par Firebase Remote Config.

---
*Développé par Dynastie AMOUSSOU & Mosleh Snoussi*