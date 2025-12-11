# 🎨 Design Patterns & Best Practices - MixologyCloud

## 📐 Architectural Patterns

### 1. Clean Architecture (Adaptée)

**Principe**: Séparation des responsabilités en couches indépendantes.

```
┌─────────────────────────────────────────────┐
│              UI LAYER                       │
│  (Screens, ViewModels, UI Models)          │
└──────────────┬──────────────────────────────┘
               │ StateFlow<UiState>
               ↓
┌─────────────────────────────────────────────┐
│           DATA LAYER                        │
│  (Repositories, Data Models, Mappers)       │
└──────┬─────────────────┬────────────────────┘
       │                 │
       ↓                 ↓
┌─────────────┐   ┌─────────────┐
│   LOCAL     │   │   REMOTE    │
│   (Room)    │   │   (API)     │
└─────────────┘   └─────────────┘
```

**Bénéfices**:
- Testabilité accrue
- Séparation des concerns
- Facilite le travail en équipe
- Maintenance simplifiée

### 2. Repository Pattern

**Implémentation**: `CocktailRepository`, `FirebaseRepository`

**Responsabilités**:
- Abstraction de la source de données
- Logique de mise en cache
- Mapping entre couches
- Gestion des erreurs

**Exemple**:
```kotlin
class CocktailRepository @Inject constructor(
    private val apiService: CocktailApiService,
    private val cocktailDao: CocktailDao
) {
    // Single source of truth: Room Database
    fun getAllCocktails(): Flow<List<CocktailData>> {
        return cocktailDao.getAllCocktails()
            .map { entities -> entities.map { it.toData() } }
    }
    
    // Coordonne API → Mapping → Cache
    suspend fun addRandomCocktail(): Result<CocktailData> {
        return try {
            val dto = apiService.getRandomCocktail().drinks?.first()
            val data = dto.toData(System.currentTimeMillis())
            cocktailDao.insertCocktail(data.toEntity())
            Result.success(data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

### 3. MVVM (Model-View-ViewModel)

**Components**:
- **View**: Composables (`*Screen.kt`)
- **ViewModel**: Logique UI + État (`*ViewModel.kt`)
- **Model**: Données UI (`*Ui.kt`, `*UiState`)

**Flow**:
```
User Action (View)
    ↓
ViewModel.method()
    ↓
Repository.operation()
    ↓
StateFlow emission
    ↓
View recomposition
```

### 4. Single Source of Truth

**Principe**: Room Database = Source unique de vérité

```kotlin
// ❌ INCORRECT: Stocker en mémoire
var cocktailsList = mutableListOf<CocktailData>()

// ✅ CORRECT: Observer Room
fun getAllCocktails(): Flow<List<CocktailData>> {
    return cocktailDao.getAllCocktails()
        .map { entities -> entities.map { it.toData() } }
}
```

**Bénéfices**:
- Pas de désynchronisation
- Persistence automatique
- Réactivité native

### 5. Unidirectional Data Flow

**Flow des données**:
```
UI → ViewModel → Repository → Data Source
        ↑                          ↓
        ←────── StateFlow ←────────┘
```

**Exemple**:
```kotlin
// UI envoie une action
Button(onClick = { viewModel.addRandomCocktail() })

// ViewModel traite
fun addRandomCocktail() {
    viewModelScope.launch {
        repository.addRandomCocktail()
        // Pas besoin de mettre à jour manuellement le state
        // Le Flow Room notifie automatiquement
    }
}

// UI observe le state
val uiState by viewModel.uiState.collectAsState()
```

## 🔧 Design Patterns Spécifiques

### 1. Mapper Pattern

**But**: Transformer objets entre couches sans dépendances croisées.

**Implémentation**: Extensions Kotlin
```kotlin
// DTO → Data
fun CocktailDto.toData(timestamp: Long): CocktailData

// Entity → Data
fun CocktailEntity.toData(): CocktailData

// Data → Entity
fun CocktailData.toEntity(): CocktailEntity

// Data → UI
fun CocktailData.toUi(): CocktailUi
```

**Avantages**:
- Découplage des couches
- Facilite les changements d'API
- Transformations centralisées

### 2. State Pattern (UiState)

**But**: Encapsuler tous les états possibles de l'UI.

```kotlin
data class CocktailListUiState(
    val cocktails: List<CocktailUi> = emptyList(),
    val groupedCocktails: Map<String, List<CocktailUi>> = emptyMap(),
    val totalCount: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null
)
```

**Usage dans Compose**:
```kotlin
when {
    uiState.isLoading -> LoadingIndicator()
    uiState.error != null -> ErrorMessage(uiState.error)
    uiState.cocktails.isEmpty() -> EmptyState()
    else -> CocktailList(uiState.groupedCocktails)
}
```

### 3. Dependency Injection (Hilt)

**But**: Inversion de contrôle, facilite les tests.

**Configuration**:
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MixologyDatabase {
        return Room.databaseBuilder(
            context,
            MixologyDatabase::class.java,
            "mixology_database"
        ).build()
    }
}
```

**Usage**:
```kotlin
@HiltViewModel
class CocktailListViewModel @Inject constructor(
    private val repository: CocktailRepository  // Automatiquement injecté
) : ViewModel()
```

### 4. Observer Pattern (Flow)

**But**: Réactivité aux changements de données.

**Room + Flow**:
```kotlin
@Query("SELECT * FROM cocktails")
fun getAllCocktails(): Flow<List<CocktailEntity>>
```

**ViewModel**:
```kotlin
init {
    cocktailRepository.getAllCocktails()
        .map { data -> data.map { it.toUi() } }
        .collect { uiModels ->
            _uiState.value = _uiState.value.copy(cocktails = uiModels)
        }
}
```

### 5. Factory Pattern (Retrofit Builder)

**Implémentation**:
```kotlin
@Provides
@Singleton
fun provideCocktailApiService(okHttpClient: OkHttpClient): CocktailApiService {
    return Retrofit.Builder()
        .baseUrl(CocktailApiService.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CocktailApiService::class.java)
}
```

## 🎯 Best Practices Implémentées

### 1. Sealed Classes pour la Navigation

```kotlin
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object CocktailList : Screen("cocktail_list")
    object CocktailDetail : Screen("cocktail_detail/{cocktailId}") {
        fun createRoute(cocktailId: String) = "cocktail_detail/$cocktailId"
    }
}
```

### 2. Coroutines & Flow

**Opérations asynchrones**:
```kotlin
suspend fun addRandomCocktail(): Result<CocktailData>
```

**Données réactives**:
```kotlin
fun getAllCocktails(): Flow<List<CocktailData>>
```

**Scope ViewModel**:
```kotlin
viewModelScope.launch {
    // Annulé automatiquement quand le ViewModel est cleared
}
```

### 3. Result Type pour les Erreurs

```kotlin
suspend fun addRandomCocktail(): Result<CocktailData> {
    return try {
        val data = fetchFromApi()
        Result.success(data)
    } catch (e: Exception) {
        Result.failure(e)
    }
}

// Usage
repository.addRandomCocktail()
    .onSuccess { data -> /* handle success */ }
    .onFailure { error -> /* handle error */ }
```

### 4. Immutabilité (Data Classes)

```kotlin
// ❌ INCORRECT: Mutable
var cocktails = mutableListOf<CocktailUi>()

// ✅ CORRECT: Immutable + copy
data class CocktailListUiState(
    val cocktails: List<CocktailUi> = emptyList()
)

_uiState.value = _uiState.value.copy(
    cocktails = newList
)
```

### 5. Nullable Gestion (API)

```kotlin
data class CocktailDto(
    @SerializedName("strDrink")
    val name: String,                    // Required
    
    @SerializedName("strCategory")
    val category: String?,               // Optional
)

// Dans le mapper
fun CocktailDto.toData(): CocktailData {
    return CocktailData(
        name = name,                     // Non-null garanti
        category = category.orEmpty()    // Gestion du null
    )
}
```

### 6. Extension Functions

```kotlin
// Lisibilité et réutilisabilité
fun Long.toFormattedDate(): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(this))
}

// Usage
val displayDate = cocktail.timestamp.toFormattedDate()
```

### 7. Composition over Inheritance

**Compose encourage la composition**:
```kotlin
@Composable
fun CocktailListScreen() {
    Scaffold(
        topBar = { TopAppBar() },
        floatingActionButton = { AddButton() }
    ) {
        CocktailList()  // Composition
    }
}
```

## 🚫 Anti-Patterns Évités

### ❌ 1. God Class
```kotlin
// ❌ INCORRECT: Tout dans une seule classe
class CocktailManager {
    fun fetchFromApi() { }
    fun saveToDb() { }
    fun mapData() { }
    fun updateUi() { }
}

// ✅ CORRECT: Responsabilités séparées
class CocktailApiService { }
class CocktailDao { }
class CocktailMapper { }
class CocktailViewModel { }
```

### ❌ 2. Hard-Coded Values
```kotlin
// ❌ INCORRECT
val url = "https://www.thecocktaildb.com/"

// ✅ CORRECT
object CocktailApiService {
    const val BASE_URL = "https://www.thecocktaildb.com/"
}
```

### ❌ 3. UI Logic dans Repository
```kotlin
// ❌ INCORRECT
class CocktailRepository {
    fun formatCocktailForDisplay(cocktail: CocktailData): String {
        return "${cocktail.name} - ${cocktail.category}"
    }
}

// ✅ CORRECT: Dans ViewModel ou Mapper
fun CocktailData.toUi(): CocktailUi {
    return CocktailUi(/* format for UI */)
}
```

### ❌ 4. Context dans ViewModel
```kotlin
// ❌ INCORRECT
class CocktailListViewModel(
    private val context: Context  // Memory leak!
) : ViewModel()

// ✅ CORRECT: Injection avec ApplicationContext si nécessaire
class CocktailListViewModel @Inject constructor(
    private val repository: CocktailRepository
) : ViewModel()
```

## 📚 Principes SOLID Appliqués

### S - Single Responsibility
- Chaque classe a une seule raison de changer
- `CocktailRepository`: Gestion des données
- `CocktailViewModel`: Logique UI
- `CocktailMapper`: Transformations

### O - Open/Closed
- Les classes sont ouvertes à l'extension, fermées à la modification
- Interface `CocktailApiService` peut être mockée pour tests

### L - Liskov Substitution
- Les repositories peuvent être remplacés par des mocks

### I - Interface Segregation
- Interfaces spécialisées (`CocktailDao`, `CocktailApiService`)

### D - Dependency Inversion
- Dépendance sur abstractions (interfaces) via Hilt
- ViewModels dépendent de Repositories, pas de Room/Retrofit directement

## 🎓 Conclusion

L'architecture MixologyCloud démontre:
- ✅ Clean Architecture adaptée au contexte Android
- ✅ Patterns modernes (MVVM, Repository, Mapper)
- ✅ Best practices Kotlin et Jetpack Compose
- ✅ Code maintenable et testable
- ✅ Scalabilité pour ajout de features

Cette structure est production-ready et suit les recommandations officielles Android.
