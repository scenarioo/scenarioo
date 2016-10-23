# Enable ESLint checks

This makes sure you see the same ESLint warnings already in WebStorm that you would also see when running ESLint in the build.

    File -> Settings

    Languages & Frameworks -> JavaScript -> Code Quality Tools -> ESLint

![Configure WebStorm to use ESLint](https://cloud.githubusercontent.com/assets/3780183/8077529/d74055be-0f56-11e5-8542-30c0a74289cf.jpg)

# Disable EditorConfig

EditorConfig is buggy in WebStorm 9 and causes problems with the code formatter. As we don't use it anyway, just disable it.

    File -> Settings

![Disable EditorConfig](https://cloud.githubusercontent.com/assets/3780183/6417586/99d438e2-beae-11e4-8fd8-1312d17a08ab.PNG)

# Add Command to run unit tests in Webstorm

Run unit tests every time a file changes:
```
File/Settings ... / External Tools / +
Name: gulp test-watch
Program: gulp 
Parameters: test-watch
Working directory: <scenarioo-client>

run unitwatch by: Tools / gulp test-watch
```
