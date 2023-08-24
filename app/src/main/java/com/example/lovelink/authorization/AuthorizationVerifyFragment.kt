package com.example.lovelink.authorization

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.lovelink.R
import com.example.lovelink.authorization.connection.AuthCodeRequest
import com.example.lovelink.authorization.connection.AuthPhoneRequest
import com.example.lovelink.databinding.FragmentAuthorizationVerifyBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class AuthorizationVerifyFragment : Fragment() {
    private lateinit var binding: FragmentAuthorizationVerifyBinding
    var authModel = AuthorizationModel()


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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.authVerifyTvPhoneNumber.text = authModel.phoneNumber
        sendCodeRequest()
    }


        fun newInstance(param1: String, param2: String) = AuthorizationVerifyFragment()

    fun sendCodeRequest(){
        binding.authVerifyButton.setOnClickListener(){
            if (binding.authVerifyEtError.isVisible) binding.authVerifyEtError.visibility = View.GONE

            binding.authVerifyButton.text = "..."

            CoroutineScope(Dispatchers.IO).launch {

                var authCode:String = binding.authVerifyEt1.text.toString() + binding.authVerifyEt2.text.toString() + binding.authVerifyEt3.text.toString() + binding.authVerifyEt4.text.toString() + binding.authVerifyEt5.text.toString() + binding.authVerifyEt6.text.toString()


                try {
                    var reqCode = authModel.sendRequest(getString(R.string.serv_ip)).sendCode(
                       AuthCodeRequest(authModel.phoneNumber, authCode)
                    )
                    requireActivity().runOnUiThread{
                    binding.authVerifyTvPhoneNumber.text = authCode
                        if (reqCode.isSuccessful && reqCode.body()?.status.toString() == "true") {
                            binding.authVerifyTvPhoneNumber.text = reqCode.body()?.access_token.toString()
                        } else {
                            binding.authVerifyEtError.visibility = View.VISIBLE
                            var textError = getString(R.string.response_error_message) + reqCode.code().toString() + " code \n" + reqCode.errorBody().toString()
                            binding.authVerifyEtError.text = textError
                        }

                    }

                }catch (e:IOException){
                    Log.d("MyLog",e.toString())
                    requireActivity().runOnUiThread {
                        binding.authVerifyEtError.visibility = View.VISIBLE
                        binding.authVerifyEtError.text = e.toString()
                    }

                }

            }


        }
    }



    }