package com.example.myapplication

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ActivityStoryDetailBinding

class StoryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryDetailBinding
    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val storyDetailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(StoryDetailViewModel::class.java)
        val storyId = intent.getStringExtra(HomeActivity.STORY_ID)
        val userPreference = UserPreference(this)
        val token = userPreference.getUser().token.toString()
        if (storyId != null) {
            storyDetailViewModel.getStoryById(storyId,"Bearer $token")
        }

        storyDetailViewModel.storyDetail.observe(this){
            with(binding){
                tvDetailName.text = it.story.name
                tvDetailDescription.text = it.story.description
                Glide.with(root)
                    .load(it.story.photoUrl)
                    .into(ivDetailPhoto)
            }
        }
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading data...")
        progressDialog.setCancelable(false)
        storyDetailViewModel.isLoading.observe(this) {
            Log.d("Loading?",it.toString())
            showLoading(it)
        }
    }
    private fun showLoading(isLoading: Boolean) {

        if (isLoading){
            progressDialog.show()
        } else{
            progressDialog.dismiss()
            Log.d("ok",isLoading.toString())
        }


    }
}