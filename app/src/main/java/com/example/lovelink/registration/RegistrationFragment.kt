package com.example.lovelink.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lovelink.R
import com.example.lovelink.databinding.FragmentAuthorizationBinding
import com.example.lovelink.databinding.FragmentRegistrationBinding


class RegistrationFragment : Fragment() {
    lateinit var binding: FragmentRegistrationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistrationBinding.inflate(layoutInflater)
        return binding.root
    }

        fun newInstance(param1: String, param2: String) = RegistrationFragment()
}