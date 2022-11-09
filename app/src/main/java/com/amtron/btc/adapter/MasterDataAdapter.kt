package com.amtron.btc.adapter

import android.annotation.SuppressLint
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amtron.btc.R
import com.amtron.btc.helper.DateHelper
import com.amtron.btc.helper.TimeHelper
import com.amtron.btc.model.MasterData
import com.google.android.material.card.MaterialCardView

class MasterDataAdapter(private val masterDataList: List<MasterData>) : RecyclerView.Adapter<MasterDataAdapter.ViewHolder>() {
    private lateinit var mListener: OnRecyclerViewItemClickListener

//    fun setOnItemClickListener(listener: OnRecyclerViewItemClickListener) {
//        mListener = listener
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.records_card_layout, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val masterData = masterDataList[position]

        holder.slNo.text = (position + 1).toString()
        holder.visitorName.text = masterData.name
        holder.ageAndSex.text = StringBuilder().append(masterData.age ).append(" (").append(masterData.gender + ")")
        holder.country.text = masterData.country
        holder.state.text = masterData.state
        holder.residency.text = masterData.residency
        holder.expand.setOnClickListener {
            if (holder.linearLayout.visibility == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(holder.card, AutoTransition())
                holder.linearLayout.visibility = View.GONE
                holder.expand.text = "EXPAND"
            } else {
                TransitionManager.beginDelayedTransition(holder.card, AutoTransition())
                holder.expand.text = "COLLAPSE"
                holder.linearLayout.visibility = View.VISIBLE
            }
        }
        holder.card.setOnClickListener {
            if (holder.linearLayout.visibility == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(holder.card, AutoTransition())
                holder.linearLayout.visibility = View.GONE
                holder.expand.text = "EXPAND"
            } else {
                TransitionManager.beginDelayedTransition(holder.card, AutoTransition())
                holder.expand.text = "COLLAPSE"
                holder.linearLayout.visibility = View.VISIBLE
            }
        }
    }

    override fun getItemCount(): Int {
        return masterDataList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card: MaterialCardView = itemView.findViewById(R.id.dataCard)
        val linearLayout: LinearLayout = itemView.findViewById(R.id.linearLayout)
        val expand: TextView = itemView.findViewById(R.id.expand)
        val slNo: TextView = itemView.findViewById(R.id.slNo)
        val visitorName: TextView = itemView.findViewById(R.id.visitorName)
//        val date: TextView = itemView.findViewById(R.id.date)
        val ageAndSex: TextView = itemView.findViewById(R.id.age_and_sex)
        val country: TextView = itemView.findViewById(R.id.country)
        val state: TextView = itemView.findViewById(R.id.state)
        val residency: TextView = itemView.findViewById(R.id.residency)
    }
}