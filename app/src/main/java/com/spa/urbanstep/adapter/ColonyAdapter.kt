package com.spa.urbanstep.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spa.urbanstep.R
import com.spa.urbanstep.model.response.Colony
import kotlinx.android.synthetic.main.row_colony.view.*

class ColonyAdapter(val context: Context, val colonyList: ArrayList<Colony>) : RecyclerView.Adapter<ColonyAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView: View? = null

        itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_colony, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val colony = colonyList.get(position)
        holder.tv_zone_name.setText(String.format(context.getString(R.string.zone_name), colony.zone_name))
        holder.tv_zone_number.setText(String.format(context.getString(R.string.zone_number), colony.zone_number))
        holder.tv_ward_name.setText(String.format(context.getString(R.string.ward_name), colony.ward_name))
        holder.tv_ward_number.setText(String.format(context.getString(R.string.ward_number), colony.ward_number))
        holder.tv_colony.setText(String.format(context.getString(R.string.colony_name), colony.colony_name))
        holder.tv_colony_category.setText(String.format(context.getString(R.string.colony_category), colony.colony_category))

    }

    override fun getItemCount(): Int {
        return colonyList.size
    }


    inner class ViewHolder(view: View?) : RecyclerView.ViewHolder(view) {
        val tv_zone_name = view!!.tv_zone_name
        val tv_zone_number = view!!.tv_zone_number
        val tv_ward_name = view!!.tv_ward_name
        val tv_ward_number = view!!.tv_ward_number
        val tv_colony = view!!.tv_colony
        val tv_colony_category = view!!.tv_colony_category

    }
}