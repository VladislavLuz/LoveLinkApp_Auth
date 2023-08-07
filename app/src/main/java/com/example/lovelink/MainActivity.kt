package com.example.lovelink

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lovelink.authorization.AuthorizationActivity
import com.example.lovelink.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        buttonAuthorizedListener()
    }


    private fun buttonAuthorizedListener(){
        binding.buttonAutorization.setOnClickListener(){
            val openAuth = Intent(this, AuthorizationActivity::class.java)
            startActivity(openAuth)
        }
    }
}