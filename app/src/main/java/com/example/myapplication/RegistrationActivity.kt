package com.example.myapplication

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.databinding.ActivityRegistrationBinding

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var viewModel: RegistrationViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val progressDialog = ProgressDialog(this)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[RegistrationViewModel::class.java]

        with(binding){
            btnRegister.setOnClickListener{
                if (edRegisterEmail.text.toString().isNotEmpty() and edRegisterName.text.toString().isNotEmpty() and edRegisterPassword.text.toString().isNotEmpty()) {
                    if(isEmailValid(edRegisterEmail.text.toString())){
                        progressDialog.setMessage("Loading...")
                        progressDialog.setCancelable(false)
                        progressDialog.show()
                        viewModel.postRegister(edRegisterEmail.text.toString(),edRegisterName.text.toString(),edRegisterPassword.text.toString())
                    }else{
                        edRegisterEmail.error = "Email tidak benar"
                        edRegisterEmail.requestFocus()
                    }
                }else{
                    Toast.makeText(this@RegistrationActivity,"Semua kolom harus diisi", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.registrationInfo.observe(this) {
            Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
            with(binding){
                edRegisterName.text?.clear()
                edRegisterEmail.text?.clear()
                edRegisterPassword.text?.clear()
                progressDialog.dismiss()
            }
        }
    }

    private fun isEmailValid(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

}