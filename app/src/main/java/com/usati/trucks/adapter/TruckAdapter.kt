package com.usati.trucks.adapter

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.usati.trucks.R
import com.usati.trucks.databinding.ListItemBinding
import com.usati.trucks.models.Data
import com.usati.trucks.utils.Utils
import java.util.*
import kotlin.collections.ArrayList


class TruckAdapter(
    var data: ArrayList<Data>
) : RecyclerView.Adapter<TruckAdapter.TruckViewHolder>(), Filterable {
    private var dataList: ArrayList<Data>

    inner class TruckViewHolder(var binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    init {
        dataList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TruckViewHolder {
        val binding = ListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TruckViewHolder(binding)
    }

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    override fun onBindViewHolder(holder: TruckViewHolder, position: Int) {
        val truck = dataList[position]
        holder.binding.apply {
            if (truck.deactivated) {
                cl.background = ColorDrawable(R.color.red)
            }
            val truckState = if (truck.lastRunningState.truckRunningState == 0) {
                "Stopped"
            } else {
                "Running"
            }
            tvTruckNo.text = truck.truckNumber
            tvCreateTime.text =
                Utils.getDateDifference(truck.lastWaypoint.updateTime).split(" ")[0]
            tvDayHrMinSec.text =
                " ${Utils.getDateDifference(truck.lastWaypoint.createTime).split(" ")[1]}" + " ago"
            tvStopStartTime.text =
                "$truckState since last ${Utils.getDateDifference(truck.lastRunningState.stopStartTime)}"
            if (truck.lastRunningState.truckRunningState == 0) {
                tvSpeed.visibility = View.INVISIBLE
                tvKm.visibility = View.INVISIBLE
            } else {
                tvSpeed.visibility = View.VISIBLE
                tvKm.visibility = View.VISIBLE
                tvSpeed.text = truck.lastWaypoint.speed.toString()
            }
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    dataList = data
                } else {
                    val resultList = ArrayList<Data>()
                    for (row in data) {
                        if (row.truckNumber.lowercase(Locale.ROOT).filter { !it.isWhitespace() }
                                .startsWith(charSearch.lowercase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                    }
                    dataList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = dataList
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                dataList = results?.values as ArrayList<Data>
                notifyDataSetChanged()
            }
        }
    }
}