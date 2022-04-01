package com.hacybeyker.allset.view.tablayout.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hacybeyker.allset.BaseFragment
import com.hacybeyker.allset.databinding.FragmentTabLayoutAccountBinding

class TabLayoutAccountFragment : BaseFragment() {

    private var _binding: FragmentTabLayoutAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTabLayoutAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupView() {

    }

    override fun destroyView() {
        _binding = null
    }
}