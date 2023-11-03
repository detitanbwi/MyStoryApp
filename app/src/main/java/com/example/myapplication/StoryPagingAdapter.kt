package com.example.myapplication
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.data.response.ListStoryItem
import com.example.myapplication.databinding.ItemStoryBinding

class StoryPagingAdapter  :
    PagingDataAdapter<ListStoryItem, StoryPagingAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.binding.cvStory.setOnClickListener {
                val context = holder.binding.root.context
                val storyId = data.id

                val moveIntent = Intent(context, StoryDetailActivity::class.java)
                moveIntent.putExtra(HomeActivity.STORY_ID, storyId)
                context.startActivity(moveIntent)
            }
            holder.bind(data)
        }else{
            Log.d("OK","Data tidak ada")
        }
    }

    class MyViewHolder(val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ListStoryItem) {
            binding.tvItemName.text = data.name
            Glide.with(binding.root)
                .load(data.photoUrl)
                .into(binding.ivItemPhoto)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}