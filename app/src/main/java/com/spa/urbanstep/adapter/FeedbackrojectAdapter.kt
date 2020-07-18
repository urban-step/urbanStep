package com.spa.urbanstep.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spa.urbanstep.R
import com.spa.urbanstep.model.response.FeedbackResponse
import kotlinx.android.synthetic.main.row_feedback_project.view.*

class FeedbackrojectAdapter(val context: Context, val feedbackList: ArrayList<FeedbackResponse.ProjectFeedback>) : RecyclerView.Adapter<FeedbackrojectAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView: View? = null

        itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_feedback_project, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val feedback = feedbackList.get(position)
        holder.tv_name.setText(String.format(context.getString(R.string.project_name), feedback.project_name))
        holder.tv_description.setText(String.format(context.getString(R.string.project_description), feedback.project_description))
        holder.tv_location.setText(String.format(context.getString(R.string.project_location), feedback.location))
        holder.tv_feedback.setText(String.format(context.getString(R.string.project_feedback), feedback.feedback))
        holder.tv_date.setText(String.format(context.getString(R.string.date_time), feedback.date_time))

    }

    override fun getItemCount(): Int {
        return feedbackList.size
    }


    inner class ViewHolder(view: View?) : RecyclerView.ViewHolder(view) {
        val tv_name = view!!.tv_project_name
        val tv_description = view!!.tv_description
        val tv_location = view!!.tv_location
        val tv_feedback = view!!.tv_project_feedback
        val tv_date = view!!.tv_date_time
    }
}