package com.example.lovelink.registration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lovelink.databinding.ActivityRegistrationBinding

class RegistrationActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onClickRegButtonBack()
    }

    private fun onClickRegButtonBack(){
        binding.toolbarRegButtonBack.setOnClickListener(){
            finish()
        }
    }
}