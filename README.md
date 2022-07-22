

# Dev Notary

Dev Notary is a simple application that lets developers create notes, documentations or memos and interact with them. Basic CRUD with addition of sharing the notes with other users. The functional requirements were simple:
- Authentication
- Managing a note: create, read, edit, delete, share
- Restore notes
- List, sort, search

# Application Demo

![Running application](/img/creating-note.gif)
*Creating notes*

![Sharing note](/img/sharing-note.gif)
*Sharing notes*

# Architecture
Since my stronger side is on Android Development, I've decided to develop this project with common Android pattern, MVVM with clean architecture. With a little help of ViewModel wrapper class on iOS side everything turned out great.

![Architecture diagram](/img/architecture.png)

# Libraries

### Shared Module:
- Firebase Kotlin SDK
- SQL Delight
- Kodein DI
- Navigation Component
- Multiplatform UUID
- Kotlin Coroutines
- KotlinX Datetime
- Multiplatform Settings 
- KotlinX Serialization
 
### Android:
- Jetpack Compose
- Accompanist

 ### iOS
- Swift UI
 
## Lines of code in each module:
### Android:
#### 2805
### iOS:
#### 1451
### Shared KMM code:
#### 2853

### 100% of code = 39.5% (Android App code) + 20.4% (iOS App code) + 40.1% (Shared KMM code) 

