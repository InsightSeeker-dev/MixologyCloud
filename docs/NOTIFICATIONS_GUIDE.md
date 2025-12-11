# 🔔 Guide Complet des Notifications Firebase

## 📖 À Quoi Sert le FCM Token?

### Définition

Le **FCM Token** (Firebase Cloud Messaging Token) est un **identifiant unique** généré pour chaque installation de votre app sur un appareil.

### Rôle du Token

```
Firebase Console → [FCM Token] → Appareil Spécifique
```

- **Identification**: Firebase utilise ce token pour savoir **sur quel appareil** envoyer la notification
- **Unique par installation**: Chaque app installée a son propre token
- **Renouvellement**: Le token peut changer (réinstallation, mise à jour, etc.)

### Analogie Simple

Le FCM Token, c'est comme une **adresse postale pour votre téléphone**:
- Sans adresse → Le facteur ne sait pas où livrer
- Sans token → Firebase ne sait pas où envoyer la notification

---

## 🎯 Comment Recevoir des Notifications

### Étape 1: S'Abonner au Topic

Dans l'app:
1. **Allez sur l'écran Firebase Features**
2. **Cliquez sur "S'abonner aux notifications"**
3. **Vérifiez** que le bouton devient "✓ Abonné aux notifications"

**Ce que ça fait**:
```kotlin
firebaseRepository.subscribeToTopic("mixology_updates")
```
→ Votre appareil rejoint le groupe `mixology_updates`

### Étape 2: Envoyer une Notification Test

#### Via la Console Firebase

1. **Ouvrez** [Firebase Console](https://console.firebase.google.com/)
2. **Sélectionnez** votre projet "MixologyCloud"
3. **Menu** → **Cloud Messaging** (dans "Engage")
4. **Cliquez** "Créer une campagne" → "Messages Firebase"

#### Remplissez le Formulaire

**Onglet 1: Notification**
- **Titre**: `Test MixologyCloud`
- **Texte**: `Nouveau cocktail disponible!`
- (Optionnel) Image, icône, etc.

**Onglet 2: Ciblage**
- **Sélectionnez**: "Topic"
- **Nom du topic**: `mixology_updates`
- **Vérifiez**: "1 appareil abonné" (ou plus)

**Onglet 3: Options**
- Laissez par défaut ou ajustez selon besoin

**Cliquez**: **"Publier"**

---

## 🔍 Pourquoi Vous Ne Recevez Pas de Notifications?

### Checklist de Vérification

#### ✅ 1. Permissions Android

**Android 13+ (API 33+)**
```xml
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
```

**Action requise**:
- Au premier lancement, l'app **doit demander** la permission
- L'utilisateur doit **accepter** les notifications

**Vérification**:
```
Paramètres → Apps → MixologyCloud → Notifications → Activées
```

#### ✅ 2. Token FCM Généré

**Dans l'app**:
- Le token doit s'afficher dans la carte "Firebase Cloud Messaging"
- Format: `fA8xK...` (long string)

**Si "Chargement..."**:
- Problème de connexion Firebase
- Vérifiez `google-services.json`

#### ✅ 3. Abonnement au Topic

**Dans l'app**:
- Le bouton doit être "✓ Abonné aux notifications"
- Un message de succès doit apparaître

**Si non abonné**:
- Cliquez sur "S'abonner aux notifications"
- Attendez le message de confirmation

#### ✅ 4. App en Arrière-Plan

**Important**: Firebase affiche automatiquement les notifications **seulement si l'app est en arrière-plan** ou fermée.

**Si l'app est ouverte** (foreground):
- La méthode `onMessageReceived()` est appelée
- Le service affiche quand même la notification

**Test**:
- Envoyez une notification
- **Appuyez sur Home** (mettre l'app en arrière-plan)
- La notification devrait apparaître dans le tiroir de notifications

#### ✅ 5. Internet Actif

- L'appareil/émulateur doit avoir une connexion Internet active
- Firebase nécessite une connexion pour recevoir les messages

#### ✅ 6. Service Déclaré dans le Manifest

**Vérifiez `AndroidManifest.xml`**:
```xml
<service
    android:name=".service.MixologyMessagingService"
    android:exported="false">
    <intent-filter>
        <action android:name="com.google.firebase.MESSAGING_EVENT" />
    </intent-filter>
</service>
```
✅ **Déjà configuré dans votre projet**

---

## 🧪 Test Complet Pas à Pas

### Scénario 1: Notification à un Appareil Spécifique (via Token)

**Étape 1**: Récupérer le Token
- Ouvrez l'app → Firebase Features
- **Copiez** le FCM Token affiché

**Étape 2**: Console Firebase
- Cloud Messaging → "Créer une campagne"
- **Titre**: `Test Direct`
- **Texte**: `Notification directe!`

**Étape 3**: Ciblage
- **Sélectionnez**: "Tester sur votre appareil"
- **Collez** le FCM Token copié
- **Cliquez**: "Tester"

**Résultat**:
- Notification devrait apparaître **immédiatement**
- Si elle n'apparaît pas → Vérifier les permissions

### Scénario 2: Notification à un Topic (Groupe)

**Étape 1**: S'abonner
- Ouvrez l'app → Firebase Features
- Cliquez "S'abonner aux notifications"
- Attendez "✓ Abonné"

**Étape 2**: Console Firebase
- Cloud Messaging → "Créer une campagne"
- **Titre**: `Test Topic`
- **Texte**: `Message pour tous!`

**Étape 3**: Ciblage
- **Sélectionnez**: "Topic"
- **Nom**: `mixology_updates`
- **Publier**

**Résultat**:
- Tous les appareils abonnés reçoivent la notification
- Délai possible: quelques secondes

---

## 🐛 Debugging - Logcat

### Commandes Utiles

```bash
# Voir tous les logs Firebase
adb logcat | grep -i firebase

# Voir les messages reçus
adb logcat | grep -i "onMessageReceived"

# Voir les tokens
adb logcat | grep -i "FCM token"

# Voir les erreurs de notification
adb logcat | grep -i "notification"
```

### Messages Importants

**Token récupéré**:
```
D/FirebaseMessaging: Token: fA8xK...
```

**Message reçu**:
```
D/MixologyMessagingService: onMessageReceived
```

**Notification affichée**:
```
I/NotificationManager: enqueueNotificationInternal
```

---

## 🎨 Architecture des Notifications

### Flux de Données

```
Console Firebase
      ↓
Firebase Cloud Messaging
      ↓
[FCM Token] → Appareil spécifique
      ↓
MixologyMessagingService.onMessageReceived()
      ↓
NotificationManager.notify()
      ↓
📱 Notification affichée
```

### Types de Messages Firebase

#### 1. **Notification Message** (automatique)
```json
{
  "notification": {
    "title": "Titre",
    "body": "Message"
  }
}
```
✅ **Affichage automatique** si l'app est en arrière-plan
✅ **Géré par Firebase** automatiquement

#### 2. **Data Message** (manuel)
```json
{
  "data": {
    "key": "value"
  }
}
```
⚠️ **Nécessite** `onMessageReceived()` pour traiter
✅ **Fonctionne toujours**, même en foreground

### Votre Configuration Actuelle

Vous utilisez **Notification Message** → Affichage automatique ✅

---

## 💡 Cas d'Usage du Token vs Topic

### Token FCM (Appareil Individuel)

**Quand l'utiliser**:
- Notification **personnalisée** pour un utilisateur
- Test sur **votre appareil** uniquement
- Message **urgent** à un utilisateur spécifique

**Exemple**:
```
"Votre commande de Mojito est prête!"
```

### Topic (Groupe)

**Quand l'utiliser**:
- Message pour **tous les utilisateurs**
- Campagnes **marketing**
- **Annonces** générales

**Exemple**:
```
"Nouveau cocktail de la semaine: Margarita!"
```

---

## ⚡ Quick Test (2 minutes)

### Test Express

1. **Lancez l'app**
2. **Allez sur Firebase Features**
3. **Vérifiez**: Le FCM Token s'affiche (pas "Chargement...")
4. **Cliquez**: "S'abonner aux notifications"
5. **Attendez**: Message "Abonné aux notifications avec succès!"
6. **Mettez l'app en arrière-plan** (bouton Home)
7. **Ouvrez**: [Firebase Console](https://console.firebase.google.com/)
8. **Cloud Messaging** → "Créer une campagne"
9. **Remplissez**:
   - Titre: `Test`
   - Message: `Hello!`
10. **Ciblage**: Topic `mixology_updates`
11. **Publiez**

**Résultat attendu**: Notification dans les **5 secondes** 🎉

---

## ❓ FAQ

### Q: Pourquoi je ne vois rien quand l'app est ouverte?
**R**: Firebase affiche automatiquement **seulement en arrière-plan**. Mettez l'app en background pour voir la notification.

### Q: Le token change-t-il?
**R**: Oui, lors de:
- Réinstallation de l'app
- Mise à jour de Firebase
- Changement d'appareil

### Q: Combien de temps pour recevoir?
**R**: **Quasi-instantané** (< 5 secondes si Internet fonctionne)

### Q: Puis-je envoyer des notifications depuis mon code?
**R**: Non, Firebase Cloud Messaging nécessite:
- **Console Firebase** (manuel)
- **Firebase Admin SDK** (serveur backend)
- **API REST Firebase** (avec clé serveur)

### Q: Les notifications fonctionnent hors ligne?
**R**: Non, Internet est **obligatoire** pour recevoir les notifications Firebase.

---

## ✅ Checklist Finale

Avant d'envoyer une notification, vérifiez:

- [ ] L'app est installée et lancée
- [ ] Le FCM Token s'affiche (pas "Chargement...")
- [ ] Abonnement au topic effectué ("✓ Abonné")
- [ ] Connexion Internet active
- [ ] Permissions notifications accordées (Android 13+)
- [ ] App mise en **arrière-plan** (pas fermée, juste Home)
- [ ] Notification envoyée depuis Console Firebase
- [ ] Ciblage correct (token ou topic `mixology_updates`)

**Si toutes les cases sont cochées et ça ne marche toujours pas**:
→ Vérifiez les logs Logcat (voir section Debugging)

---

## 🚀 Résumé

| Élément | Rôle |
|---------|------|
| **FCM Token** | Adresse de votre appareil |
| **Topic** | Groupe d'appareils |
| **S'abonner** | Rejoindre le groupe |
| **Console Firebase** | Envoyer les notifications |
| **MixologyMessagingService** | Recevoir et afficher |

**Les notifications Firebase fonctionnent!** Il faut juste suivre les bonnes étapes. 🎉
