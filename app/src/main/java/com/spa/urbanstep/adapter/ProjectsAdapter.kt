package com.spa.urbanstep.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spa.urbanstep.R
import com.spa.urbanstep.model.Project
import kotlinx.android.synthetic.main.row_item_projects.view.*

class ProjectsAdapter(val context: Context, val projects: ArrayList<Project>) : RecyclerView.Adapter<ProjectsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView: View? = null

        itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_item_projects, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val project = projects.get(position)
        holder.projectName.setText(project.name)
        holder.projectDesc.setText(project.description)
    }

    override fun getItemCount(): Int {
        return projects.size
    }


    inner class ViewHolder(view: View?) : RecyclerView.ViewHolder(view) {
        val projectName = view!!.tv_project_name
        val projectDesc = view!!.tv_project_detail
    }
}