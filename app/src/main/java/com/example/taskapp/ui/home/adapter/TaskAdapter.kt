package com.example.taskapp.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.taskapp.R
import com.example.taskapp.databinding.ItemTaskBinding
import com.example.taskapp.model.Task

class TaskAdapter(
    private val onLongClick: (Task) -> Unit,
    private val context: Context
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    private val data: ArrayList<Task> = arrayListOf()
    private lateinit var adapter: TaskAdapter

    fun addTask(task: Task) {
        data.add(0, task)
        notifyItemChanged(0)
    }

    fun addTasks(task: List<Task>) {
        data.clear()
        data.addAll(task)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            ItemTaskBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(data[position])
//        if (position % 2 == 0) {
//            holder.itemTaskLayout.setBackgroundResource(R.drawable.bg_item)
//        } else {
//            holder.itemTaskLayout.setBackgroundResource(R.drawable.bg_item_orange)
//        }
    }

    inner class TaskViewHolder(private val binding: ItemTaskBinding) : ViewHolder(binding.root) {
        val itemTaskLayout = binding.itemTaskLayout
        fun bind(task: Task) {
            if (adapterPosition % 2 == 0){
                binding.apply {
                    itemTaskLayout.setBackgroundResource(R.drawable.bg_item)
                    tvTitle.setTextColor(ContextCompat.getColor(context,R.color.black))
                    tvDesc.setTextColor(ContextCompat.getColor(context, R.color.black))
                }
            }else binding.apply {
                itemTaskLayout.setBackgroundResource(R.drawable.bg_item_orange)
                tvTitle.setTextColor(ContextCompat.getColor(context,R.color.white))
                tvDesc.setTextColor(ContextCompat.getColor(context, R.color.white))
            }


            binding.apply {
                tvTitle.text = task.title
                tvDesc.text = task.desc
                itemView.setOnLongClickListener {
                    onLongClick(task)
                    false
                }

            }
        }
    }
}

