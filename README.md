# ♛ ChessBoard Importer Frontend ♛

Repository for the frontend part of project "Chessboard Importer". The project is realized by the team of 4 students of Control Engineering and Robotics on specialization Information Technologies in Control Engineering as part of the Team Project Conference on Faculty of Electronics for the Company NetworkedAssets. To read more about the backend part of the project visit the wiki page of the [backend application repository](https://gitlab.com/kpz-2021-chessboard-importer-team/chessboard-importer-backend). For more information visit the [wiki page](https://gitlab.com/kpz-2021-chessboard-importer-team/chessboard-importer-mobile/-/wikis/home)

## Authors

- Piotr Kowalski - Team leader
- Piotr Bednarek - Machine learning
- Kajetan Zdanowicz - DevOps
- Adam Bednorz - API, improving chess recognition
- Marcin Gruchała - Mobile application

## Demo

<img src="demo.gif" width="300">

## [Download APK file](chessboard-importer-1.0.apk)

If apk file will download as zip file, rename .zip file to .apk file.

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
