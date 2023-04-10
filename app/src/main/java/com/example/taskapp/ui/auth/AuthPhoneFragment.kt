package com.example.taskapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.taskapp.MainActivity
import com.example.taskapp.R
import com.example.taskapp.databinding.FragmentAuthPhoneBinding
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class AuthPhoneFragment : Fragment() {
    private lateinit var binding: FragmentAuthPhoneBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var number: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthPhoneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        listener()
    }
    private fun listener() {
        binding.btnSendOtp.setOnClickListener {
            number = binding.etPhoneNumber.text.trim().toString()
            if (number.isNotEmpty()) {
                if (number.length == 9) {
                    number = "+996$number"
                    binding.proBar1.visibility = View.VISIBLE
                    val options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(number)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(requireActivity())                 // Activity (for callback binding)
                        .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                        .build()
                    PhoneAuthProvider.verifyPhoneNumber(options)
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.toast_et_number),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.et_enter_number),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener /*(this)*/ { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(requireContext(), getString(R.string.toas_auth_success), Toast.LENGTH_SHORT).show()
                    sendToMAin()
                } else {
                    // Sign in failed, display a message and update the UI
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Log.e("aslan", "signInWithPhoneAuthCredential: ${task.exception.toString()}" )
                    }
                    // Update UI
                }
            }
    }
    private fun sendToMAin(){
        startActivity(Intent(requireContext(), MainActivity::class.java))
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
            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                Log.w("aslan", "onVerificationFailed: ${e.toString()}")
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                Log.w("aslan", "onVerificationFailed: ${e.toString()}")
            }
//                        else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
//                // reCAPTCHA verification attempted with null Activity
//            }
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            val bundle = Bundle()
            bundle.putString("OTP", verificationId)
            bundle.putParcelable("resendToken", token)
            bundle.putString("phoneNumber", number)

            val otpFragment = OTPFragment()
            otpFragment.arguments = bundle

            binding.proBar1.visibility = View.GONE

            // Navigate to the OTPFragment using a FragmentTransaction
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, otpFragment)
            transaction.addToBackStack(null)
            transaction.commit()

            Log.e("aslan", "onCodeSent: ${verificationId.toString()}" )        }
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser !=null){
            startActivity(Intent(requireContext(), MainActivity::class.java))
        }
    }
}