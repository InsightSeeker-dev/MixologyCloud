# ⚡ MixologyCloud - Quick Start Guide

## 🚀 Setup en 5 Minutes

### 1️⃣ Renommer les Fichiers de Configuration (2 min)

Dans le dossier racine du projet:

```powershell
# Renommer les fichiers de configuration
Rename-Item "MixologyCloud_build.gradle.kts" "app/build.gradle.kts"
Rename-Item "MixologyCloud_project_build.gradle.kts" "build.gradle.kts"
Rename-Item "MixologyCloud_AndroidManifest.xml" "app/src/main/AndroidManifest.xml"
Rename-Item "MixologyCloud_gitignore.txt" ".gitignore"
Rename-Item "MixologyCloud_README.md" "README.md"
```

**OU** Manuellement:
- Copier le contenu des fichiers `MixologyCloud_*` vers leurs destinations
- Supprimer les fichiers temporaires

### 2️⃣ Configuration Firebase (2 min)

1. **Console Firebase**: https://console.firebase.google.com
   - Créer un nouveau projet "MixologyCloud"
   - Ajouter une application Android
   - Package: `com.example.mixologycloud`

2. **Télécharger** `google-services.json`
   - Le placer dans `app/google-services.json`

3. **Remote Config** (Firebase Console):
   ```
   primary_color      = "#FF6200EE"     (String)
   welcome_message    = "Bienvenue!"    (String)
   feature_enabled    = true            (Boolean)
   ```
   - Publier les changements

### 3️⃣ Personnalisation (1 min)

**Modifier** `app/src/main/java/com/example/mixologycloud/ui/screens/HomeScreen.kt`:

```kotlin
Text(
    text = "• [Votre Nom]\n• [Nom Collègue]",  // LIGNE 48
    style = MaterialTheme.typography.bodyLarge,
    textAlign = TextAlign.Center
)
```

### 4️⃣ Build & Run

```bash
# Sync Gradle
./gradlew clean

# Build
./gradlew assembleDebug

# Run (avec device/emulator connecté)
./gradlew installDebug
```

**OU** dans Android Studio:
- File → Sync Project with Gradle Files
- Run → Run 'app'

---

## 🌳 Git - Premier Commit

```bash
# Initialiser
git init
git add .
git commit -m "Initial commit: MixologyCloud Clean Architecture"

# Remote (si déjà créé sur GitHub)
git remote add origin https://github.com/[USER]/MixologyCloud.git
git branch -M main
git push -u origin main
```

---

## 📱 Test Rapide des Features

### ✅ Feature 1: Homepage
1. Lancer l'app
2. Vérifier l'affichage des noms
3. Tester les 2 boutons de navigation

### ✅ Feature 2: Cocktails
1. Cliquer "Liste de Cocktails"
2. Appuyer sur "Add Random" (3-4 fois)
3. Vérifier le groupement par date
4. Cliquer sur un cocktail → Voir le détail
5. Tester "Delete All"

### ✅ Feature 3: Firebase
1. Cliquer "Firebase Features"
2. Vérifier que le header affiche le message de Remote Config
3. Vérifier la couleur dynamique
4. Voir le FCM Token

---

## 🐛 Troubleshooting Express

### Erreur: "google-services.json not found"
```bash
# Vérifier le fichier
ls app/google-services.json

# Si absent, télécharger depuis Firebase Console
```

### Erreur: "Unresolved reference: Hilt"
```bash
# Sync Gradle
./gradlew clean build
```

### L'API ne répond pas
- Vérifier la connexion Internet
- Tester l'URL: https://www.thecocktaildb.com/api/json/v1/1/random.php

### Les images ne s'affichent pas
- Vérifier la permission INTERNET dans AndroidManifest
- Vérifier Coil dependency dans build.gradle

### Room ne persiste pas
```kotlin
// Vérifier dans AppModule.kt que la database est bien en Singleton
@Provides
@Singleton
fun provideMixologyDatabase(...)
```

---

## 📂 Structure Minimale à Comprendre

```
app/src/main/java/com/example/mixologycloud/
├── data/
│   ├── local/       → Room (Database, DAO, Entity)
│   ├── remote/      → API (Service, DTO)
│   ├── repository/  → Logique (Repository)
│   ├── model/       → Data models
│   └── mapper/      → Transformations
│
├── ui/
│   ├── screens/     → Composables
│   ├── viewmodel/   → ViewModels + States
│   └── navigation/  → Routes
│
└── di/              → Hilt config
```

---

## 🎯 Checklist Minimal

- [ ] Firebase configuré (google-services.json)
- [ ] Remote Config avec 3 paramètres
- [ ] Noms des membres mis à jour
- [ ] App compile sans erreur
- [ ] Les 3 features fonctionnent
- [ ] Code commit sur Git

---

## 🔗 Liens Utiles Rapides

- **API Cocktails**: https://www.thecocktaildb.com/api.php
- **Firebase Console**: https://console.firebase.google.com
- **Android Compose**: https://developer.android.com/jetpack/compose
- **Room Database**: https://developer.android.com/training/data-storage/room
- **Hilt**: https://developer.android.com/training/dependency-injection/hilt-android

---

## 💡 Commandes Git Utiles

```bash
# Statut
git status

# Ajouter tous les fichiers modifiés
git add .

# Commit
git commit -m "Description du changement"

# Push
git push

# Pull (avant de commencer à travailler)
git pull

# Créer une branche
git checkout -b feature/ma-feature

# Changer de branche
git checkout main

# Merge
git merge feature/ma-feature

# Voir l'historique
git log --oneline
```

---

## 🤝 Workflow Collaboratif

### Avant de commencer à coder:
```bash
git pull
git checkout -b feature/nom-feature
```

### Après avoir terminé:
```bash
git add .
git commit -m "Description claire"
git push origin feature/nom-feature
```

### Sur GitHub:
- Créer une Pull Request
- Demander une review au collègue
- Merger après validation

---

## 📸 Captures d'Écran à Prendre

1. Homepage avec noms des membres
2. Liste de cocktails avec groupement par date
3. Détail d'un cocktail
4. Écran Firebase avec Remote Config
5. (Bonus) Notification reçue

---

## 🎓 Documentation Complète

Pour plus de détails, voir:
- `README.md` - Vue d'ensemble
- `ARCHITECTURE.md` - Architecture détaillée
- `PATTERNS.md` - Design patterns utilisés
- `CHECKLIST.md` - Checklist exhaustive
- `FILE_TREE.txt` - Arborescence complète

---

**Bonne chance! 🚀**

Si vous rencontrez un problème, vérifiez d'abord:
1. Gradle sync effectué?
2. google-services.json présent?
3. Internet connecté?
4. Device/Emulator lancé?
