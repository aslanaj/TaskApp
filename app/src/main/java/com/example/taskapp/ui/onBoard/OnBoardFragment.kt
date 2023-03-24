package com.example.taskapp.ui.onBoard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.taskapp.R
import com.example.taskapp.databinding.FragmentOnBoardBinding
import com.example.taskapp.ui.onBoard.adapter.OnBoardingAdapter
import me.relex.circleindicator.CircleIndicator3


class OnBoardFragment : Fragment() {
 private lateinit var binding: FragmentOnBoardBinding
 private val adapter= OnBoardingAdapter(this::onStartClick, this::onNextClick)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnBoardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPager.adapter = adapter
        binding.indicator.setViewPager(binding.viewPager)
        adapter.registerAdapterDataObserver(binding.indicator.adapterDataObserver)

    }
    private fun onStartClick(){
        findNavController().navigateUp()
    }
    private fun onNextClick(){
        binding.viewPager.currentItem+=1
    }


}