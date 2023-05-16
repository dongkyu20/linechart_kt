package com.example.linechart1_

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet



class MainActivity : AppCompatActivity() {
    private var thread: Thread? = null
    private lateinit var lineChart : LineChart

//    private val floatTemp : ArrayList<float>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setChart()

    }

    private fun setChart() {
        lineChart = findViewById(R.id.lineChart)

        val xAxis: XAxis = lineChart.xAxis

        xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
//          textsize = 10f
            setDrawGridLines(false)
            granularity = 1f
            axisMinimum = 2f
            isGranularityEnabled = true

        }
        lineChart.apply {
            axisRight.isEnabled = false
            axisLeft.axisMaximum = 50f
            legend.apply {
                textSize = 15f
                verticalAlignment = Legend.LegendVerticalAlignment.TOP
                horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                orientation = Legend.LegendOrientation.HORIZONTAL
                setDrawInside(false)
            }
        }
        val lineData = LineData()
        lineChart.data = lineData
        feedMultiple()
    }

    private fun feedMultiple() {
        if (thread != null) {
            thread!!.interrupt()
        }

        val runnable = Runnable {

            addEntry()
        }

        thread = Thread(Runnable {
            while (true) {
                runOnUiThread(runnable)
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        })
        thread!!.start()
    }

    private fun addEntry() {
        val data: LineData = lineChart.data

        data?.let {
            var set : ILineDataSet? = data.getDataSetByIndex(0)

            if(set == null) {
                set = createSet()
                data.addDataSet(set)
            }
            data.addEntry(Entry(set.entryCount.toFloat(), 36.5f ),0)
            data.notifyDataChanged()
            lineChart.apply{
                notifyDataSetChanged()
                moveViewToX(data.entryCount.toFloat())
                setVisibleXRangeMaximum(4f)
                setPinchZoom(true)
                isDoubleTapToZoomEnabled = true
                description.text = "시간"
                setBackgroundColor(Color.WHITE)
                description.textSize = 15f
                setExtraOffsets(8f, 16f,8f, 16f)
            }
        }
    }

    private fun createSet(): LineDataSet {
        val set = LineDataSet(null, "")                       // lable 입력하면 상단에 코인명 뜸
        set.apply {
            axisDependency = YAxis.AxisDependency.LEFT
            color = Color.BLACK
            setCircleColor(Color.DKGRAY)
            valueTextSize = 10f
            lineWidth = 2f
            circleRadius = 3f
            fillAlpha = 0
            fillColor = Color.DKGRAY
            setDrawValues(true)
        }
        return set
    }
}

