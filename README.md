# YummCart — Practicals 2–6 and 8

A standalone Java/XML-free programmatic Android UI learning project for CS7035. The app is intentionally separate from Student Information, Practical 9 and Practical 10.

## Visual direction: Forest & Mint
- Deep forest green, warm ivory, mint and sage accents.
- No orange/saffron palette.
- Compact bottom navigation, hero banner, horizontally scrolling category chips, dish cards with prices, and cart quantity controls.
- Subtle entrance and tap animations; animated splash mark with progress indicator.

## Included source
- Authentication entry/registration screens (`AuthActivity.java`)
- Food menu, category filters, account and camera intent, cart and order confirmation (`FoodActivity.java`)
- SQLite seed data and local cart/order/account tables (`Db.java`)
- Airplane Mode broadcast receiver and order ContentProvider

## Open in Android Studio
1. Extract this folder.
2. Open the extracted `YummCart_Practicals_2-6_8` directory in Android Studio.
3. Let Gradle sync using Android Gradle Plugin 8.6.1 or a compatible version and JDK 17.
4. Run on an emulator or Android phone.

## Important verification status
The source has been revised to improve the visual layout and fix obvious Java source issues found during review (including duplicate menu methods and an invalid multiline Java string in the prior version). This environment does not have Android SDK/Gradle installed, so a Gradle build, APK install and emulator/device run could not be executed here. Treat the project as a revised source package pending Android Studio build and practical testing. Firebase/network payment services are not part of YummCart.
