package com.spa.urbanstep.adapter

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spa.urbanstep.R
import com.spa.urbanstep.model.Zone
import kotlinx.android.synthetic.main.row_know_area.view.*

class KnowAreaAdapter(val context: Context, val projects: ArrayList<Zone>, val isZone: Boolean, val listener: ClickListner) : RecyclerView.Adapter<KnowAreaAdapter.ViewHolder>() {

    interface ClickListner {
        fun onWardDetailClick(zone: Zone, isZone: Boolean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView: View? = null

        itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_know_area, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val zone = projects.get(position)
        if (isZone) {
            holder.zoneNumber.setText("" + zone.zone_number)
        } else {
            holder.zoneNumber.setText(String.format("" + zone.id!!))
        }
        holder.zoneName.setText(zone.name)

        if (zone.color != null) {
            holder.map_color.setBackgroundColor(Color.parseColor(zone.color!!))
        }

        if (zone.name.equals("Central", true) || zone.name.equals("Karol Bagh", true) || zone.name.equals("Chitranjan Park", true) || zone.name.equals("Naraina", true)) {
            holder.tv_view.setEnabled(true);
            holder.tv_view.setAlpha(1f);
        } else {
            holder.tv_view.setAlpha(.5f);
            holder.tv_view.setEnabled(false);
        }

        holder.tv_view.setOnClickListener {

            listener.onWardDetailClick(zone!!, isZone)
        }
    }

    override fun getItemCount(): Int {
        return projects.size
    }


    inner class ViewHolder(view: View?) : RecyclerView.ViewHolder(view) {
        val zoneNumber = view!!.tv_zone_number
        val zoneName = view!!.tv_zone_name
        val tv_view = view!!.tv_view
        val map_color = view!!.tv_map_legend
    }
}