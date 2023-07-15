package com.example.lovelink.authorization


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        sendPhoneRequests()
    }

        fun newInstance(param1: String, param2: String) = AuthorizationFragment()


    fun sendPhoneRequests(){
        binding.fragmentAuthButtonNext.setOnClickListener(){
            binding.fragmentAuthButtonNext.text = "Hwll"

            val authRetrofitPhone = Retrofit.Builder()
                .baseUrl("http://92.51.39.4").addConverterFactory(GsonConverterFactory.create()).build()
            var authAPI = authRetrofitPhone.create(AuthorizationAPI::class.java)
            Log.d("MyLog","AuthAPIsuccess")

            CoroutineScope(Dispatchers.IO).launch {
                var tempPhone= "+7" + binding.fragmentAuthEtPhone.text.toString()
                Log.d("MyLog","Corutine")
                val reqPhone = authAPI.authSendPhone(
                    AuthPhoneRequest(tempPhone)
                )
                Log.d("MyLog","Corutine2")
                requireActivity().runOnUiThread {
                    binding.fragmentAuthButtonNext.text = tempPhone
                    if(reqPhone.isSuccessful){
                        binding.fragmentAuthTVSMSWarning.text = "Okey!!"
                        binding.fragAuthTvEnterPhone.text = reqPhone.body().toString()
                    }else{
                        binding.fragmentAuthTVSMSWarning.text = reqPhone.code().toString()
                    binding.fragAuthTvEnterPhone.text = reqPhone.errorBody().toString()
                    }
                }
            }
        }
    }


    }