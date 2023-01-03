package com.example.project2.uiComponents

import android.animation.LayoutTransition
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.project2.Completion
import com.example.project2.R
import com.google.android.material.chip.Chip

class ExpandableCard(
    heading: String = "heading",
    details: String = "detailsView",
    value: Int = 0,
    view: View,
    completion: Completion? = null
) {
    private val card: View
    private val cl: ConstraintLayout
    private val textView: TextView
    private val detailsView: TextView
    private val valueView: TextView
    private val ind: ProgressBar
    private val find: Chip
    var expanded = false

    private val completion: Completion?

    init {
        card = view
        cl = card.findViewById(R.id.cl)
        cl.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        this.textView = card.findViewById(R.id.heading)
        this.detailsView = card.findViewById(R.id.details)
        this.valueView = card.findViewById(R.id.value)
        this.ind = card.findViewById(R.id.ind)
        find = card.findViewById(R.id.find)
        this.completion = completion

        textView.text = heading
        this.detailsView.text = details
        this.valueView.text = "Chances\n${value} %"
        ind.progress = value

    }


    fun transition() {
        when (expanded) {
            false -> expand()
            else -> contract()
        }
        expanded = !expanded

        TransitionManager.beginDelayedTransition(cl, AutoTransition())
    }

    private fun expand() {
        detailsView.visibility = View.VISIBLE
    }

    private fun contract() {
        detailsView.visibility = View.GONE
    }

    fun findMedication() {
        try {
            completion?.onComplete()
        } catch (e: java.lang.Exception) {
            completion?.onCancelled()
        }
    }

    fun setValue(h:String,d:String="",v:Int){
        textView.text = h
        valueView.text = "Chances\n$v %"
        ind.progress = v
    }
    fun setDetail(t:String){
        detailsView.text = t
    }



}