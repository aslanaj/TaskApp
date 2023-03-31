package com.example.taskapp.ui.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.example.taskapp.App
import com.example.taskapp.databinding.FragmentTaskBinding
import com.example.taskapp.model.Task

class TaskFragment : Fragment() {
    private lateinit var binding: FragmentTaskBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTaskBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSave.setOnClickListener{
            if(binding.etTitle.text.isNotEmpty()){
                save()
                Toast.makeText(requireContext(),"Successfully Saved", Toast.LENGTH_SHORT).show()
            } else binding.etTitle.error = "Это поле обязательна для заполнения"
            Toast.makeText(requireContext(),"Это поле обязательна для заполнения", Toast.LENGTH_SHORT).show()
        }
    }
    private fun save(){
        val data= Task(title = binding.etTitle.text.toString(),
            desc = binding.etDescription.text.toString())
        setFragmentResult(TASK_REQUEST, bundleOf(TASK_KEY to data))
        App.db.taskDao().insert(data)
        findNavController().navigateUp()
    }
    companion object {
        const val TASK_REQUEST = "task"
        const val TASK_KEY = "key"
    }

}