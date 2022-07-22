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
f.write(f'''

# Dev Notary


## Kotlin Multiplatform Application

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
