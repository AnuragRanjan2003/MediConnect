package com.example.project2.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project2.R
import com.example.project2.models.SurveyItem
import com.google.android.material.chip.Chip

class SurveyRecAdapter(surveyList: List<SurveyItem>, context: Context) :
    RecyclerView.Adapter<SurveyRecAdapter.MyViewHolder>() {
    private var surveyList: List<SurveyItem>
    private val context: Context

    init {
        this.surveyList = surveyList
        this.context = context
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val heading: TextView = itemView.findViewById(R.id.heading)
        val Content: TextView = itemView.findViewById(R.id.content)
        val chipNone: Chip = itemView.findViewById(R.id.chip_none)
        val chipMid: Chip = itemView.findViewById(R.id.chip_mid)
        val chipHigh: Chip = itemView.findViewById(R.id.chip_high)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.symptom_rec_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = surveyList[position]
        holder.heading.text = item.heading
        holder.Content.text = item.content

        holder.chipNone.isChecked = true

        holder.chipNone.setOnClickListener {
            surveyList[position].value = "none"
        }

        holder.chipMid.setOnClickListener {
            if (holder.chipMid.isChecked) {
                surveyList[position].value = "mid"
            }else{
                holder.chipNone.isChecked = true
                surveyList[position].value = "none"
            }
        }

        holder.chipHigh.setOnClickListener {
            if (holder.chipHigh.isChecked) {
                surveyList[position].value = "high"
            }else{
                holder.chipNone.isChecked = true
                surveyList[position].value = "none"
            }
        }
    }


    override fun getItemCount(): Int {
        return surveyList.size
    }

    fun getList(): List<SurveyItem> {
        return this.surveyList
    }
}