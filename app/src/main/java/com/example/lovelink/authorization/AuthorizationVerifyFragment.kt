package com.example.lovelink.authorization

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.example.lovelink.R
import com.example.lovelink.authorization.connection.AuthCodeRequest
import com.example.lovelink.authorization.connection.AuthPhoneRequest
import com.example.lovelink.databinding.FragmentAuthorizationVerifyBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException
import java.sql.Time
import kotlin.time.DurationUnit

class AuthorizationVerifyFragment : Fragment() {
    private lateinit var binding: FragmentAuthorizationVerifyBinding
    var authModel = AuthorizationModel()
    private lateinit var phoneNumber: String


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
        setFragmentListener()
        sendCode()
    }


    fun newInstance(param1: String, param2: String) = AuthorizationVerifyFragment()

    private fun setFragmentListener() {
        setFragmentResultListener(AuthConst.REQUESTPHONE_KEY) { requestKey, bundle ->
            phoneNumber = bundle.getString(AuthConst.BUNDLEPHONE_KEY).toString()
            binding.authVerifyTvPhoneNumber.text = phoneNumber
        }
    }

    fun sendCode() {

        binding.authVerifyButton.setOnClickListener() {
            sendCodeRequest()
        }

        binding.authVerifyButtonSendAgain.setOnClickListener() {
            resendPhoneRequest()

        }
    }

    fun sendCodeRequest() {
        binding.authVerifyButton.isEnabled = false
        if (binding.authVerifyEtError.isVisible) {
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

        var authCode: String = binding.authVerifyEt1.text.toString() +
                binding.authVerifyEt2.text.toString() +
                binding.authVerifyEt3.text.toString() +
                binding.authVerifyEt4.text.toString() +
                binding.authVerifyEt5.text.toString() +
                binding.authVerifyEt6.text.toString()


        if (authModel.checkFieldCharacters(authCode,6)) {
            binding.authVerifyButton.text = "..."

            CoroutineScope(Dispatchers.IO).launch {

                try {
                    var reqCode = authModel.sendRequest(getString(R.string.serv_ip)).sendCode(
                        AuthCodeRequest(phoneNumber, authCode)
                    )
                    requireActivity().runOnUiThread {
                        binding.authVerifyTvPhoneNumber.text = authCode
                        if (reqCode.isSuccessful && reqCode.body()?.status.toString() == "true") {
                            binding.authVerifyTvPhoneNumber.text = reqCode.body()?.access_token.toString()
                        } else {
                            binding.authVerifyEtError.visibility = View.VISIBLE
                            var textError = getString(R.string.response_error_message) +
                                    reqCode.code().toString() + " code \n" +
                                    reqCode.errorBody()?.string()
                                        ?.let { JSONObject(it).getString("detail") }
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

                } catch (e: IOException) {
                    Log.d("MyLog", e.toString())
                    requireActivity().runOnUiThread {
                        binding.authVerifyEtError.visibility = View.VISIBLE
                        binding.authVerifyEtError.text = e.toString()
                        binding.authVerifyButton.setBackgroundColor(resources.getColor(R.color.error_buttonNext_color))
                        binding.authVerifyButton.text = getString(R.string.autorization_button)
                    }
                }
            }
        }else {
            if (binding.authVerifyEtError.visibility != View.VISIBLE) {
                binding.authVerifyEtError.visibility = View.VISIBLE
            }
            binding.authVerifyButton.setBackgroundColor(resources.getColor(R.color.error_buttonNext_color))
            binding.authVerifyButton.text = getString(R.string.autorization_button)
            binding.authVerifyEtError.text = getString(R.string.response_error_message)
        }
        binding.authVerifyButton.isEnabled = true
    }

    fun resendPhoneRequest() {
        binding.authVerifyButtonSendAgain.isEnabled = false
        CoroutineScope(Dispatchers.IO).launch {
            try {
                var reqPhone = authModel.sendRequest(getString(R.string.serv_ip)).sendPhone(
                    AuthPhoneRequest(phoneNumber)
                )
                requireActivity().runOnUiThread {

                    if (reqPhone.isSuccessful && reqPhone.body()?.status.toString() == "true") {
                        binding.authVerifyButtonSendAgain.setTextColor(resources.getColor(R.color.black))
                        startSmsResponseDelayTimer(60000)
                    } else {
                        binding.authVerifyEtError.visibility = View.VISIBLE
                        var textError = getString(R.string.response_error_message) +
                                reqPhone.code().toString() + " code \n" +
                                reqPhone.errorBody()?.string()
                                    ?.let { JSONObject(it).getString("detail") }
                        binding.authVerifyEtError.text = textError
                        binding.authVerifyButton.setBackgroundColor(
                            resources.getColor(
                                R.color.error_buttonNext_color
                            )
                        )
                    }

                }
            } catch (i: IOException) {
                Log.d("MyLog", i.toString())
                requireActivity().runOnUiThread {
                    binding.authVerifyEtError.visibility = View.VISIBLE
                    binding.authVerifyEtError.text = i.toString()
                    binding.authVerifyButton.setBackgroundColor(resources.getColor(R.color.error_buttonNext_color))
                    binding.authVerifyButton.text = getString(R.string.autorization_button)
                }
            }

        }
    }

    fun startSmsResponseDelayTimer(necessaryTime:Long){
        object:CountDownTimer(necessaryTime,1000){
            override fun onTick(TimeM: Long) {
                binding.authVerifyButtonSendAgain.text = getString(R.string.send_code_number_again_success) + " " + (TimeM/1000).toString() + getString(R.string.second_abbreviated)
            }

            override fun onFinish() {
                binding.authVerifyButtonSendAgain.isEnabled = true
                binding.authVerifyButtonSendAgain.text = getString(R.string.send_code_number_again)
                binding.authVerifyButtonSendAgain.setTextColor(resources.getColor(R.color.send_code_again))
            }

        }.start()
    }


}