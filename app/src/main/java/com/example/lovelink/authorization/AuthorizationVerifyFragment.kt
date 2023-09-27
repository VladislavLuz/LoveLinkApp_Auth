package com.example.lovelink.authorization

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.lovelink.R
import com.example.lovelink.authorization.connection.AuthCodeRequest
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
    ): View {
        binding = FragmentAuthorizationVerifyBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.authVerifyTvPhoneNumber.text = authModel.phoneNumber
        sendCode()
    }


        fun newInstance(param1: String, param2: String) = AuthorizationVerifyFragment()

    fun sendCode(){

        binding.authVerifyButton.setOnClickListener(){
            sendCodeRequest()
        }

        binding.authVerifyButtonSendAgain.setOnClickListener(){
            sendCodeRequest()

        }
    }

    fun sendCodeRequest(){
        binding.authVerifyButton.isEnabled = false
        binding.authVerifyButtonSendAgain.isEnabled = false
            if (binding.authVerifyEtError.isVisible){
                binding.authVerifyEtError.visibility = View.GONE
                binding.apply {
                    authVerifyEt1.setTextColor(resources.getColor(R.color.black))
                    authVerifyEt2.setTextColor(resources.getColor(R.color.black))
                    authVerifyEt3.setTextColor(resources.getColor(R.color.black))
                    authVerifyEt4.setTextColor(resources.getColor(R.color.black))
                    authVerifyEt5.setTextColor(resources.getColor(R.color.black))
                    authVerifyEt6.setTextColor(resources.getColor(R.color.black))
                }
                binding.authVerifyButton.setBackgroundColor(resources.getColor(R.color.active_buttonNext_color))
            }

            binding.authVerifyButton.text = "..."

            CoroutineScope(Dispatchers.IO).launch {

                var authCode:String = binding.authVerifyEt1.text.toString() + binding.authVerifyEt2.text.toString() + binding.authVerifyEt3.text.toString() + binding.authVerifyEt4.text.toString() + binding.authVerifyEt5.text.toString() + binding.authVerifyEt6.text.toString()


                try {
                    var reqCode = authModel.sendRequest(getString(R.string.serv_ip)).sendCode(
                       AuthCodeRequest(authModel.phoneNumber, authCode)
                    )
                    requireActivity().runOnUiThread{
                        binding.authVerifyTvPhoneNumber.text = authCode
                        if(reqCode.isSuccessful && reqCode.body()?.status.toString() == "true"){
                            binding.authVerifyTvPhoneNumber.text = reqCode.body()?.access_token.toString()
                        } else {
                            binding.authVerifyEtError.visibility = View.VISIBLE
                            var textError = getString(R.string.response_error_message) + reqCode.code().toString() + " code \n" + reqCode.errorBody().toString()
                            binding.authVerifyEtError.text = textError
                            binding.apply {
                                authVerifyEt1.setTextColor(resources.getColor(R.color.error))
                                authVerifyEt2.setTextColor(resources.getColor(R.color.error))
                                authVerifyEt3.setTextColor(resources.getColor(R.color.error))
                                authVerifyEt4.setTextColor(resources.getColor(R.color.error))
                                authVerifyEt5.setTextColor(resources.getColor(R.color.error))
                                authVerifyEt6.setTextColor(resources.getColor(R.color.error))
                            }
                            binding.authVerifyButton.setBackgroundColor(resources.getColor(R.color.error_buttonNext_color))
                            binding.authVerifyButton.text = getString(R.string.autorization_button)
                        }

                    }

                }catch (e:IOException){
                    Log.d("MyLog",e.toString())
                    requireActivity().runOnUiThread {
                        binding.authVerifyEtError.visibility = View.VISIBLE
                        binding.authVerifyEtError.text = e.toString()
                        binding.authVerifyButton.setBackgroundColor(resources.getColor(R.color.error_buttonNext_color))
                        binding.authVerifyButton.text = getString(R.string.autorization_button)
                    }

                }

            }
        binding.authVerifyButton.isEnabled = true
        binding.authVerifyButtonSendAgain.isEnabled = true
    }





    }