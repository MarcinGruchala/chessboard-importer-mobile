# chessboard-importer-mobile

Android application for Chessboard Importer

## Build APK from command line

Go to root project directory and run the following command:

### Linux

```shell
foo@bar:~$ ./gradlew assembleDebug
```

### Windows

```shell
foo@bar:~$ gradlew assembleDebug
```

This creates APK file `app-debug.apk` in directory `./app/build/outputs/apk/debug`

## Build APK from command line and install it on a running emulator or device

Go to root project directory and run the following command:

### Linux

```shell
foo@bar:~$ ./gradlew installDebug
```

### Windows

```shell
foo@bar:~$ gradlew installDebug
```

## Build APK from Android Studio

1. In main menu head to `Build > Build Bundle(s) / APK(s) > Build APK(s)`.
2. After finished building, pop up window will appear. Click `locate` button.
3. You are directed to directory containing `app-debug.apk`
