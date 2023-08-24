package com.example.lovelink.authorization


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.lovelink.MainActivity
import com.example.lovelink.R
import com.example.lovelink.authorization.connection.AuthPhoneRequest
import com.example.lovelink.databinding.FragmentAuthorizationBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class AuthorizationFragment(): Fragment() {
    private lateinit var binding: FragmentAuthorizationBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentAuthorizationBinding.inflate(layoutInflater)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sendPhoneRequest()
    }

        fun newInstance(param1: String, param2: String) = AuthorizationFragment()


    fun sendPhoneRequest(){

        binding.fragmentAuthButtonNext.setOnClickListener(){
            if (binding.fragmentAuthTVError.isVisible) binding.fragmentAuthTVError.visibility = View.GONE

            binding.fragmentAuthButtonNext.text = "..."

            CoroutineScope(Dispatchers.IO).launch {
                var tempNumber= "+7" + binding.fragmentAuthEtPhone.text.trim().toString()

                var authModel = AuthorizationModel()
                authModel.phoneNumber = tempNumber
                try {
                    var reqPhone = authModel.sendRequest(getString(R.string.serv_ip)).sendPhone(
                        AuthPhoneRequest(tempNumber)
                    )
                    requireActivity().runOnUiThread {

                            binding.fragmentAuthButtonNext.text = tempNumber

                            if (reqPhone.isSuccessful && reqPhone.body()?.status.toString() == "true") {

                                binding.fragmentAuthTVSMSWarning.text = "Okey!!"

                                findNavController().navigate(R.id.authorizationVerifyFragment)
                            } else {
                                binding.fragmentAuthTVError.visibility = View.VISIBLE
                                var textError = getString(R.string.response_error_message) + reqPhone.code().toString() + " code \n" + reqPhone.errorBody().toString()
                                binding.fragmentAuthTVError.text = textError
                            }

                    }
                }catch (i:IOException){
                    Log.d("MyLog",i.toString())
                    requireActivity().runOnUiThread {
                        binding.fragmentAuthTVError.visibility = View.VISIBLE
                        binding.fragmentAuthTVError.text = i.toString()
                    }
                }

            }
        }
    }


    }