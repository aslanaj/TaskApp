package com.example.taskapp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.taskapp.R
import com.example.taskapp.databinding.FragmentHomeBinding
import com.example.taskapp.model.Task
import com.example.taskapp.ui.home.adapter.TaskAdapter
import com.example.taskapp.ui.task.TaskFragment.Companion.TASK_KEY
import com.example.taskapp.ui.task.TaskFragment.Companion.TASK_REQUEST

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val adapter = TaskAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentResultListener(TASK_REQUEST) { key, bundle ->
            // Здесь можно передать любой тип, поддерживаемый Bundle-ом
            val result = bundle.getSerializable(TASK_KEY) as Task
            adapter.addTask(result)
        }
        binding.apply {
            rvHome.adapter =adapter
            fab.setOnClickListener{
                findNavController().navigate(R.id.taskFragment)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}