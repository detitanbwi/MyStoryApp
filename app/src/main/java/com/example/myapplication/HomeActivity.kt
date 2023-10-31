package com.example.myapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.data.UserModel
import com.example.myapplication.data.response.ListStoryItem
import com.example.myapplication.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var token: String
    private val storyPagingViewModel: StoryPagingViewModel by viewModels {
        PagingViewModelFactory(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(HomeViewModel::class.java)

        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager

        val userPreference = UserPreference(this)
        token = userPreference.getUser().token.toString()

//        mainViewModel.getStories(token)

//        mainViewModel.stories.observe(this){
//            setStoryData(it.listStory)
//        }

        binding.cvStoryMaps.setOnClickListener {
            val intent = Intent(this, StoryMapsActivity::class.java)
            startActivity(intent)
        }
        binding.fabLogout.setOnClickListener {
            val sharedPrefs = UserPreference(this)
            sharedPrefs.setUser(UserModel("","",""))

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.fabAddStory.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE)
        }

        getData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE) {
            val mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(HomeViewModel::class.java)
            mainViewModel.getStories(token)
        }
    }
    private fun getData() {
        val adapter = StoryPagingAdapter()
        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        storyPagingViewModel.story.observe(this, {
            adapter.submitData(lifecycle, it)
            Log.d("OK",it.toString())
        })
    }

    private fun setStoryData(Stories: List<ListStoryItem>) {
        val adapter = StoryAdapter()
        Log.d("Titan","Recycler")
        adapter.submitList(Stories)
        binding.rvStory.adapter = adapter
    }
    companion object {
        const val STORY_ID = "story_id"
        const val REQUEST_CODE = 1
    }
}