package com.example.taskapp.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.taskapp.data.local.Pref
import com.example.taskapp.databinding.FragmentDashboardBinding
import com.example.taskapp.model.Car
import com.example.taskapp.utils.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private lateinit var db: FirebaseFirestore

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var pref: Pref
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = Pref(requireContext())
        db = FirebaseFirestore.getInstance()
        binding.btnSave.setOnClickListener {
            saveCar()
        }
    }


    private fun saveCar() {
        val name = binding.etTitle.text.toString()
        val model = binding.etDescription.text.toString()
        val car = Car(name, model)

        db.collection(FirebaseAuth.getInstance().currentUser?.uid.toString()).document().set(car)
            .addOnSuccessListener {
                showToast("Data successfully saved! ")
                binding.etTitle.text?.clear()
                binding.etDescription.text?.clear()
            }
            .addOnFailureListener {
                Log.e("ololo", "saveCar: "+ it.message)
            }
    }

}