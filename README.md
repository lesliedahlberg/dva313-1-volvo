# Eco-Drive for Volvo Co-Pilot
Volvo Eco-Drive is a custom app for Volvo's Co-Pilot tablets which they use in machinery such as excavators and wheel loaders. Eco-Drive enables machine operators to approach eco-friendly driving as a game and provides live-feedback to how well they are doing.

## The App
The mobile app can be found in the directory /app. It can be installed on any Volvo Co-Pilot and provides live-data about eco-driving.

### Installation instructions
- Download CPAC for Android API 15
- Create a resource file names secret.xml and add a string definition ```<string name="token">INSERT YOUR TOKEN HERE</string>```
- Add the "theKey.jks" to "app" folder
- Connect to Co-Pilot adb connect IP_ADDRESS
- Build and push to CPAC tablet in Android Studio

## The Web Portal
The Web Portal provides a updated high-score list with the 20 best drivers.
