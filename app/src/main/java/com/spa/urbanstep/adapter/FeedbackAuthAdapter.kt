package com.spa.urbanstep.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spa.urbanstep.R
import com.spa.urbanstep.model.response.FeedbackResponse
import kotlinx.android.synthetic.main.row_feedback_auth.view.*

class FeedbackAuthAdapter(val context: Context, val feedbackList: ArrayList<FeedbackResponse.AuthFeedback>) : RecyclerView.Adapter<FeedbackAuthAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView: View? = null

        itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_feedback_auth, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val feedback = feedbackList.get(position)
        holder.tv_name.setText(String.format(context.getString(R.string.project_name), feedback.authority_name))
        holder.rating.rating = feedback.rating!!.toInt().toFloat()
        holder.tv_feedback.setText(String.format(context.getString(R.string.project_feedback), feedback.feedback))
    }

    override fun getItemCount(): Int {
        return feedbackList.size
    }


    inner class ViewHolder(view: View?) : RecyclerView.ViewHolder(view) {
        val tv_name = view!!.tv_auth_name
        val rating = view!!.rating
        val tv_feedback = view!!.tv_auth_feedback
    }
}