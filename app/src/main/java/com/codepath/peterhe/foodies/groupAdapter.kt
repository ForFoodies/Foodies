package com.codepath.peterhe.foodies

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GroupAdapter(val context: Context, val groups: MutableList<Group>): RecyclerView.Adapter<GroupAdapter.ViewHolder>() {
    private lateinit var mlistner: onItemClickListner
    interface onItemClickListner {
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListner(listner: onItemClickListner) {
        mlistner = listner
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupAdapter.ViewHolder {
        //specify the layout file to use for this item
        val view = LayoutInflater.from(context).inflate(R.layout.item_group,parent,false)
        return ViewHolder(view,mlistner)
    }

    override fun onBindViewHolder(holder: GroupAdapter.ViewHolder, position: Int) {
        val group = groups.get(position)
        holder.bind(group)
    }

    override fun getItemCount(): Int {
        return groups.size
    }

    // Clean all elements of the recycler
    fun clear() {
        groups.clear()
        notifyDataSetChanged()
    }

    // Add a list of items -- change to type used
    fun addAll(postList: List<Group>) {
        groups.addAll(postList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView : View,listner: GroupAdapter.onItemClickListner): RecyclerView.ViewHolder(itemView) {
        val tvName : TextView
        val tvDescription: TextView
        val tvTime: TextView
        val tvNum : TextView
        init {
            tvName = itemView.findViewById(R.id.tv_groupName_item)
            tvDescription = itemView.findViewById(R.id.tv_groupDescription_item)
            tvTime = itemView.findViewById(R.id.tv_GroupTimeDate_item)
            tvNum = itemView.findViewById(R.id.tv_GroupNum_item)
            itemView.setOnClickListener {
                listner.onItemClick(adapterPosition)
            }
        }
        fun bind(group:Group) {
            tvDescription.text = group.getDescription()
            tvName.text = group.getName()
            val time = "${group.getTime()} ${group.getDate()}"
            tvTime.text = time
            val num = "${group.getCurrent()}/${group.getMax()}"
            tvNum.text =num
        }
    }
}