package com.example.project2.uiComponents

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.project2.R

const val GOOD = "good"
const val MID = "mid"
const val DAN = "danger"

class StatusChip(view: View, context: Context) {
    private val text: TextView
    private val chip: View
    private val card : CardView
    private val cl: ConstraintLayout
    private val activity: Context

    init {
        chip = view
        card = chip.findViewById(R.id.card1)
        cl = card.findViewById(R.id.cl1)
        text = cl.findViewById(R.id.status)

        activity = context
    }

    fun setText(status: String) {
        text.text = status
        setColor(status)
    }

    private fun setColor(status: String) {
        when (status) {
            GOOD -> setGreen()
            MID -> setYellow()
            DAN -> setRed()
            else -> setGreen()
        }
    }

    private fun setRed() {
        text.setTextColor(activity.resources.getColor(R.color.md_theme_light_error, null))
        cl.setBackgroundColor(
            activity.resources.getColor(
                R.color.md_theme_light_errorContainer,
                null
            )
        )
    }

    private fun setYellow() {
        text.setTextColor(activity.resources.getColor(R.color.yellow_text, null))
        cl.setBackgroundColor(activity.resources.getColor(R.color.yellow_light, null))
    }

    private fun setGreen() {
        text.setTextColor(activity.resources.getColor(R.color.green_text, null))
        cl.setBackgroundColor(activity.resources.getColor(R.color.green_light, null))
    }


}