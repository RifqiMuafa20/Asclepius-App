package com.dicoding.asclepius.view.history

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.local.entity.PhotoHistory
import com.dicoding.asclepius.databinding.FragmentHistoryBinding
import com.dicoding.asclepius.view.home.MainFragment

class HistoryFragment : Fragment() {

    private lateinit var _binding: FragmentHistoryBinding
    private val historyViewModel by viewModels<HistoryViewModel>()
    private val binding get() = _binding

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                showToast("Permission request granted")
            } else {
                showToast("Permission request denied")
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this.requireContext(),
            HistoryFragment.REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> {
                showConfirmationDialog()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Konfirmasi")
        builder.setMessage("Apakah Anda yakin ingin menghapus data?")
        builder.setPositiveButton("Ya") { dialog, which ->
            historyViewModel.deleteAllPhotoVisited()
        }
        builder.setNegativeButton("Tidak") { dialog, which ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}