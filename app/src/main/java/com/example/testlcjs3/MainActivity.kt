package com.example.testlcjs3

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.webkit.WebView
import androidx.activity.ComponentActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONArray
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    private lateinit var webView: WebView
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webView = WebView(this)
        webView.settings.javaScriptEnabled = true
        // NOTE: Required for LCJS to work!
        webView.settings.domStorageEnabled = true
        setContentView(webView)
        // Load web app from src/assets
        webView.loadUrl("file:///android_asset/index.html")

        GlobalScope.launch {
            delay(2000) // Delay for 2 seconds
            runTestData()
        }
    }

    val numChannels = 3
    val frequency = 1000 // Hz

    /**
     * Stream data into charts in real-time.
     */
    suspend fun runTestData() {
        val timeIntervalMillis = (1.0 / frequency * 1000).toLong()
        val data = Array(numChannels) { Pair<MutableList<Long>, MutableList<Double>>(ArrayList(), ArrayList()) }
        var startTime = System.currentTimeMillis()

        while (true) {
            val currentTime = System.currentTimeMillis()
            val elapsedTime = currentTime - startTime
            val numDataPoints = (elapsedTime / timeIntervalMillis).toInt()

            if (numDataPoints > 0) {
                for (channel in 0 until numChannels) {
                    for (i in 0 until numDataPoints) {
                        val timestamp = currentTime - (numDataPoints - i) * (1000L / frequency)
                        val newDataPoint = generateRandomData(timestamp)
                        data[channel].first.add(timestamp)
                        data[channel].second.add(newDataPoint)
                    }

                    if (!data[channel].first.isEmpty() && !data[channel].second.isEmpty()) {
                        val xDataJson = JSONArray(data[channel].first)
                        val yDataJson = JSONArray(data[channel].second)
                        handler.post {
                            webView.loadUrl("javascript:appendData($channel, $xDataJson, $yDataJson)")
                        }
                        data[channel].first.clear()
                        data[channel].second.clear()
                    }
                }
                startTime = currentTime
            }
            delay(16) // Use delay from Kotlin coroutines
        }
    }

    fun generateRandomData(timestamp: Long): Double {
        val frequency = 0.1 // Adjust the frequency to control the curve
        val amplitude = 5.0  // Adjust the amplitude to control the curve height

        val timeInSeconds = timestamp / 1000.0
        val sineValue = amplitude * Math.sin(2 * Math.PI * frequency * timeInSeconds)

        return sineValue + Random.nextDouble(-0.3,0.3);
    }
}
