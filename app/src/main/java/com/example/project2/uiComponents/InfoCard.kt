package com.example.project2.uiComponents

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.project2.R

class InfoCard(view: View) {
    private val text1: TextView
    private val text2: TextView
    private val head1: TextView
    private val head2: TextView
    private val card: View
    private val cl: ConstraintLayout
    private val icon1: ImageView
    private val icon2: ImageView

    init {
        card = view
        cl = card.findViewById<CardView>(R.id.c1).findViewById(R.id.cl_info)
        text1 = cl.findViewById(R.id.p_text1)
        head1 = cl.findViewById(R.id.p_text3)
        text2 = cl.findViewById(R.id.p_text2)
        head2 = cl.findViewById(R.id.p_text4)
        icon1 = cl.findViewById(R.id.icon2)
        icon2 = cl.findViewById(R.id.icon1)
    }

    fun setTexts(t1: String, t2: String) {
        text1.text = t1
        text2.text = t2
    }

    fun setBgColor(color: Int) {
        cl.setBackgroundColor(color)
    }

    fun setHead(t1: String, t2: String) {
        head1.text = t1
        head2.text = t2
    }

    fun setIcons(d1: Drawable, d2: Drawable) {
        icon1.setImageDrawable(d1)
        icon2.setImageDrawable(d2)
    }


}