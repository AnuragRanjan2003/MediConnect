package com.example.project2.uiComponents

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.project2.R

class AnimatedButton(text: String, changeText: String, icon: Drawable, view: View) {
    private val text: String
    private val changeText: String
    private val icon: Drawable
    private val button: View
    private val cl: ConstraintLayout
    private var state :Boolean

    init {
        this.text = text
        this.changeText = changeText
        this.icon = icon
        button = view
        cl = button.findViewById(R.id.cl)
        this.state = false

        button.findViewById<TextView>(R.id.text).text = text
        button.findViewById<ImageView>(R.id.icon).setImageDrawable(icon)
    }


    fun clicked() {
        if (!state) {
            button.findViewById<TextView>(R.id.text).text = changeText
            button.findViewById<ProgressBar>(R.id.prg).visibility = View.VISIBLE
        } else {
            button.findViewById<TextView>(R.id.text).text = text
            button.findViewById<ProgressBar>(R.id.prg).visibility = View.GONE
        }
        state = !state
    }


}