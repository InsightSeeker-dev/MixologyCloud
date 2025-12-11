# Architecture MixologyCloud - Documentation Technique

## 📐 Vue d'Ensemble de l'Architecture

MixologyCloud suit une **Clean Architecture adaptée** avec séparation stricte en couches DATA et UI.

## 🗂️ Structure des Packages

```
com.example.mixologycloud/
│
├── data/                              # COUCHE DATA
│   ├── local/                         # Persistence locale
│   │   ├── CocktailDao.kt            # Interface Room DAO
│   │   ├── CocktailEntity.kt         # Entity Room avec timestamp
│   │   └── MixologyDatabase.kt       # Database Room
│   │
│   ├── remote/                        # API externe
│   │   ├── CocktailApiService.kt     # Interface Retrofit
│   │   └── CocktailDto.kt            # DTO de l'API
│   │
│   ├── repository/                    # Couche Repository
│   │   ├── CocktailRepository.kt     # Logique métier Cocktails
│   │   └── FirebaseRepository.kt     # Logique Firebase
│   │
│   ├── model/                         # Modèles métier
│   │   └── CocktailData.kt           # Objet agnostique
│   │
│   └── mapper/                        # Mappers de transformation
│       └── CocktailMapper.kt         # DTO→Data, Entity→Data
│
├── ui/                                # COUCHE UI
│   ├── screens/                       # Composables
│   │   ├── HomeScreen.kt             # Feature 1: Homepage
│   │   ├── CocktailListScreen.kt     # Feature 2: Liste
│   │   ├── CocktailDetailScreen.kt   # Feature 2: Détail
│   │   └── FirebaseScreen.kt         # Feature 3: Firebase
│   │
│   ├── viewmodel/                     # ViewModels
│   │   ├── CocktailListViewModel.kt  # VM Liste + État
│   │   ├── CocktailDetailViewModel.kt# VM Détail + État
│   │   └── FirebaseViewModel.kt      # VM Firebase + État
│   │
│   ├── model/                         # Modèles UI
│   │   └── CocktailUi.kt             # Objet formaté pour UI
│   │
│   ├── navigation/                    # Navigation
│   │   └── NavGraph.kt               # Routes et navigation
│   │
│   └── theme/                         # Thème Material3
│       ├── Theme.kt
│       └── Type.kt
│
├── di/                                # Injection de dépendances
│   └── AppModule.kt                  # Module Hilt
│
├── service/                           # Services Android
│   └── MixologyMessagingService.kt   # FCM Service
│
├── MixologyCloudApplication.kt       # Application Hilt
└── MixologyCloudActivity.kt          # Single Activity
```

## 🔄 Flux de Données (Critical!)

### Ajout d'un Cocktail
```
1. USER clique "Add Random"
2. ViewModel.addRandomCocktail()
3. Repository.addRandomCocktail()
   ├─ API call → CocktailDto
   ├─ Mapper: Dto → CocktailData (+ timestamp)
   ├─ Mapper: Data → CocktailEntity
   └─ DAO.insert(entity)
4. Room déclenche Flow
5. Repository observe → map Entity → Data
6. ViewModel observe → map Data → UI
7. Compose recompose avec nouveaux cocktails
```

### Groupement par Jour
```
1. CocktailEntity stocke timestamp (Long)
2. Mapper convertit timestamp → "dd/MM/yyyy"
3. ViewModel groupe par insertionDay
4. UI affiche headers par jour
```

## 🎯 Mappers - Règles Strictes

### CocktailDto → CocktailData
```kotlin
fun CocktailDto.toData(timestamp: Long = System.currentTimeMillis()): CocktailData
```
- Ajoute le timestamp lors de la conversion
- Gère les nullable de l'API (.orEmpty())

### CocktailEntity → CocktailData
```kotlin
fun CocktailEntity.toData(): CocktailData
```
- Conversion directe 1:1

### CocktailData → CocktailEntity
```kotlin
fun CocktailData.toEntity(): CocktailEntity
```
- Préparation pour Room

### CocktailData → CocktailUi
```kotlin
fun CocktailData.toUi(): CocktailUi
```
- Formate timestamp en "dd/MM/yyyy"
- Prépare pour affichage

## 🔥 Firebase - Architecture Propre

### Principe
**JAMAIS d'appel Firebase direct dans l'UI ou ViewModel**

### Implémentation
```
FirebaseMessaging/RemoteConfig 
    ↓
FirebaseRepository (data layer)
    ↓ expose RemoteConfigData
FirebaseViewModel
    ↓ expose FirebaseUiState
FirebaseScreen (observe state)
```

### Remote Config Impact
- `primary_color` → Change la couleur du header
- `welcome_message` → Change le texte d'accueil
- `feature_enabled` → Active/désactive des features

## 🏛️ États UI (UiState Pattern)

### CocktailListUiState
```kotlin
data class CocktailListUiState(
    val cocktails: List<CocktailUi>,
    val groupedCocktails: Map<String, List<CocktailUi>>,
    val totalCount: Int,
    val isLoading: Boolean,
    val error: String?
)
```

### CocktailDetailUiState
```kotlin
data class CocktailDetailUiState(
    val cocktail: CocktailUi?,
    val isLoading: Boolean,
    val error: String?
)
```

### FirebaseUiState
```kotlin
data class FirebaseUiState(
    val remoteConfig: RemoteConfigData?,
    val fcmToken: String?,
    val isLoading: Boolean,
    val error: String?
)
```

## 🔌 Injection Hilt

### Provided
- `MixologyDatabase` (Singleton)
- `CocktailDao` (Singleton)
- `CocktailApiService` (Retrofit)
- `FirebaseRemoteConfig` (Singleton)
- `FirebaseMessaging` (Singleton)

### Injected
- `CocktailRepository` (dans ViewModels)
- `FirebaseRepository` (dans FirebaseViewModel)

## 📱 Features Détaillées

### Feature 1: Homepage
- **Fichier**: `HomeScreen.kt`
- **Contenu**: Noms des membres + 2 boutons navigation
- **Navigation**: Vers CocktailList et Firebase

### Feature 2: Cocktails
#### Liste (`CocktailListScreen.kt`)
- Affichage groupé par jour d'insertion
- Footer avec count total
- Actions: Add Random, Delete All
- Navigation vers détail au clic

#### Détail (`CocktailDetailScreen.kt`)
- Image en grand format
- Type de verre (Card secondaire)
- Instructions (Card tertiaire)
- Date d'ajout

### Feature 3: Firebase
- **Fichier**: `FirebaseScreen.kt`
- Affichage du Remote Config
- FCM Token
- Header avec couleur dynamique
- Bouton subscription notifications

## 🧪 Points de Test Critiques

1. **Mapping**: Vérifier que timestamp est bien ajouté
2. **Groupement**: Items bien groupés par jour
3. **Firebase**: Remote Config change bien l'UI
4. **Room**: Persistence correcte après app kill
5. **API**: Gestion erreurs réseau

## 📚 Conventions de Nommage

- **Entity**: `CocktailEntity`
- **DTO**: `CocktailDto`
- **Data**: `CocktailData`
- **UI**: `CocktailUi`
- **State**: `CocktailListUiState`
- **Repository**: `CocktailRepository`
- **ViewModel**: `CocktailListViewModel`
- **Screen**: `CocktailListScreen`

## 🚨 Points d'Attention

1. **N'oubliez pas**: `google-services.json` dans `.gitignore`
2. **Timestamp**: Toujours ajouté lors de l'insertion
3. **Firebase**: Toujours via Repository
4. **Navigation**: Utiliser Screen.createRoute()
5. **States**: Toujours exposés en StateFlow
6. **Compose**: Collecter states avec .collectAsState()

## 🎓 Pour Collaborer

1. Respecter la structure des packages
2. Toujours mapper entre couches
3. Jamais de logique métier dans l'UI
4. Utiliser Hilt pour injection
5. États immutables (data class + copy)
6. Flow pour les données réactives
