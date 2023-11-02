# LightningChart JS x Android template

This is a minimal example of using LightningChart JS in an Android application.

The project was bootstrapped with [Android Studio](https://developer.android.com/studio) 2022.3.1.20, Kotlin DSL, API 24.

## Running the application

Please refer to Android Studio materials on instructions how to run Android applications.

**Emulators may not be supported!** Many emulators still don't support WebGL as well as browsers do, so they may be completely non-functional with LightningChart JS or may perform very badly.
This repository was tested only using locally connected physical Android phones.

In order to run the application, you need to [get a license key](https://lightningchart.com/js-charts/#license-key) and supply it in [index.js file](https://github.com/Arction/lcjs-react-template/blob/master/app/src/main/assets/index.js).

![Screen recording of LightningChart JS in Android application](./recording.mp4)

## How can LightningChart JS be used in an Android application?

LightningChart JS is a JavaScript library for high-performance data visualization.
It is an extensive suite of data visualization features and provides the greatest performance in the world of JavaScript charts. Read more general information about LightningChart JS [here](https://lightningchart.com/js-charts/docs/).

LightningChart JS can be embedded into Android applications by placing a separate **Web application** (HTML/CSS/JS) into the code base and loading it inside a `WebView` component.

The main application is still developed like any Android application, but the charting parts are isolated to a separate application, which is developed in `JavaScript` or `TypeScript`. Data is generally connected to the Android application, and transferred from there to the Web application. This example shows an example how this can be done.

For more information about using Web-based content in Android application, you may refer to materials maintained by Android.
See "Web-based content" in Android developer materials, for example. https://developer.android.com/develop/ui/views/layout/webapps

## Step-by-step guide to adding LightningChart JS to an Android application

Here you can find the steps that were done to create this project template, so you can follow them for any existing Android application.

All LightningChart JS related code changes have been isolated to a single commit, which you can conveniently view [here](https://github.com/Arction/lcjs-android-example/commit/e714979599698f90971cb6c631d344ed2432a2b5).

### 1. Add charting Web application to code base

In the example these are static HTML/CSS/JS files.
Alternatively, you could dynamically build these files from a more complicated code base for the Web application, and utilize `Node.js` modules, for example.

These files can be found under `app/src/main/assets`:

- [`index.html`](https://github.com/Arction/lcjs-react-template/blob/master/app/src/main/assets/index.html)
- [`index.js`](https://github.com/Arction/lcjs-react-template/blob/master/app/src/main/assets/index.js)
- `lcjs.iife.js`
  - This is the IIFE bundle of LightningChart JS library, downloaded from [NPM](https://www.npmjs.com/package/@arction/lcjs?activeTab=code)

### 2. Add required project configurations for using LightningChart JS in WebView

- Add `implementation("androidx.webkit:webkit:1.8.0")` to [`app/build/gradle.kts`](https://github.com/Arction/lcjs-react-template/blob/master/app/build.gradle.kts). Required for using WebView.
- Add `<uses-permission android:name="android.permission.INTERNET" />` to [`app/src/main/AndroidManifest.xml`](https://github.com/Arction/lcjs-react-template/blob/master/app/src/main/AndroidManifest.xml). Required for LightningChart JS developer/trial license verification.

### 3. Display the Web application in a WebView

In your applications Activity where you want to display the chart(s), create a `WebView` and load the `index.html` asset.

```kotlin
class MainActivity : ComponentActivity() {
    private lateinit var webView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        webView = WebView(this)
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        setContentView(webView)
        webView.loadUrl("file:///android_asset/index.html")
    }
}
```

Make sure both JavaScript and DOM storage are enabled for the WebView! (as seen above)

This should result in the Web application becoming visible in the Android application.
You can inspect the JavaScript code running with the "Logcat" tab. If there are license or other errors in JavaScript run-time, they should popup there.

Please note that the `index.js` file found in this repository does not display any chart until data is actually sent to the WebView.

### 4. Communicate with the Web application and send data to charts

There are a number of different methods of communicating between the Android application and the Web application.
**This example utilizes the `loadUrl` method to trigger a function call in JavaScript side with parameters injected as strings from Android side.**

```js
// Function definition in Web application.
//      channel = string
//      xValues = list of x values
//      yValues = list of y values
function appendData(channel, xValues, yValues) {}
```

```kotlin
// Trigger JavaScript function from Android application and send XY data points over as parameters.
val channel = 0
val xValues = listOf(1000, 2000, 3000, 4000, 5000)
val yValues = listOf(10.0, 20.0, 15.0, 25.0, 30.0)
webView.loadUrl("javascript:appendData($channel, ${JSONArray(xValues)}, ${JSONArray(yValues)})")
```

## More information

Learn more about LightningChart JS and Android [here](https://lightningchart.com/js-charts/docs/frameworks/android/).

Learn more about connecting to real-time data sources [here](https://lightningchart.com/js-charts/docs/basic-topics/real-time-data/)
