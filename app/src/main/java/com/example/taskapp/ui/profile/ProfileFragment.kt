package com.example.taskapp.ui.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.taskapp.data.local.ProfilePref
import com.example.taskapp.databinding.FragmentProfileBinding
import com.example.taskapp.utils.loadImage

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var profilePref: ProfilePref




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profilePref = ProfilePref(requireContext())
        setTextToEditText()
        binding.imgProfile.loadImage(profilePref.getImageUri())
        saveImage()

    }

    private fun saveImage() {
        binding.imgProfile.setOnClickListener{
            val intentImage = Intent()
            intentImage.type = "image/*"
            intentImage.action = Intent.ACTION_GET_CONTENT
            launcher.launch(intentImage)
        }
    }

    private fun setTextToEditText() {
        binding.apply {
            etName.setText(profilePref.getNameText())
            etName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    profilePref.saveProfileNameText(s.toString())
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }


    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val photoUri = result.data?.data
                profilePref.saveImageUri(photoUri.toString())
                binding.imgProfile.loadImage(photoUri.toString())
            }
        }

}