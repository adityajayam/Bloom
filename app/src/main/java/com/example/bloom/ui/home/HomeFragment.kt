package com.example.bloom.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.bloom.adapter.CartListAdapter
import com.example.bloom.adapter.ThemeListAdapter
import com.example.bloom.databinding.FragmentHomeBinding
import com.example.bloom.repository.BloomRepository
import com.example.bloom.viewmodel.BloomViewModel
import com.example.bloom.viewmodel.BloomViewModelFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val bloomViewModel: BloomViewModel by activityViewModels {
        BloomViewModelFactory(BloomRepository())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val themeListAdapter = ThemeListAdapter(requireContext())
        val cartListAdapter = CartListAdapter(requireContext())
        binding.themeList.layoutManager =
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL)
        binding.cartList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        bloomViewModel.mediaList.observe(viewLifecycleOwner, { it ->
            it.forEach { Log.e(TAG, it.id.toString()) }
            themeListAdapter.submitList(it)
            cartListAdapter.submitList(it)
        })
        binding.themeList.adapter = themeListAdapter
        binding.cartList.adapter = cartListAdapter
        bloomViewModel.getCollectionsList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "HomeFragment"
    }
}