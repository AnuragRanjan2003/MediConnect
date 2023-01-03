package com.example.project2.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.project2.R
import com.example.project2.models.SaveDataModel
import com.example.project2.uiComponents.DAN
import com.example.project2.uiComponents.GOOD
import com.example.project2.uiComponents.MID
import com.example.project2.uiComponents.StatusChip
import com.facebook.shimmer.ShimmerFrameLayout
import kotlin.math.floor

class HistoryRecAdapter(list: ArrayList<SaveDataModel>, context: Context) :
    RecyclerView.Adapter<HistoryRecAdapter.MyViewHolder>() {
    private var list: ArrayList<SaveDataModel>
    private val context: Context

    init {
        this.list = list
        this.context = context
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val placeholder: ShimmerFrameLayout
        val name: TextView
        val date: TextView
        val value: TextView
        val chip: View
        val cl: ConstraintLayout

        init {
            placeholder = itemView.findViewById(R.id.placeholder)
            name = itemView.findViewById(R.id.name)
            date = itemView.findViewById(R.id.date)
            value = itemView.findViewById(R.id.value)
            chip = itemView.findViewById(R.id.status)
            cl = itemView.findViewById(R.id.cl_main)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.history_item_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (itemCount == 0) {
            holder.cl.visibility = View.INVISIBLE
            holder.placeholder.visibility = View.VISIBLE
            holder.placeholder.startShimmer()
        } else {
            holder.placeholder.stopShimmer()
            holder.placeholder.visibility = View.INVISIBLE
            holder.cl.visibility = View.VISIBLE

            val item = list[position]
            holder.date.text = item.date
            holder.name.text = item.dname1
            val v=floor(item.prob1?.toDouble()!! * 100).toInt()
            holder.value.text = "$v %"
            val statusChip = StatusChip(holder.chip, context)
            statusChip.setText(getStatus(v))
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun getStatus(v: Int): String {
        return if(v<3) GOOD else if(v<6) MID else DAN
    }
}