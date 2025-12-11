# ✅ Checklist MixologyCloud - Setup & Validation

## 📋 Configuration Initiale

### Étape 1: Setup Firebase
- [ ] Créer un projet Firebase sur console.firebase.google.com
- [ ] Ajouter une application Android (package: `com.example.mixologycloud`)
- [ ] Télécharger `google-services.json`
- [ ] Placer `google-services.json` dans `app/`
- [ ] Vérifier que `google-services.json` est dans `.gitignore`

### Étape 2: Configuration Firebase Remote Config
- [ ] Aller dans Firebase Console → Remote Config
- [ ] Ajouter paramètre `primary_color` (String): `"#FF6200EE"`
- [ ] Ajouter paramètre `welcome_message` (String): `"Bienvenue sur MixologyCloud"`
- [ ] Ajouter paramètre `feature_enabled` (Boolean): `true`
- [ ] Publier les changements

### Étape 3: Configuration Firebase Messaging
- [ ] Aller dans Firebase Console → Cloud Messaging
- [ ] Activer Firebase Cloud Messaging API
- [ ] (Optionnel) Configurer les clés serveur pour envoyer des notifications

### Étape 4: Gradle & Dependencies
- [ ] Copier `MixologyCloud_project_build.gradle.kts` → `build.gradle.kts` (root)
- [ ] Copier `MixologyCloud_build.gradle.kts` → `app/build.gradle.kts`
- [ ] Sync Gradle
- [ ] Vérifier qu'il n'y a pas d'erreurs de compilation

### Étape 5: AndroidManifest
- [ ] Copier `MixologyCloud_AndroidManifest.xml` → `app/src/main/AndroidManifest.xml`
- [ ] Vérifier les permissions (INTERNET, ACCESS_NETWORK_STATE, POST_NOTIFICATIONS)
- [ ] Vérifier l'enregistrement du service Firebase

### Étape 6: Fichiers README & Git
- [ ] Renommer `MixologyCloud_README.md` → `README.md`
- [ ] Mettre à jour les noms des membres dans README.md
- [ ] Renommer `MixologyCloud_gitignore.txt` → `.gitignore`
- [ ] Mettre à jour les noms dans `HomeScreen.kt` (ligne des membres)

## 🧪 Tests de Validation

### Test 1: Build & Compilation
- [ ] Clean Project
- [ ] Rebuild Project
- [ ] Aucune erreur de compilation
- [ ] Aucun warning critique

### Test 2: Feature 1 - Homepage
- [ ] L'app se lance sans crash
- [ ] L'écran d'accueil s'affiche correctement
- [ ] Les noms des membres sont visibles
- [ ] Le bouton "Liste de Cocktails" fonctionne
- [ ] Le bouton "Firebase Features" fonctionne

### Test 3: Feature 2 - Cocktails List
- [ ] Navigation vers la liste fonctionne
- [ ] Bouton "Add Random" ajoute un cocktail
- [ ] L'image du cocktail s'affiche (vérifier connexion Internet)
- [ ] Le nom et la catégorie sont visibles
- [ ] Le header de date s'affiche (ex: "11/12/2024")
- [ ] Le footer affiche le count total
- [ ] Cliquer sur un item navigue vers le détail

### Test 4: Feature 2 - Cocktail Detail
- [ ] L'image en grand s'affiche
- [ ] Le nom du cocktail est affiché
- [ ] La catégorie et le type (Alcoholic/Non-Alcoholic) sont visibles
- [ ] La card "Type de verre" affiche le bon verre
- [ ] La card "Instructions" affiche les instructions
- [ ] La date d'ajout est affichée en bas
- [ ] Le bouton retour fonctionne

### Test 5: Feature 2 - Groupement par Jour
- [ ] Ajouter 2-3 cocktails
- [ ] Fermer l'app complètement
- [ ] Rouvrir l'app
- [ ] Les cocktails sont toujours là (Room persistence)
- [ ] Changer la date système (pour tester le groupement)
- [ ] Ajouter un nouveau cocktail
- [ ] Vérifier qu'un nouveau header de date apparaît

### Test 6: Feature 2 - Delete All
- [ ] Cliquer sur "Delete All"
- [ ] La liste devient vide
- [ ] Le message "Aucun cocktail" apparaît
- [ ] Le count devient 0

### Test 7: Feature 3 - Firebase Remote Config
- [ ] Navigation vers l'écran Firebase
- [ ] Le header avec le welcome message s'affiche
- [ ] La couleur du header correspond au Remote Config
- [ ] Les paramètres Remote Config sont affichés
- [ ] Le FCM Token est visible

### Test 8: Feature 3 - Firebase Messaging
- [ ] Cliquer sur "S'abonner aux notifications"
- [ ] Aucun crash
- [ ] (Optionnel) Envoyer une notification test depuis Firebase Console
- [ ] (Optionnel) Vérifier la réception de la notification

### Test 9: Architecture - Room Persistence
- [ ] Ajouter des cocktails
- [ ] Tuer l'app (force stop)
- [ ] Relancer l'app
- [ ] Les cocktails sont toujours présents

### Test 10: Architecture - Mapping
- [ ] Ajouter un cocktail
- [ ] Vérifier dans le logcat que le timestamp est bien créé
- [ ] Vérifier que la date affichée correspond

### Test 11: Erreurs & Edge Cases
- [ ] Désactiver le WiFi/Data
- [ ] Essayer "Add Random"
- [ ] Vérifier qu'un message d'erreur s'affiche (Snackbar)
- [ ] Réactiver le réseau
- [ ] Réessayer → doit fonctionner

## 🔍 Vérifications Code

### Architecture
- [ ] Tous les mappers sont dans `data/mapper/`
- [ ] Aucun appel Firebase direct dans UI ou ViewModel
- [ ] Tous les ViewModels utilisent `@HiltViewModel`
- [ ] Tous les Repositories utilisent `@Inject constructor`
- [ ] Les States sont exposés en `StateFlow`

### Naming Conventions
- [ ] Entity: `*Entity.kt`
- [ ] DTO: `*Dto.kt`
- [ ] Data: `*Data.kt`
- [ ] UI: `*Ui.kt`
- [ ] State: `*UiState`
- [ ] ViewModel: `*ViewModel.kt`
- [ ] Screen: `*Screen.kt`

### Packages Structure
- [ ] `data/local/` contient Room files
- [ ] `data/remote/` contient Retrofit files
- [ ] `data/repository/` contient Repositories
- [ ] `data/model/` contient Data models
- [ ] `data/mapper/` contient Mappers
- [ ] `ui/screens/` contient Composables
- [ ] `ui/viewmodel/` contient ViewModels
- [ ] `ui/model/` contient UI models

## 📝 Préparation Git

### Avant Premier Commit
- [ ] `.gitignore` est configuré
- [ ] `google-services.json` est dans `.gitignore`
- [ ] `local.properties` est dans `.gitignore`
- [ ] Aucun fichier sensible n'est tracké

### Premier Commit
```bash
git init
git add .
git commit -m "Initial commit: MixologyCloud - Clean Architecture setup"
git branch -M main
git remote add origin [URL_REPO]
git push -u origin main
```

### Branches Recommandées
- [ ] `main` - Code stable
- [ ] `develop` - Développement actif
- [ ] `feature/cocktails` - Feature 2
- [ ] `feature/firebase` - Feature 3

## 🎯 Critères de Réussite Final

- [ ] ✅ L'app compile sans erreur
- [ ] ✅ Les 3 features fonctionnent
- [ ] ✅ La persistence Room fonctionne
- [ ] ✅ L'API TheCocktailDB répond
- [ ] ✅ Firebase Remote Config modifie l'UI
- [ ] ✅ L'architecture respecte le diagramme de flux
- [ ] ✅ Les mappers sont tous implémentés
- [ ] ✅ Le groupement par jour fonctionne
- [ ] ✅ Le footer count fonctionne
- [ ] ✅ Le README est complet

## 📚 Documents à Fournir

- [ ] Code source (GitHub)
- [ ] README.md
- [ ] ARCHITECTURE.md
- [ ] Captures d'écran des 3 features
- [ ] (Optionnel) Vidéo démo

## 💡 Conseils Collaboration

1. **Workflow Git**:
   - Toujours pull avant de push
   - Créer une branche par feature
   - Pull request pour merge

2. **Communication**:
   - Documenter les changements d'architecture
   - Prévenir avant modification de fichiers partagés
   - Utiliser les issues GitHub

3. **Code Review**:
   - Vérifier le respect de l'architecture
   - Vérifier les naming conventions
   - Tester localement avant merge

---

**Bonne chance pour votre projet! 🚀**
