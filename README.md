# react-native-argyle-sdk

## Getting started

`$ npm install react-native-argyle-sdk --save` or `$ yarn add react-native-argyle-sdk`
`$ yarn add react-native-swift` (so that project can run swift code, mostly configuration thing)
`$ react-native link react-native-swift` (even for react-native version > 0.60.x projects, it's mostly configuration, not adding any actual projects)

### Mostly automatic installation

`$ react-native link react-native-argyle-sdk`

### iOS extra steps installation:

minimum deployment target needs to be at least 11.0

# react-native version >= 0.60.x
`$ cd ios`
`$ pod install`

# react-native version < 0.60.x
# with pods
`$ cd ios`
`$ pod install`

while installing pods, if getting error about React dependency, need to add `:modular_headers => true` for the `React` pod. i.e.:
pod 'React', path: rn_path, subspecs: [
  'Core',
  'RCTActionSheet',
  'DevSupport',
  'RCTAnimation',
  'RCTGeolocation',
  'RCTImage',
  'RCTLinkingIOS',
  'RCTNetwork',
  'RCTSettings',
  'RCTText',
  'RCTVibration',
  'RCTWebSocket',
  'CxxBridge'
  ], :modular_headers => true

# without pods
1. download native Argyle.framework (from https://github.com/argyle-systems/Argyle_iOS)
2. Add Argyle.framework to root ios/Frameworks folder (add Frameworks folder not present yet)
3. Build Settings > Always Embed Swift Standard Libraries > YES
4. Select root project -> main target -> build phases
        1. if there's no "Embedded frameworks" phase
            1. click + -> "new copy files phase"
            2. click "Destination" and select "Frameworks"
            3. click + below and add Argyle.framework
            4. You can rename this phase to "Embedded frameworks" (optional)
        2. if "Embedded frameworks" phase exists, just click + below and add Argyle.framework
5. (should be there) make sure `ARArgyleSdk.xcodeproj` has `$(PROJECT_DIR)/../../../ios/Frameworks`

### Manual installation

#### iOS

steps from `without pods` part, plus

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-argyle-sdk` and add `ARArgyleSdk.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libARArgyleSdk.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainApplication.java`
  - Add `import com.reactlibrary.ARArgyleSdkPackage;` to the imports at the top of the file
  - Add `new ARArgyleSdkPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-argyle-sdk'
  	project(':react-native-argyle-sdk').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-argyle-sdk/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-argyle-sdk')
  	```


## Usage
```javascript
import ARArgyleSdk from 'react-native-argyle-sdk'
ArgyleSdk.loginWith("your_plugin_key", "https://api-sandbox.develop.argyle.io", "")
ArgyleSdk.onUserCreated(res => console.log("onUserCreated", res))

ArgyleSdk.onAccountConnected(res => console.log("onAccountConnected", res))

ArgyleSdk.onAccountRemoved(res => console.log("onAccountRemoved", res))
ArgyleSdk.onError(error => {
    console.log("onError", error)
    if (error === ArgyleSdk.errorCodes.EXPIRED_TOKEN) {
        setTimeout(() => {
            // Simulated timeout before updating the token
            ArgyleSdk.updateToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIiwiZXhwIjoxNTk4MTA5OTE2LCJqdGkiOiJkZDI5ZDJjYWUxMjQ0OTlhYjY1NzlhOWEwMmQzZTMzNSIsInVzZXJfaWQiOiJlNjQwMzE5Zi1mYWJjLTRjMWYtYjIyMS0yZGFmNWFkMWY0NjEiLCJjbGllbnRfaWQiOiI0ODA3MTZkYy05ZDc3LTQ0OTEtYjVhNC0xODc5MzkxYjhmNjUiLCJpc19kZXZfb25seSI6dHJ1ZX0.BxE92-Iu67KM0CF9OihsCJjnxdagzCixZVvMmy8KWvQ")
        }, 300)
    }
})
ArgyleSdk.onTokenExpired(res => console.log("onTokenExpired", res))
```
