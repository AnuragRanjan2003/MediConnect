package com.example.project2.uiComponents

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.project2.Completion
import com.example.project2.R

class AnimatedButton(
    text: String,
    changeText: String,
    icon: Drawable,
    view: View,
    iconEnabled: Boolean = true,
    textColor: Int = Color.GRAY,
    completion: Completion? = null
) {
    private val text: String
    private val changeText: String
    private val icon: Drawable
    private val button: View
    private val cl: ConstraintLayout
    private var state: Boolean
    private val iconEnabled: Boolean
    private val textColor: Int
    private val comp: Completion?


    init {
        this.text = text
        this.changeText = changeText
        this.icon = icon
        button = view
        cl = button.findViewById(R.id.cl)
        this.state = false
        this.iconEnabled = iconEnabled
        this.textColor = textColor
        comp = completion

        button.findViewById<TextView>(R.id.text).text = text
        button.findViewById<TextView>(R.id.text).setTextColor(textColor)
        button.findViewById<ImageView>(R.id.icon).setImageDrawable(icon)

        if (!iconEnabled)
            button.findViewById<ImageView>(R.id.icon).visibility = View.GONE


    }


    fun activate() {

        button.findViewById<TextView>(R.id.text).text = changeText
        button.findViewById<ProgressBar>(R.id.prg).visibility = View.VISIBLE
        comp?.onComplete()

    }

    fun deactivate() {

        button.findViewById<TextView>(R.id.text).text = text
        button.findViewById<ProgressBar>(R.id.prg).visibility = View.GONE
        comp?.onCancelled(name = "button", message = "cancelled")

    }


}