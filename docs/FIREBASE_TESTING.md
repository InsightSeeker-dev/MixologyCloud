# 🔥 Guide de Test Firebase

Ce guide explique comment tester les fonctionnalités Firebase de MixologyCloud.

## 📋 Prérequis

### 1. Fichier `google-services.json`
✅ **Déjà configuré** - Le fichier existe dans `app/google-services.json`

### 2. Dépendances Firebase
✅ **Déjà ajoutées** dans `app/build.gradle.kts`:
- `firebase-messaging-ktx` (Notifications)
- `firebase-config-ktx` (Remote Config)
- `firebase-analytics-ktx` (Analytics)

---

## 🧪 Comment Tester Firebase Screen

### État Actuel par Défaut

Au démarrage, Firebase Screen affiche:

#### 📊 Remote Config (Valeurs par défaut)
- **Couleur principale**: `#FF6200EE` (violet par défaut)
- **Message de bienvenue**: `"Bienvenue sur MixologyCloud"`
- **Feature activée**: `true`

Ces valeurs sont **hardcodées par défaut** dans `FirebaseRepository.kt` et s'affichent immédiatement.

#### 📱 Firebase Cloud Messaging
- **FCM Token**: S'affiche après quelques secondes
- **Bouton "S'abonner aux notifications"**: Souscrit au topic `mixology_updates`

---

## 🔧 Configuration de Remote Config (Console Firebase)

Pour tester Remote Config avec des valeurs personnalisées:

### 1. Accéder à la Console Firebase
1. Allez sur [Firebase Console](https://console.firebase.google.com/)
2. Sélectionnez votre projet **MixologyCloud**
3. Menu: **Remote Config** (dans la section "Engage")

### 2. Créer les Paramètres

Ajoutez ces **3 paramètres** dans Remote Config:

| Clé | Type | Valeur Exemple | Description |
|-----|------|----------------|-------------|
| `primary_color` | String | `#FF00FF00` | Couleur verte (format hex Android) |
| `welcome_message` | String | `Bienvenue Mixologue!` | Message personnalisé |
| `feature_enabled` | Boolean | `false` | Active/désactive une feature |

### 3. Publier les Changements
- Cliquez sur **"Publier les modifications"**
- Les nouvelles valeurs seront disponibles après le fetch

---

## 🎯 Test dans l'Application

### A. Lancement Initial

1. **Lancez l'app** et naviguez vers **"Firebase Features"**
2. **Vous verrez**:
   - Valeurs **par défaut** (couleur violette)
   - FCM Token qui se charge
   - Interface fonctionnelle

### B. Actualiser Remote Config

Pour récupérer les valeurs de la console Firebase:

1. **Cliquez sur le bouton "Actualiser Remote Config"**
   - OU: Cliquez sur l'icône 🔄 en haut à droite

2. **Attendez quelques secondes**

3. **Observez les changements**:
   - ✅ La **bannière de bienvenue** change de couleur
   - ✅ Le **texte** du message de bienvenue change
   - ✅ La **valeur** de "Feature activée" change

### C. Test des Notifications

1. **Cliquez sur "S'abonner aux notifications"**
2. **Le bouton devient**:  `✓ Abonné aux notifications` (désactivé)
3. **Vous recevez** un message de confirmation via Snackbar

#### Envoyer une Notification Test

Dans la Console Firebase:
1. **Cloud Messaging** → **"Créer une campagne"**
2. Titre: `Test MixologyCloud`
3. Message: `Nouveau cocktail disponible!`
4. **Ciblage**: Topic `mixology_updates`
5. **Envoyer**

---

## 🔍 Vérification Visuelle

### Comportement Attendu

#### ✅ Remote Config Fonctionne
- La couleur de la bannière **change** après actualisation
- Le message **change** selon la console
- Les valeurs **s'affichent** correctement dans la carte "Remote Config"

#### ✅ FCM Fonctionne
- Le **token FCM** s'affiche (long string de caractères)
- Le bouton **passe à "Abonné"** après clic
- Les **notifications** arrivent sur l'appareil

#### ❌ Remote Config par Défaut Seulement
- La couleur reste **violette** (`#FF6200EE`)
- Le message reste **"Bienvenue sur MixologyCloud"**
- Aucun changement après actualisation

**→ Causes possibles**:
- Remote Config non configuré dans la console
- Pas de connexion Internet
- `google-services.json` invalide

---

## 🐛 Debugging

### Logs à Vérifier (Logcat)

```bash
# Filtrer les logs Firebase
adb logcat | grep -i firebase

# Rechercher les erreurs Remote Config
adb logcat | grep -i "remote config"

# Rechercher les tokens FCM
adb logcat | grep -i "FCM token"
```

### Points de Contrôle

| Élément | Vérification |
|---------|--------------|
| **Internet** | L'appareil/émulateur a-t-il Internet? |
| **google-services.json** | Correspond-il au projet Firebase? |
| **Package name** | Est-ce bien `com.example.mixologycloud`? |
| **Console Firebase** | Les paramètres Remote Config sont-ils publiés? |

---

## 📝 Architecture

### Flux de Données

```
FirebaseScreen (UI)
       ↓
FirebaseViewModel
       ↓
FirebaseRepository (Data Layer)
       ↓
Firebase SDK (Remote Config + Messaging)
```

**Principe respecté**: Aucun appel Firebase direct dans l'UI, tout passe par le Repository.

---

## 🎨 Changements Visuels Observables

### Exemple avec Remote Config Actif

**Avant** (par défaut):
- 🟣 Bannière violette
- "Bienvenue sur MixologyCloud"

**Après** (avec config `#FF00FF00` + "Bienvenue Mixologue!"):
- 🟢 Bannière verte
- "Bienvenue Mixologue!"

---

## ✅ Checklist de Test

- [ ] L'écran Firebase s'affiche sans crash
- [ ] Les valeurs par défaut apparaissent
- [ ] Le FCM Token se charge
- [ ] Le bouton "Actualiser" fonctionne
- [ ] Le bouton "S'abonner" change d'état après clic
- [ ] La couleur de la bannière est dynamique
- [ ] Le message de bienvenue est dynamique
- [ ] Les messages Snackbar s'affichent (succès/erreur)
- [ ] Le bouton retour fonctionne

---

## 🚀 Configuration Actuelle

### Interval de Fetch

✅ **Configuré à: 60 secondes (1 minute)** - Optimisé pour les tests

Cela signifie que Remote Config peut être actualisé toutes les minutes, ce qui facilite les tests.

⚠️ **Note pour Production**: En production, il est recommandé d'augmenter cette valeur à 3600s (1h) pour éviter le throttling Firebase et économiser le quota.

**`FirebaseRepository.kt`** ligne 25:
```kotlin
.setMinimumFetchIntervalInSeconds(60) // Configuration actuelle
```

---

## 💡 Résumé

- **Remote Config** fonctionne **même sans console Firebase** (valeurs par défaut)
- Les changements sont **visibles immédiatement** dans l'UI
- L'architecture **respecte Clean Architecture** (pas de Firebase dans l'UI)
- Le bouton **Actualiser** permet de forcer le fetch des nouvelles valeurs
