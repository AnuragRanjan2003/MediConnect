package com.example.project2.uiComponents

import android.app.AlertDialog
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.example.project2.R

class AnalysisDialog(pieHeading: String = "", view: AlertDialog) {
    private val view: AlertDialog

    init {
        this.view = view
    }

    fun setData(list: List<DataEntry>) {
        val chart: AnyChartView = view.findViewById(R.id.chart)
        val pie = AnyChart.pie()
        pie.data(list)
        chart.setChart(pie)
    }
}