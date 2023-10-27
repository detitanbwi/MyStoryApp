package com.example.myapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.data.UserModel
import com.example.myapplication.data.response.ListStoryItem
import com.example.myapplication.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(HomeViewModel::class.java)

        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager

        val userPreference = UserPreference(this)
        val userId = userPreference.getUser().userId
        val token = userPreference.getUser().token
        val name = userPreference.getUser().name

        mainViewModel.getStories(token.toString())

        mainViewModel.stories.observe(this){
            setStoryData(it.listStory)
        }

        binding.btnLogout.setOnClickListener {
            val sharedPrefs = UserPreference(this)
            sharedPrefs.setUser(UserModel("","",""))

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.fabAddStory.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }
    }
    private fun setStoryData(Stories: List<ListStoryItem>) {
        val adapter = StoryAdapter()
        Log.d("Titan","Recycler")
        adapter.submitList(Stories)
        binding.rvStory.adapter = adapter
    }
    companion object {
        const val STORY_ID = "story_id"
    }
}