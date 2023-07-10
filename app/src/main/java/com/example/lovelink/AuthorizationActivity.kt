package com.example.lovelink

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.example.lovelink.databinding.ActivityAutorizationBinding

class AuthorizationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAutorizationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAutorizationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onClickAuthBack()

        /*val toolbar = findViewById<Toolbar>(R.id.toolbarAuth)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
           // setDisplayHomeAsUpEnabled(true)
           // setHomeAsUpIndicator(R.drawable.ic_back_toolbar)
        }*/

        supportFragmentManager.beginTransaction().replace(R.id.fragment_auth_first,AuthorizationFragment()).commit()
    }


    fun onClickAuthBack(){
        binding.toolbarAuthButtonBack.setOnClickListener(){
            finish()
        }
    }

}