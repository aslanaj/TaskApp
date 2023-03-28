package com.example.taskapp.ui.onBoard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.taskapp.databinding.ItemOnboardingBinding
import com.example.taskapp.model.OnBoard
import com.example.taskapp.utils.loadImage

class OnBoardingAdapter(
    private val onClick: () -> Unit,
    private val onClickNext: () -> Unit
) :
    Adapter<OnBoardingAdapter.OnBoardingViewHolder>() {

    private val data = arrayListOf(
        OnBoard(
            "https://static.wixstatic.com/media/075ada_f52be7ec30464484add5b9fb9e849649~mv2.png/v1/fit/w_2500,h_1330,al_c/075ada_f52be7ec30464484add5b9fb9e849649~mv2.png",
            "Task Manager 1",
            "Lorem ipsum dolor sit amet, alii iuvaret theophrastus no per, mea affert discere minimum ne. "
        ),
        OnBoard(
            "https://flow-e.com/wp-content/uploads/bfi_thumb/Google-task-list-379tmv50jkyo35v5zqpoui.png",
            "Task Manager 2",
            "Lorem ipsum dolor sit amet, alii iuvaret theophrastus no per, mea affert discere minimum ne. "
        ),
        OnBoard(
            "https://kissflow.com/hubfs/task-management.jpg",
            "Task Manager 3",
            "Lorem ipsum dolor sit amet, alii iuvaret theophrastus no per, mea affert discere minimum ne. "
        )

    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingViewHolder {
        return OnBoardingViewHolder(
            ItemOnboardingBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: OnBoardingViewHolder, position: Int) {
        holder.bind(data[position])

    }

    override fun getItemCount(): Int = data.size

    inner class OnBoardingViewHolder(private val binding: ItemOnboardingBinding) :
        ViewHolder(binding.root) {
        fun bind(onBoard: OnBoard) {
            binding.apply {
                tvTitle.text = onBoard.title
                tvDesc.text = onBoard.description
                ivBoard.loadImage(onBoard.image)
                tvSkip.isVisible = adapterPosition != 2
                btnStart.isVisible = adapterPosition == 2
                tvNext.isVisible = adapterPosition != 2
                tvSkip.setOnClickListener {
                    onClick()
                }
                btnStart.setOnClickListener {
                    onClick()
                }

                tvNext.setOnClickListener {
                    onClickNext()
                }
            }

        }
    }

}