## Project Structure

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

             

