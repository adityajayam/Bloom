package com.example.bloom.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.bloom.adapter.SearchListAdapter
import com.example.bloom.adapter.SpacesItemDecoration
import com.example.bloom.databinding.FragmentFavouritesBinding
import com.example.bloom.repository.BloomRepository
import com.example.bloom.viewmodel.BloomViewModel
import com.example.bloom.viewmodel.BloomViewModelFactory

class FavouritesFragment : Fragment() {
    private lateinit var favouritesViewModel: FavouritesViewModel
    private var _binding: FragmentFavouritesBinding? = null
    private val binding get() = _binding!!
    private val bloomViewModel: BloomViewModel by activityViewModels {
        BloomViewModelFactory(BloomRepository())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        favouritesViewModel =
            ViewModelProvider(this).get(FavouritesViewModel::class.java)

        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val searchListAdapter = SearchListAdapter(requireContext()) { it ->
            //ToDo call download work from here
            bloomViewModel.downloadImage(activity?.applicationContext!!, it)
        }
        binding.searchList.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.searchList.addItemDecoration(SpacesItemDecoration(16))
        bloomViewModel.mediaList.observe(viewLifecycleOwner, { it ->
            it.forEach { Log.e(TAG, it.id.toString()) }
            searchListAdapter.submitList(it)
        })
        binding.searchList.adapter = searchListAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "FavouritesFragment"
    }
}