package com.example.lovelink.authorization

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lovelink.R
import com.example.lovelink.databinding.FragmentAuthorizationVerifyBinding

class AuthorizationVerifyFragment : Fragment() {
    private lateinit var binding: FragmentAuthorizationVerifyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthorizationVerifyBinding.inflate(layoutInflater)
        return binding.root
    }


        fun newInstance(param1: String, param2: String) = AuthorizationVerifyFragment()



    }