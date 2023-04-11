package com.example.taskapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.taskapp.MainActivity
import com.example.taskapp.R
import com.example.taskapp.databinding.FragmentOTPBinding
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class OTPFragment : androidx.fragment.app.Fragment() {

    private lateinit var binding: FragmentOTPBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var phoneNumber: String
    private lateinit var OTP: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOTPBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        val bundle = arguments
        if (bundle != null && bundle.containsKey("OTP")
            && bundle.containsKey("resendToken")
            && bundle.containsKey("phoneNumber")
        ) {
            OTP = bundle.getString("OTP").toString()
            resendToken = bundle.getParcelable("resendToken")!!
            phoneNumber = bundle.getString("phoneNumber").toString()
        } else {
            // handle missing arguments
        }

        addTextChangeListener()
        resendOTPVisibility()
        listener()
    }

    private fun listener() {
        binding.apply {
            btnSendOtp.setOnClickListener {
                val typeOTP = (etInputCode1.text.toString()
                        + etInputCode2.text.toString()
                        + etInputCode3.text.toString()
                        + etInputCode4.text.toString()
                        + etInputCode5.text.toString()
                        + etInputCode6.text.toString())
                if (typeOTP.isNotEmpty()) {
                    if (typeOTP.length == 6) {
                        val credential: PhoneAuthCredential =
                            PhoneAuthProvider.getCredential(OTP, typeOTP)
                        proBar2.visibility = View.VISIBLE
                        signInWithPhoneAuthCredential(credential)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.toast_enter_correct_code),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.toast_enter_code),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            tvSendCodeAgain.setOnClickListener {
                resendVerificationCode()
                resendOTPVisibility()
            }
        }
    }

    private fun resendOTPVisibility() {
        binding.apply {
            etInputCode1.setText("")
            etInputCode2.setText("")
            etInputCode3.setText("")
            etInputCode4.setText("")
            etInputCode5.setText("")
            etInputCode6.setText("")
            tvSendCodeAgain.visibility = View.GONE
        }
        Handler(Looper.myLooper()!!).postDelayed(Runnable {
            binding.apply {
                tvSendCodeAgain.visibility = View.GONE
                tvSendCodeAgain.isEnabled = true
            }
        }, 60000)
    }

    private fun resendVerificationCode() {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity())                 // Activity (for callback binding)
            .setCallbacks(callbacks)
            .setForceResendingToken(resendToken)         // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                Log.w("aslan", "onVerificationFailed: ${e.toString()}")
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                Log.w("aslan", "onVerificationFailed: ${e.toString()}")
            }
            //            else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
//                // reCAPTCHA verification attempted with null Activity
//            }

            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            OTP = verificationId
            resendToken = token
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener /*(this)*/ { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    binding.proBar2.visibility = View.VISIBLE
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.toas_auth_success),
                        Toast.LENGTH_SHORT
                    ).show()
                    sendToMAin()
                } else {
                    // Sign in failed, display a message and update the UI
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Log.e(
                            "aslan",
                            "signInWithPhoneAuthCredential: ${task.exception.toString()}"
                        )
                    }
                    // Update UI
                }
            }
    }

    private fun sendToMAin() {
        startActivity(Intent(requireContext(), MainActivity::class.java))
    }

    private fun addTextChangeListener() {
        binding.apply {
            etInputCode1.addTextChangedListener(EditTextWatcher(etInputCode1))
            etInputCode2.addTextChangedListener(EditTextWatcher(etInputCode2))
            etInputCode3.addTextChangedListener(EditTextWatcher(etInputCode3))
            etInputCode4.addTextChangedListener(EditTextWatcher(etInputCode4))
            etInputCode5.addTextChangedListener(EditTextWatcher(etInputCode5))
            etInputCode6.addTextChangedListener(EditTextWatcher(etInputCode6))
        }
    }

    inner class EditTextWatcher(private val view: View) : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(p0: Editable?) {
            val text = p0.toString()
            when (view.id) {
                R.id.et_input_code1 -> if (text.length == 1) binding.etInputCode2.requestFocus()
                R.id.et_input_code2 -> if (text.length == 1) binding.etInputCode3.requestFocus() else if (text.isEmpty()) binding.etInputCode1.requestFocus()
                R.id.et_input_code3 -> if (text.length == 1) binding.etInputCode4.requestFocus() else if (text.isEmpty()) binding.etInputCode2.requestFocus()
                R.id.et_input_code4 -> if (text.length == 1) binding.etInputCode5.requestFocus() else if (text.isEmpty()) binding.etInputCode3.requestFocus()
                R.id.et_input_code5 -> if (text.length == 1) binding.etInputCode6.requestFocus() else if (text.isEmpty()) binding.etInputCode4.requestFocus()
                R.id.et_input_code6 -> if (text.isEmpty())   binding.etInputCode5.requestFocus()
            }
        }
    }
}