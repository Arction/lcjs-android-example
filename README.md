# LightningChart<sup>&#174;</sup> JS Android usage example

This repository showcases how to use LightningChart<sup>&#174;</sup> JS charting library in a Android project.

More information about LightningChart<sup>&#174;</sup> JS can be found from our website, https://www.arction.com/lightningchart-js/.

## Getting Started

The project can be opened with Android Studio and run on the emulator or an actual device. Refer to the [Android Developer Guide](https://developer.android.com/guide) for instructions for Android development.

The chart is running inside a [WebView](https://developer.android.com/reference/android/webkit/WebView). Data is send from the native Android code to the WebView to be displayed.
You can find the source code for the charts from `/app/src/main/assets`. `index.html` is the page that is running inside the WebView and contains the JavaScript for configuring the chart.
`lcjs.iife.js` is IIFE packaged version of LightningChart<sup>&#174;</sup> JS that can be found inside the NPM package.

JavaScript has to be enabled on the WebView for the chart to work!

```java
webView.getSettings().setJavaScriptEnabled(true);
```

### Sending data to the chart

Data can be send to the chart from native Android code with the help of [`evaluateJavascript`](https://developer.android.com/reference/android/webkit/WebView#evaluateJavascript(java.lang.String,%20android.webkit.ValueCallback%3Cjava.lang.String%3E)) method.

```java
view.evaluateJavascript("setData('" + xyArray + "')", null);
```

The `setData` is a JavaScript method that has been defined inside the WebView. The `evaluateJavascript` method will call that method to set data in the chart. The contents of `xyArray` is provided as the argument to the `setData` method.

```js
function setData(data){
    var arrData = data.split("||")
    const ArrX = JSON.parse(arrData[0])
    const ArrY = JSON.parse(arrData[1])
    const chartData = ArrX.map( ( itr1, itr2 ) => ( { x: itr1, y: ArrY[itr2] } ) )
    lineSeries.add(chartData)
}
```

### Support

If you notice an error in the example code, please open an issue on [GitHub][0].

Official [API documentation][1] can be found on [Arction][2] website.

If the docs and other materials do not solve your problem as well as implementation help is needed, ask on [StackOverflow][3] (tagged lightningchart).

If you think you found a bug in the LightningChart JavaScript library, please contact support@arction.com.

Direct developer email support can be purchased through a [Support Plan][4] or by contacting sales@arction.com.

© Arction Ltd 2009-2020. All rights reserved.

Android is a trademark of Google LLC.

[0]: https://github.com/Arction/lcjs-html-example/issues
[1]: https://www.arction.com/lightningchart-js-api-documentation
[2]: https://www.arction.com
[3]: https://stackoverflow.com/questions/tagged/lightningchart
[4]: https://www.arction.com/support-services/
