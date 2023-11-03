package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.data.UserModel
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

        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager

        val userPreference = UserPreference(this)
        token = userPreference.getUser().token.toString()

        binding.swipeRefresh.setOnRefreshListener {
            getData()
            binding.swipeRefresh.isRefreshing = false
        }

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
        storyPagingViewModel.story.observe(this) {
            adapter.submitData(lifecycle, it)
            Toast.makeText(this,"data diperbarui", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val STORY_ID = "story_id"
        const val REQUEST_CODE = 1
    }
}