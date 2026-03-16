1-# Project Structure

app/
├── manifests/
│   └── AndroidManifest.xml
├── kotlin+java/
│   └── com.khadija.bankapp/
│       ├── MainActivity.kt
│       ├── data/
│       │   ├── account/
│       │   │   ├── AccountRepository
│       │   │   └── BankAccount
│       │   ├── auth/
│       │   │   └── AuthRepository
│       │   ├── transaction/
│       │   │   ├── Transaction
│       │   │   └── TransactionRepository
│       │   └── user/
│       │       ├── UserProfile
│       │       └── UserRepository
│       └── ui/
│           ├── account/
│           │   ├── AccountScreen.kt
│           │   └── AccountViewModel
│           ├── auth/
│           │   ├── AuthViewModel
│           │   ├── LoginScreen.kt
│           │   └── RegisterScreen.kt
│           ├──home/
│           │   └── HomeScreen.kt
│           ├── navigation/
│           │   └── AppNavGraph.kt
│           ├──profile/            
│           │   ├── ProfileScreen.kt
│           │   └── ProfileViewModel
│           ├──transaction/ 
│           │   ├── TransactionHistoryScreen.kt
│           │   ├── TransactionHistoryScreenViewModel.kt
│           │   ├── TransactionScreen.kt
│           │   └──  TransactionViewModel.kt
│           ├──transfer/ 
│           │   ├── TransferScreen.kt
│           │   └── TransferViewModel.kt

2-# Technologies Used

- Kotlin
- Android Studio
- MVVM Architecture
- Retrofit (REST API)
- Room Database
- SharedPreferences
- Firebase Authentication
- Firebase Cloud Messaging (FCM)
- LiveData / Flow
- ProGuard / R8
- MockAPI (Test REST API)


3-# Features

- User Authentication (Firebase Authentication)
- View bank accounts
- View account balance
- Transaction history
- Push notifications for new transactions
- Offline access using Room database
- Secure session management
