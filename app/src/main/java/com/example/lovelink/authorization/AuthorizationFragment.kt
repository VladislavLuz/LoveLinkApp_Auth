package com.example.lovelink.authorization


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lovelink.R
import com.example.lovelink.authorization.connection.AuthPhoneRequest
import com.example.lovelink.authorization.connection.AuthorizationAPI
import com.example.lovelink.databinding.FragmentAuthorizationBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthorizationFragment : Fragment() {
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

            binding.fragmentAuthButtonNext.text = "..."

            CoroutineScope(Dispatchers.IO).launch {
                var tempPhone= "+7" + binding.fragmentAuthEtPhone.text.trim().toString()

                var authModel = AuthorizationModel()
                val reqPhone = authModel.sendRequest(getString(R.string.serv_ip)).sendPhone(
                    AuthPhoneRequest(tempPhone)
                )

                requireActivity().runOnUiThread {
                    binding.fragmentAuthButtonNext.text = tempPhone
                    if(reqPhone.isSuccessful && reqPhone.body()?.status.toString() == "true"){
                        binding.fragmentAuthTVSMSWarning.text = "Okey!!"
                    }else{
                        binding.fragmentAuthTVError.visibility = View.VISIBLE
                        var textError = getString(R.string.response_error_message) + reqPhone.code().toString() + " code \n" + reqPhone.errorBody().toString()
                        binding.fragmentAuthTVError.text =  textError
                    }
                }
            }
        }
    }


    }