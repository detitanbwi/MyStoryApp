package com.example.myapplication

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.data.response.ListStoryItem
import com.example.myapplication.databinding.ItemStoryBinding

class StoryAdapter : ListAdapter<ListStoryItem, StoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    class MyViewHolder(val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(preview: ListStoryItem){
            binding.tvSender.text = preview.name
            Glide.with(binding.root)
                .load(preview.photoUrl)
                .into(binding.ivStoryItem)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val story = getItem(position)
        holder.binding.cvStory.setOnClickListener {
            //val moveIntent = Intent(holder.binding.root.context,HomeActivity::class.java)
            //moveIntent.putExtra(HomeActivity.STORY_ID,story.id)
            Toast.makeText(holder.binding.root.context,story.id,Toast.LENGTH_SHORT).show()
            //holder.binding.root.context.startActivity(moveIntent)
        }
        holder.bind(story)
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}

