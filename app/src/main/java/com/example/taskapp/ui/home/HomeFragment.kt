package com.example.taskapp.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.taskapp.App
import com.example.taskapp.R
import com.example.taskapp.databinding.FragmentHomeBinding
import com.example.taskapp.model.Task
import com.example.taskapp.ui.home.adapter.TaskAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val adapter = TaskAdapter(this::onLongClick)

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
        dbSave()
        binding.apply {
            rvHome.adapter = adapter
            fab.setOnClickListener {
                findNavController().navigate(R.id.taskFragment)
            }
        }
    }

    private fun onLongClick(task: Task) {
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle("Delete task")
        alertDialog.setMessage("Do you really want to delete this task?")
        alertDialog.setNegativeButton("NO") { dialog, p1 -> dialog?.cancel() }
        alertDialog.setPositiveButton("YES") { dialog, p1 ->
            App.db.taskDao().delete(task)
            dbSave()
        }
        alertDialog.create().show()
    }

    private fun dbSave() {
        val data = App.db.taskDao().getAll()
        adapter.addTasks(data)
    }
}
