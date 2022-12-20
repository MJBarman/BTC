package com.amtron.btc.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amtron.btc.R
import com.amtron.btc.model.MasterData
import com.google.android.material.button.MaterialButton

class RecordsAdapter(private val masterDataList: List<MasterData>) :
    RecyclerView.Adapter<RecordsAdapter.ViewHolder>() {

    private lateinit var mItemClickListener: OnRecyclerViewItemClickListener

    fun setOnItemClickListener(mItemClickListener: OnRecyclerViewItemClickListener?) {
        this.mItemClickListener = mItemClickListener!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.records_card_layout_new, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val masterData = masterDataList[position]

        holder.slNo.text = (position + 1).toString()
        holder.visitorName.text = masterData.name
        holder.ageAndSex.text =
            StringBuilder().append(masterData.age).append(" (").append(masterData.gender + ")")
        if (masterData.residency.isNotEmpty()) {
            holder.residency.text =
                masterData.state + " (" + masterData.residency + "), " + masterData.country
        } else {
            holder.residency.text = masterData.state + ", " + masterData.country

        }

        /*holder.expand.setOnClickListener {
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
                holder.expand.text = "EXPAND"
                holder.linearLayout.visibility = View.GONE

            } else {
                TransitionManager.beginDelayedTransition(holder.card, AutoTransition())
                holder.expand.text = "COLLAPSE"
                holder.linearLayout.visibility = View.VISIBLE
            }
        }*/

        holder.deleteRecord.setOnClickListener {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClickListener(position, "delete")
            }
        }
    }

    override fun getItemCount(): Int {
        return masterDataList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //        val card: MaterialCardView = itemView.findViewById(R.id.dataCard)
        val deleteRecord: MaterialButton = itemView.findViewById(R.id.delete_record)

        //        val linearLayout: LinearLayout = itemView.findViewById(R.id.linearLayout)
//        val expand: TextView = itemView.findViewById(R.id.expand)
        val slNo: TextView = itemView.findViewById(R.id.slNo)
        val visitorName: TextView = itemView.findViewById(R.id.visitorName)

        //        val date: TextView = itemView.findViewById(R.id.date)
        val ageAndSex: TextView = itemView.findViewById(R.id.age_and_sex)

        //        val country: TextView = itemView.findViewById(R.id.country)
//        val state: TextView = itemView.findViewById(R.id.state)
        val residency: TextView = itemView.findViewById(R.id.residency)
    }
}