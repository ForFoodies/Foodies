package com.codepath.peterhe.foodies

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.parse.ParseFile
import com.parse.ParseUser

class MemberAdapter(val context: Context, val members: MutableList<ParseUser>): RecyclerView.Adapter<MemberAdapter.ViewHolder>() {
    private lateinit var mlistner: onItemClickListner
    interface onItemClickListner {
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListner(listner: onItemClickListner) {
        mlistner = listner
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberAdapter.ViewHolder {
        //specify the layout file to use for this item
        val view = LayoutInflater.from(context).inflate(R.layout.item_member,parent,false)
        return ViewHolder(view,mlistner)
    }

    override fun onBindViewHolder(holder: MemberAdapter.ViewHolder, position: Int) {
        val member = members.get(position)
        holder.bind(member)
    }

    override fun getItemCount(): Int {
        return members.size
    }

    // Clean all elements of the recycler
    fun clear() {
        members.clear()
        notifyDataSetChanged()
    }

    // Add a list of items -- change to type used
    fun addAll(memberList: List<ParseUser>) {
        members.addAll(memberList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView : View,listner: MemberAdapter.onItemClickListner): RecyclerView.ViewHolder(itemView) {
        val tvName : TextView
        val tvDescription: TextView
        val tvTime: TextView
        init {
            tvName = itemView.findViewById(R.id.tv_MemberName_item)
            tvDescription = itemView.findViewById(R.id.tv_MemberDescription_item)
            tvTime = itemView.findViewById(R.id.tv_MemberCreatedTime_item)
            itemView.setOnClickListener {
                listner.onItemClick(adapterPosition)
            }
        }
        fun bind(user: ParseUser) {
            val image:ParseFile? = user.getParseFile("profile")
            Glide.with(context).load(image?.url).apply(
                RequestOptions().transforms(
                CenterCrop(), RoundedCorners(10)
            )).into(itemView.findViewById<ImageView>(R.id.iv_MemberProfile_item))
            tvDescription.text = user.get("description").toString()
            tvName.text = user.getUsername()
            val time = "${user.getCreatedAt()}".substring(4,10)
            tvTime.text = time
            //TODO add profile image
        }
    }
}