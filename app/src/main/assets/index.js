console.log("hello from index.js");
const {
  lightningChart,
  Themes,
  AxisScrollStrategies,
  disableThemeEffects,
  AxisTickStrategies,
  emptyFill,
} = lcjs;

const lc = lightningChart({
  // Put your trial/developer license key here. You can get one for free from: https://lightningchart.com/js-charts/#license-key
});

const container = document.getElementById("chart");
const channels = [];
const theme = disableThemeEffects(Themes.light);

function appendData(channel, xValues, yValues) {
  let ch = channels[channel];
  if (!ch) {
    const chContainer = document.createElement("div");
    container.append(chContainer);
    chContainer.className = "channel";
    const chart = lc
      .ChartXY({
        container: chContainer,
        theme,
        defaultAxisX: { type: "linear-highPrecision" },
      })
      .setTitle("")
      .setPadding({ left: 10, right: 10, top: 5, bottom: 5 });
    const series = chart
      .addPointLineAreaSeries({ dataPattern: "ProgressiveX" })
      .setAreaFillStyle(emptyFill)
      .setStrokeStyle((stroke) => stroke.setThickness(1))
      .setMaxSampleCount(100_000);
    chart.forEachAxis((axis) => axis.setTickStrategy(AxisTickStrategies.Empty));
    chart
      .getDefaultAxisX()
      .setScrollStrategy(AxisScrollStrategies.progressive)
      .setInterval({
        // NOTE: 10 second time view
        start: -10_000,
        end: 0,
        stopAxisAfter: false,
      });
    ch = { chart, series };
    channels[channel] = ch;
  }
  ch.series.appendSamples({ xValues, yValues });
}

// Measure FPS
const FPSTracker_requestAnimationFrame = ((opts) => {
  let tStart = Date.now();
  let frames = 0;
  let fps = 0;
  const recordFrame = () => {
    frames++;
    const tNow = Date.now();
    fps = 1000 / ((tNow - tStart) / frames);
    requestAnimationFrame(recordFrame);
    channels[0]?.chart
      .setTitlePosition("series-left-top")
      .setTitle(`FPS: ${fps.toFixed(1)}`);
  };
  requestAnimationFrame(recordFrame);
  setInterval(() => {
    tStart = Date.now();
    frames = 0;
  }, 5000);
})();
