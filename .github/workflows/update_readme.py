import os

def countlines(start, endsWith, lines=0, begin_start=None):
    for thing in os.listdir(start):
        thing = os.path.join(start, thing)
        if os.path.isfile(thing):
            if thing.endswith(endsWith):
                with open(thing, 'r') as f:
                    newlines = f.readlines()
                    newlines = len(newlines)
                    lines += newlines

                    if begin_start is not None:
                        reldir_of_thing = '.' + thing.replace(begin_start, '')
                    else:
                        reldir_of_thing = '.' + thing.replace(start, '')

    for thing in os.listdir(start):
        thing = os.path.join(start, thing)
        if os.path.isdir(thing):
            lines = countlines(thing,endsWith, lines, begin_start=start)

    return lines
def truncate(n, decimals=0):
    multiplier = 10 ** decimals
    return int(n * multiplier) / multiplier


def returnPercentageOfEachModule(android,ios,shared):
    total = android + ios + shared
    androidAsPercentage = (android * 100) / total
    iosAsPercentage = (ios * 100) / total
    sharedAsPercentage = (shared * 100) / total
    result = f'''100% of code = {round(androidAsPercentage,1)}% (Android App code) + {round(iosAsPercentage,1)}% (iOS App code) + {round(sharedAsPercentage,1)}% (Shared KMM code) '''
    print(result)
    return result

androidLines = countlines('androidApp','.kt')
iosLines = countlines('iosApp','.swift')
sharedLines = countlines('shared','.kt')
percentageComposition = returnPercentageOfEachModule(androidLines,iosLines,sharedLines)

open('README.md', 'w').close()
f = open('README.md','w')
## In this section I'll write my README.md
f.write(f'''

# Dev Notary

Dev Notary is a simple application that lets developers create notes, documentations or memos and interact with them. Basic CRUD with addition of sharing the notes with other users. The functional requirements were simple:
- Authentication
- Managing a note: create, read, edit, delete, share
- Restore notes
- List, sort, search

# Application Demo

![Running application](/img/kmm-my-first-take/creating-note.gif)
*Creating notes*

![Sharing note](/img/kmm-my-first-take/sharing-note.gif)
*Sharing notes*

# Architecture
Since my stronger side is on Android Development, I've decided to develop this project with common Android pattern, MVVM with clean architecture. With a little help of ViewModel wrapper class on iOS side everything turned out great.

![Architecture diagram](/img/kmm-my-first-take/architecture.png)

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
#### {androidLines}
### iOS:
#### {iosLines}
### Shared KMM code:
#### {sharedLines}

### {percentageComposition}

''')
f.close()
