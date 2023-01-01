package com.example.project2.adapters

import android.content.Context
import android.util.Log.e
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.project2.R
import com.example.project2.models.SurveyItem
import com.example.project2.models.Symptom
import com.google.android.material.chip.Chip

class SurveyRecAdapter(surveyList: ArrayList<SurveyItem>, context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    private var surveyList: ArrayList<SurveyItem>
    private var fullList: List<SurveyItem>
    private val context: Context

    init {
        this.surveyList = surveyList
        this.context = context
        this.fullList = ArrayList(surveyList)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val heading: TextView = itemView.findViewById(R.id.heading)
        val Content: TextView = itemView.findViewById(R.id.content)
        val chipNone: Chip = itemView.findViewById(R.id.chip_none)
        val chipMid: Chip = itemView.findViewById(R.id.chip_mid)
        val chipHigh: Chip = itemView.findViewById(R.id.chip_high)
    }

    class EditFieldViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val heading: TextView = itemView.findViewById(R.id.heading)
        val Content: TextView = itemView.findViewById(R.id.content)
        val editValue: EditText = itemView.findViewById(R.id.et_value)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position <= 5)
            R.layout.symptom_rec_editfield_item
        else R.layout.symptom_rec_item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.symptom_rec_item -> MyViewHolder(
                LayoutInflater.from(context).inflate(viewType, parent, false)
            )
            R.layout.symptom_rec_editfield_item -> EditFieldViewHolder(
                LayoutInflater.from(context).inflate(viewType, parent, false)
            )
            else -> MyViewHolder(LayoutInflater.from(context).inflate(viewType, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MyViewHolder -> bindForViewHolder(holder, position)
            is EditFieldViewHolder -> bindForEditField(holder, position)
            else -> e("Adapter", "view is not of any type")
        }
    }

    private fun bindForViewHolder(holder: MyViewHolder, position: Int) {
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
            } else {
                holder.chipNone.isChecked = true
                surveyList[position].value = "none"
            }
        }

        holder.chipHigh.setOnClickListener {
            if (holder.chipHigh.isChecked) {
                surveyList[position].value = "high"
            } else {
                holder.chipNone.isChecked = true
                surveyList[position].value = "none"
            }
        }
    }

    private fun bindForEditField(holder: EditFieldViewHolder, position: Int) {
        val item = surveyList[position]
        holder.heading.text = item.heading
        holder.Content.text = item.content
        holder.editValue.setText("default")
        holder.editValue.doAfterTextChanged {
            if (it.toString() == "default")
                surveyList[position].value = "none"
            else
                surveyList[position].value = it.toString()
        }
    }


    override fun getItemCount(): Int {
        return surveyList.size
    }

    fun getList(): List<Symptom> {
        val list = ArrayList<Symptom>()
        for (item in surveyList) {
            if (item.value != "none")
                list.add(parseItemToValue(item))
        }
        return list
    }

    private fun parseItemToValue(item: SurveyItem): Symptom {
        val temp = item
        when (item.value) {
            "mid" -> temp.value = "20"
            "high" -> temp.value = "50"
        }
        return Symptom(temp.heading,temp.value)
    }

    override fun getFilter(): Filter {
        return mFilter
    }

    private val mFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = ArrayList<SurveyItem>()

            if (constraint.isNullOrBlank()) {
                filteredList.addAll(fullList)
            } else {
                val query = constraint.toString().trim()
                for (item in fullList) {
                    if (item.heading.contains(query,true))
                        filteredList.add(item)
                }
            }
            val results =  FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            surveyList.clear()
            if (results != null)
                surveyList.addAll(results.values as List<SurveyItem>)
            notifyDataSetChanged()
        }
    }


}