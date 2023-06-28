package com.example.lovelink

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lovelink.databinding.ActivityAutorizationBinding

class AutorizationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAutorizationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAutorizationBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_autorization)

    }
}