package com.example.lovelink.authorization


import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
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

class AuthorizationFragment() : Fragment() {
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


    fun sendPhoneRequest() {

        binding.fragmentAuthButtonNext.setOnClickListener() {
            binding.fragmentAuthButtonNext.isEnabled = false
            if (binding.fragmentAuthTVError.isVisible) binding.fragmentAuthTVError.visibility =
                View.GONE
            var authModel = AuthorizationModel()
            binding.fragmentAuthEtPhone.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (authModel.checkFieldCharacters(
                            binding.fragmentAuthEtPhone.text.trim().toString(), 10
                        )
                    ) {
                        binding.fragmentAuthTVError.visibility = View.GONE
                        binding.fragmentAuthButtonNext.setBackgroundColor(resources.getColor(R.color.fragment_auth_buttton_next))
                        binding.fragmentAuthButtonNext.isEnabled = true
                    } else {
                        if (binding.fragmentAuthTVError.visibility != View.VISIBLE) {
                            binding.fragmentAuthTVError.visibility = View.VISIBLE
                            binding.fragmentAuthButtonNext.setBackgroundColor(resources.getColor(R.color.error_buttonNext_color))
                            binding.fragmentAuthButtonNext.isEnabled = false
                        }
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })

            if (authModel.checkFieldCharacters(
                    binding.fragmentAuthEtPhone.text.trim().toString(),
                    10
                )
            ) {

                binding.fragmentAuthButtonNext.text = "..."
                var tempNumber = "+7" + binding.fragmentAuthEtPhone.text.trim().toString()
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        var reqPhone = authModel.sendRequest(getString(R.string.serv_ip)).sendPhone(
                            AuthPhoneRequest(tempNumber)
                        )
                        requireActivity().runOnUiThread {

                            if (reqPhone.isSuccessful && reqPhone.body()?.status.toString() == "true") {
                                setFragmentResult(
                                    AuthConst.REQUESTPHONE_KEY,
                                    bundleOf(AuthConst.BUNDLEPHONE_KEY to tempNumber)
                                )
                                findNavController().navigate(R.id.authorizationVerifyFragment)
                            } else {
                                binding.fragmentAuthTVError.visibility = View.VISIBLE
                                var textError = getString(R.string.response_error_message) +
                                        reqPhone.code().toString() + " code \n" +
                                        reqPhone.errorBody().toString()
                                binding.fragmentAuthTVError.text = textError
                                binding.fragmentAuthButtonNext.setBackgroundColor(
                                    resources.getColor(
                                        R.color.error_buttonNext_color
                                    )
                                )
                                binding.fragmentAuthButtonNext.text = getString(R.string.next)
                            }

                        }
                    } catch (i: IOException) {
                        Log.d("MyLog", i.toString())
                        requireActivity().runOnUiThread {
                            binding.fragmentAuthTVError.visibility = View.VISIBLE
                            binding.fragmentAuthTVError.text = i.toString()
                            binding.fragmentAuthButtonNext.setBackgroundColor(resources.getColor(R.color.error_buttonNext_color))
                            binding.fragmentAuthButtonNext.text = getString(R.string.next)
                        }
                    }

                }
            } else {
                if (binding.fragmentAuthTVError.visibility != View.VISIBLE) {
                    binding.fragmentAuthTVError.visibility = View.VISIBLE
                    binding.fragmentAuthButtonNext.setBackgroundColor(resources.getColor(R.color.error_buttonNext_color))
                    binding.fragmentAuthButtonNext.text = getString(R.string.next)
                }
            }
            binding.fragmentAuthButtonNext.isEnabled = true
        }
    }


}