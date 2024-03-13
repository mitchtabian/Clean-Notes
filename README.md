# [Clean Architecture](https://codingwithmitch.com/courses/android-clean-architecture/)

Android example with Clean Architecture by layer.

**Watch the course here:** [Clean Architecture](https://codingwithmitch.com/courses/android-clean-architecture/)

**NOTE** I got rid of dynamic feature modules because you cannot write tests currently. See bug: [google issue tracker](https://issuetracker.google.com/issues/145191501).

In the future I will make another course on Dynamic Feature Modules.

![logo](https://codingwithmitch.s3.amazonaws.com/static/courses/21/clean_architecture_diagrams.png)

# Running this app

To run this app you will need to create a firebase project and hook it up with the project. I password protected the login of mine so you won't be able to get into the app.

# Running the Tests
1. cd into /tests/
2. type in terminal: `run_tests.sh`
This will run all the unit tests and instrumentation tests. It will also start the firebase emulator to simulate firestore db.
**The test results** are in `/app/build/reports/`.

# Credits
1. https://proandroiddev.com/gradle-dependency-management-with-kotlin-94eed4df9a28
2. https://proandroiddev.com/intro-to-app-modularization-42411e4c421e
3. https://www.droidcon.com/media-detail?video=380845032
4. https://proandroiddev.com/kotlin-clean-architecture-1ad42fcd97fa
5. https://www.raywenderlich.com/3595916-clean-architecture-tutorial-for-android-getting-started
6. http://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html
7. https://developer.android.com/guide/navigation/navigation-dynamic
8. https://proandroiddev.com/navigation-with-dynamic-feature-modules-48ee7645488
9. https://medium.com/androiddevelopers/patterns-for-accessing-code-from-dynamic-feature-modules-7e5dca6f9123
10. https://hackernoon.com/android-components-architecture-in-a-modular-word-d0k32i6
This project is tested with BrowserStack
