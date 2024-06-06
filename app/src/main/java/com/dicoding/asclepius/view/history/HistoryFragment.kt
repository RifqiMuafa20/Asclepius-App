package com.dicoding.asclepius.view.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.data.local.entity.PhotoHistory
import com.dicoding.asclepius.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    private lateinit var _binding: FragmentHistoryBinding
    private val historyViewModel by viewModels<HistoryViewModel>()
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setHasOptionsMenu(true)

        val layoutManager = LinearLayoutManager(context)
        binding.rvHistory.layoutManager = layoutManager

        historyViewModel.getAllPhotoVisited()?.observe(viewLifecycleOwner) { listPhoto ->
            setHistoryData(listPhoto)
        }

        return root
    }

    private fun setHistoryData(listPhoto: List<PhotoHistory>) {
        val adapter = HistoryAdapter()
        adapter.submitList(listPhoto)
        binding.rvHistory.adapter = adapter
    }
}