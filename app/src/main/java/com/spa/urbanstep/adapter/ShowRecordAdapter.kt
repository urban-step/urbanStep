package com.spa.urbanstep.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spa.urbanstep.DashboardType
import com.spa.urbanstep.R
import com.spa.urbanstep.model.response.RecordDetail
import kotlinx.android.synthetic.main.row_show_record.view.*


class ShowRecordAdapter(val context: Context, val projects: ArrayList<RecordDetail>, val type: Int) : RecyclerView.Adapter<ShowRecordAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView: View? = null

        itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_show_record, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val record = projects.get(position)
        if (type == DashboardType.GRIEVANCE.ordinal) {
            holder.detail.setText(record.problem)
            holder.category.text = record.category
            holder.subCategory.text = record.sub_category
        } else if (type == DashboardType.QUERY.ordinal) {
            holder.detail.setText(record.query)
        } else if (type == DashboardType.UPDATE_US.ordinal) {
            holder.detail.setText(record.topic)
        } else if (type == DashboardType.SUGGESTION.ordinal) {
            holder.detail.setText(record.suggestion)
        } else if (type == DashboardType.FEEDBACK.ordinal) {
            holder.detail.setText(record.feedback)
        } else if (type == DashboardType.DISCUSSION.ordinal) {
            holder.detail.setText(record.discuss)
        }
        holder.uniqueId.text = record.unique_id
        holder.date.setText(record.date_time)
        holder.status.setText(record.status)
    }

    override fun getItemCount(): Int {
        return projects.size
    }


    inner class ViewHolder(view: View?) : RecyclerView.ViewHolder(view) {
        val uniqueId = view!!.tv_project_name
        val date = view!!.tv_date
        val status = view!!.tv_status
        val place = view!!.tv_place
        val category = view!!.tv_category
        val subCategory = view!!.tv_sub_category
        val detail = view!!.tv_project_detail


    }
}