# Garage-app

About Garage App

Garage App is a small project for managing your family vacation plans by adding your own vehicles and checking possible locations for nice, relaxing walk or trip.
The project targets latest Android version 16 of Android SDK, it's UI is written in Jetpack Compose and the project uses the latest technologies for building modern android apps, 
such as Hilt Di, Kotlin programming language, Kotlin Coroutines, Kotlin Flow, Room from Jetpack Architecture Components, Retrofit 2, etc..

There are two main screens in Garage App - My Garage, in which you can add/edit your vehicles, and My Places, in which you can browse the locations you have saved in your database.
Initially, all Places are sorted by Rating starting from the Highest to the Lowest so the user to be able to see first the best places. User can mark Places as `Favorite` and can review his favorite Places by clicking the Filter Chip `Favorite`. He can also switch the List of Places with a Map view, which to mark with pins for you all currently listed locations.

Garage App has few Unit and Instrumentation tests. The Unit tests are written on the new JUnit5 platform, while the Instrumentations are running on CustomTestRunner, which implements
AndroidJUnitRunner.

Running:

Project is developed in Android Studio IDE, version Narwhal 3 Feature Drop | 2025.1.3 RC 2. 
To run it, download the Source of the Project or Clone the Repository, open Android Studio IDE and Open the project folder.
Inside the project folder in `Garage-app/app/release/` you will find a release build `app-release.aab` for the project. You can install download it on your phone, install it and test the app with it.
I have also prepared a debug build - `app-debug.apk`. You will find it in folder `Garage-app/app/debug/`.

App Architecture:

MVVM/MVI blend based on the Clean Architecture pattern. It has Presentation Layer, which holds all the Composable views and the ViewModel and Domain Layer. 
Both of them are connected with a Repository, which is connected to the Data providers (External REST Services and Local Device Database) and provides the data,
which is required from the Presentation layer.
  
Running tests:

- To run the all Instrumentation tests at ones you have to run this command in the console -  `./gradlew connectedAndroidTest` (`gradlew connectedAndroidTest` for Windows). Instrumentation tests are running successfully on emulator but more important is how to use physical devices. In order to do it, you have to disable All Device Animations from the Development options so the tests to be able to run. The Animation are making the latency within the Main thread too big so it to sync with the Test Thread;
- To run the all Unit tests at ones you have to run this command in the console - `./gradlew test`(`gradlew test` for Windows). This runs all Unit tests for the whole Project. Garage app has only 1 Module so far, called `app`, so you can run all its test by calling `./gradlew app:test` (`gradlew app:test` for Windows). It is gonna run smoother and faster than `./gradlew test`;
- If you want to run All Tests within the app call `./gradlew app:test connectedAndroidTest` or `./gradlew test connectedAndroidTest`.

Handling No Network Connection and/or Disable Location setting of the Device

- If Network Connection is down when you start the app for first time there will be a screen, which says to you that you have to establish your Network connection. It shows a Snackbar at the bottom of the screen, which will lead the User to the WIFI Connection settings so he to Enable his WiFi or his MobileData and connect the device to the Internet. After returning to the app there will be a `Reload` button, which will make possible the user to Reload the screen and to use the app;
- If the user has disabled the Location (GPS) of his device, when he Extends the Place Card view there will be a Service you on its place, saying to him that he has to Switch ON his Location. The view will lead the user to the Location settings were he can Switch it on. Also, if he has disabled it only for this app, the app will ask for Permission for using the user's location.

What is coming next:

The=is app is a Prototype and it is very simple. In order it to grow up into a successful project it needs few more improvements and features:

- Make possible for user to Search for Locations by providing Coording (Latitude-LOngtitude) data or Search locations by name and calling an API with this data + with the Amount of Places he wants to see in the List;
- Region detection functionality - For example, as a touring you visit a new vacation destination. With this functionality the app should be able to scan the surrounded area gather the most popular places in it. With this the user will be able to prepare his trip/walk plans everywhere in the world very fast and very easy;
- More filters for Places and Vehicles list - Search view on top to filter Places by Name and Vehicles by Name, Year, Manufacturer, FuelType, etc. More sorting options - make possible
  to sort Places by rating and other fields;
- Write More tests;
- Adding a Continuous Integration (Ci) tool to build a proper processes for testing and distributing the project.
- Better Design and Color pallets for the Light and Dark theme;
- Better Icons;

This are the more important features that the App misses so far. 

Other good to have features:

- Add commercials;
- More functionalities can be added to the Google Maps's view;
- More information about the Vehicles, like when its ensurance policy expires, etc.
- In-app Purchase mechanism;
- Some gamification features might be good to have too. For example, if the user visits three chosen places in the next two days, he will be rewarded with internal currency for the app (Gold coins or something like that), with which he can buy No commercials for the next month instead of paying witl real money.
