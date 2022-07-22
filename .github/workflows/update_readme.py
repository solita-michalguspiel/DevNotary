import sys

android = sys.argv[1]
ios = sys.argv[2]
shared = sys.argv[3]

print("hello world!")

open('readme.md', 'w').close()
f = open('readme.md','w')
f.write(f'''

# Dev Notary

## Kotlin Multiplatform Application


## Lines of code in each module:


### Android:
{android}

### iOS:
{ios}

### shared:
{shared}

''')
f.close()
