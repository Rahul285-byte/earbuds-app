# Earbuds Battery - Android Project

This is a minimal Android Studio project that listens for Bluetooth battery broadcasts
and displays battery percentage (if the earbud and Android version support it).

## How to build locally (Android Studio)
1. Install Android Studio (2022.3+ recommended).
2. Open this folder as a project.
3. Let Gradle sync and install any SDK components if prompted.
4. Connect your Android device (USB debug) or use an emulator and Run the app.

## How to get an installable APK using GitHub Actions (CI)
1. Create a GitHub repo and push this project.
2. Add the provided `.github/workflows/android-build.yml` workflow (already in this zip).
3. On any push, the workflow will build and upload an APK artifact you can download from the Actions page.

Note: Not all earbuds broadcast battery levels to Android. Some manufacturers keep battery info behind proprietary apps or use Bluetooth LE GATT profiles that need device-specific code.
