# MixologyCloud

**Projet étudiant - Android Cloud 2025**

Application Android moderne pour découvrir et gérer une collection de cocktails avec intégration Firebase.

## 👥 Membres du Groupe
- [Nom Membre 1]
- [Nom Membre 2]

## 📱 Fonctionnalités

### Feature 1: Homepage
Écran d'accueil simple avec navigation vers les features principales.

### Feature 2: Liste de Cocktails (API + Room)
- Récupération de cocktails aléatoires via **TheCocktailDB API**
- Stockage local avec **Room Database**
- Affichage groupé par jour d'insertion
- Détails complets de chaque cocktail (image, instructions, type de verre)

### Feature 3: Firebase Integration
- **Firebase Cloud Messaging** pour les notifications push
- **Firebase Remote Config** pour la configuration dynamique de l'UI
- Architecture propre avec FirebaseRepository

## 🏗️ Architecture

Le projet suit une **Clean Architecture adaptée** avec séparation stricte des couches:

```
app/
├── data/
│   ├── local/          # Room Database, DAO, Entity
│   ├── remote/         # Retrofit API, DTO
│   ├── repository/     # Repositories (Cocktail, Firebase)
│   ├── model/          # ModelData (Objet métier)
│   └── mapper/         # Mappers entre couches
├── ui/
│   ├── screens/        # Composables (Home, List, Detail, Firebase)
│   ├── viewmodel/      # ViewModels
│   ├── model/          # ModelUi (Objet UI)
│   ├── navigation/     # Navigation Compose
│   └── theme/          # Theme Material3
├── di/                 # Injection Hilt
└── service/            # Firebase Messaging Service
```

### Flux de Données
```
API/Firebase → DTO/RemoteConfig → Data → Entity/Cache → Data → UI
```

### Mappers Explicites
- `CocktailDto` → `CocktailData`
- `CocktailEntity` → `CocktailData`
- `CocktailData` → `CocktailUi`

## 🛠️ Stack Technique

- **Langage**: Kotlin
- **UI**: Jetpack Compose (Single Activity)
- **Navigation**: Navigation Compose
- **Min SDK**: 26 (Android 8.0)
- **Compile SDK**: 35
- **Database**: Room
- **Network**: Retrofit + OkHttp
- **Dependency Injection**: Hilt
- **Firebase**: Messaging, Remote Config
- **Images**: Coil
- **Coroutines**: Flow, StateFlow

## 📦 Dépendances Principales

```kotlin
// Compose
androidx.compose.material3
androidx.navigation:navigation-compose

// Room
androidx.room:room-ktx

// Retrofit
com.squareup.retrofit2:retrofit
com.squareup.retrofit2:converter-gson

// Hilt
com.google.dagger:hilt-android
androidx.hilt:hilt-navigation-compose

// Firebase
com.google.firebase:firebase-messaging-ktx
com.google.firebase:firebase-config-ktx

// Coil
io.coil-kt:coil-compose
```

## 🚀 Setup

1. Cloner le repository:
```bash
git clone [URL_DU_REPO]
cd MixologyCloud
```

2. Ajouter le fichier `google-services.json` dans `app/`:
   - Créer un projet Firebase sur https://console.firebase.google.com
   - Télécharger le fichier de configuration
   - Le placer dans `app/google-services.json`

3. Configurer Firebase Remote Config:
   - Accéder à la console Firebase
   - Ajouter les paramètres:
     - `primary_color` (String): "#FF6200EE"
     - `welcome_message` (String): "Bienvenue sur MixologyCloud"
     - `feature_enabled` (Boolean): true

4. Sync Gradle et Run

## 📝 API Utilisée

**TheCocktailDB**
- Endpoint: `https://www.thecocktaildb.com/api/json/v1/1/random.php`
- Documentation: https://www.thecocktaildb.com/api.php

## 🎨 Principes Respectés

- **KISS** (Keep It Simple, Stupid)
- **DRY** (Don't Repeat Yourself)
- **Single Responsibility Principle**
- **Separation of Concerns**
- **Clean Architecture**

## 📚 Documentation Complète

Pour plus de détails, consultez la documentation dans le dossier **[`docs/`](docs/)**:

- **[QUICKSTART.md](docs/QUICKSTART.md)** - Guide de démarrage rapide (5 min)
- **[ARCHITECTURE.md](docs/ARCHITECTURE.md)** - Architecture détaillée
- **[CHECKLIST.md](docs/CHECKLIST.md)** - Checklist de validation
- **[PATTERNS.md](docs/PATTERNS.md)** - Design patterns utilisés
- **[SUMMARY.md](docs/SUMMARY.md)** - Résumé exécutif

## 📄 License

Projet étudiant - Tous droits réservés

## 🔗 Liens Utiles

- [Android Developers](https://developer.android.com/)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Firebase Documentation](https://firebase.google.com/docs)
- [TheCocktailDB API](https://www.thecocktaildb.com/api.php)
