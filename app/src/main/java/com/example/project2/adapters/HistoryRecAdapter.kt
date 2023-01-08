package com.example.project2.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.project2.Completion
import com.example.project2.R
import com.example.project2.models.SaveDataModel
import com.example.project2.uiComponents.DAN
import com.example.project2.uiComponents.GOOD
import com.example.project2.uiComponents.MID
import com.example.project2.uiComponents.StatusChip
import com.facebook.shimmer.ShimmerFrameLayout
import java.text.SimpleDateFormat
import kotlin.math.floor

class HistoryRecAdapter(list: ArrayList<SaveDataModel>, context: Context, completion: Completion) :
    RecyclerView.Adapter<HistoryRecAdapter.MyViewHolder>() {
    private var list: ArrayList<SaveDataModel>
    private val context: Context
    private var loading: Boolean
    private val completion: Completion

    init {
        this.list = list
        this.context = context
        this.loading = true
        this.completion = completion
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView
        val date: TextView
        val value: TextView
        val chip: View
        val cl: ConstraintLayout
        val card: CardView

        init {
            card = itemView.findViewById(R.id.cardView)
            cl = card.findViewById(R.id.cl_main)
            name = cl.findViewById(R.id.name)
            date = cl.findViewById(R.id.date)
            value = cl.findViewById(R.id.value2)
            chip = cl.findViewById(R.id.status_chip)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.history_item_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val statusChip = StatusChip(holder.chip, context)

        if (loading) {
            holder.cl.visibility = View.INVISIBLE
        } else {
            holder.cl.visibility = View.VISIBLE

            val item = list[position]
            holder.date.text = getDate(item.date)
            holder.name.text = item.dname1
            val v = floor(item.prob1.toDouble() * 100).toInt()
            holder.value.text = "$v %"

            statusChip.setText(getStatus(v))
            holder.card.setOnClickListener {
                completion.onComplete(item)
            }
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun getStatus(v: Int): String {
        return if (v < 10) GOOD else if (v < 50) MID else DAN
    }

    private fun getDate(dateString: String?): String {
        val date = SimpleDateFormat("dd-MM-yyyy HH:mm").parse(dateString)
        val format = SimpleDateFormat("dd-MM-yyyy")
        return format.format(date)
    }

    fun endLoading() {
        this.loading = false
        notifyDataSetChanged()
    }
}