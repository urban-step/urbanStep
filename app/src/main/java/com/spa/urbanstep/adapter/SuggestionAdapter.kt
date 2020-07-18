package com.spa.urbanstep.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spa.urbanstep.R
import com.spa.urbanstep.model.Suggestion

class SuggestionAdapter(val context: Context, val suggestions: ArrayList<Suggestion>) : RecyclerView.Adapter<SuggestionAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView: View? = null

        itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_item_suggestion, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return suggestions.size
    }


    inner class ViewHolder(view: View?) : RecyclerView.ViewHolder(view) {
    }
}