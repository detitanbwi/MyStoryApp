package com.example.myapplication

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.UserModel
import com.example.myapplication.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private val looping:Boolean = true

    companion object {
        const val EXTRA_TYPE_FORM = "extra_type_form"
        const val EXTRA_RESULT = "extra_result"
        const val RESULT_CODE = 101
        const val TYPE_ADD = 1
        const val TYPE_EDIT = 2
        private const val FIELD_REQUIRED = "Field tidak boleh kosong"
        private const val FIELD_DIGIT_ONLY = "Hanya boleh terisi numerik"
        private const val FIELD_IS_NOT_VALID = "Email tidak valid"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userPreference = UserPreference(this)

        val progressDialog = ProgressDialog(this)


        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[MainViewModel::class.java]

        val userId = userPreference.getUser().userId
        val token = userPreference.getUser().token
        val name = userPreference.getUser().name

        if (userId != "" && token != "" && name != "") {
            // Data exists in UserPreference
            Toast.makeText(this, "User data exists. UserId: $userId, Token: $token", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@MainActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.tvRegistrasi.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }

        with(binding){
            btnLogin.setOnClickListener{
                progressDialog.setMessage("Loading...")
                progressDialog.setCancelable(false)
                progressDialog.show()
                if (edLoginEmail.text.toString().isNotEmpty() and edLoginPassword.text.toString().isNotEmpty()) {
                    viewModel.postLogin(edLoginEmail.text.toString(),edLoginPassword.text.toString())
                }else{
                    Toast.makeText(this@MainActivity,"Semua kolom harus diisi", Toast.LENGTH_SHORT).show()
                }
            }
        }


        viewModel.loginInfo.observe(this) {
            Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
            progressDialog.dismiss()
            if (!it.error) {
                with(binding){
                    edLoginEmail.text?.clear()
                    edLoginPassword.text?.clear()
                    saveUser(it.loginResult.name, it.loginResult.token, it.loginResult.userId )
                    val intent = Intent(this@MainActivity, HomeActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        val motionLayout = binding.motionLayout
        motionLayout.transitionToState(R.id.end)
        motionLayout.setTransitionListener(object : MotionLayout.TransitionListener{
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {

            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {

            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                if(looping){
                    motionLayout?.transitionToState(R.id.end)
                }
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {

            }
        })
    }
    private fun saveUser(name: String, token: String, userId: String) {
        val userPreference = UserPreference(this)
        val userData = UserModel()
        userData.name = name
        userData.token = token
        userData.userId = userId
        userPreference.setUser(userData)
    }


}