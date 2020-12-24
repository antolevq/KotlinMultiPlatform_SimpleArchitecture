package com.leva.androidApp.view.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.jetbrains.handson.androidApp.R
import com.leva.kmm.shared.domain.model.RocketLaunch

class LaunchesRvAdapter(var launchEntities: List<RocketLaunch>) : RecyclerView.Adapter<LaunchesRvAdapter.LaunchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaunchViewHolder {
        return LayoutInflater.from(parent.context)
            .inflate(R.layout.item_launch, parent, false)
            .run(::LaunchViewHolder)
    }

    override fun getItemCount(): Int = launchEntities.count()

    override fun onBindViewHolder(holder: LaunchViewHolder, position: Int) {
        holder.bindData(launchEntities[position])
    }

    inner class LaunchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val missionNameTextView = itemView.findViewById<TextView>(R.id.missionName)
        private val launchYearTextView = itemView.findViewById<TextView>(R.id.launchYear)
        private val launchSuccessTextView = itemView.findViewById<TextView>(R.id.launchSuccess)
        private val missionDetailsTextView = itemView.findViewById<TextView>(R.id.details)

        fun bindData(launchEntity: RocketLaunch) {
            val ctx = itemView.context
            missionNameTextView.text = ctx.getString(R.string.mission_name_field, launchEntity.missionName)
            launchYearTextView.text = ctx.getString(R.string.launch_year_field, launchEntity.launchYear.toString())
            missionDetailsTextView.text = ctx.getString(R.string.details_field, launchEntity.details ?: "")
            val launchSuccess = launchEntity.launchSuccess
            if (launchSuccess != null ) {
                if (launchSuccess) {
                    launchSuccessTextView.text = ctx.getString(R.string.successful)
                    launchSuccessTextView.setTextColor((ContextCompat.getColor(itemView.context,
                        R.color.colorSuccessful
                    )))
                } else {
                    launchSuccessTextView.text = ctx.getString(R.string.unsuccessful)
                    launchSuccessTextView.setTextColor((ContextCompat.getColor(itemView.context,
                        R.color.colorUnsuccessful
                    )))
                }
            } else {
                launchSuccessTextView.text = ctx.getString(R.string.no_data)
                launchSuccessTextView.setTextColor((ContextCompat.getColor(itemView.context,
                    R.color.colorNoData
                )))
            }
        }
    }
}