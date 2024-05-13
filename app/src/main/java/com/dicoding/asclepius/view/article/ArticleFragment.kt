package com.dicoding.asclepius.view.article

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.remote.response.ArticlesItem
import com.dicoding.asclepius.databinding.FragmentArticleBinding

class ArticleFragment : Fragment() {

    private lateinit var _binding: FragmentArticleBinding
    private val ArticleViewModel by viewModels<ArticleViewModel>()
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArticleBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val layoutManager = LinearLayoutManager(context)
        binding.rvArticles.layoutManager = layoutManager

        ArticleViewModel.listArticle.observe(viewLifecycleOwner) { listArticle ->
            setArticleData(listArticle, this.requireContext())
        }

        ArticleViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        ArticleViewModel.isError.observe(viewLifecycleOwner) {
            showToastError(it)
        }

        return root
    }

    private fun setArticleData(listArticle: List<ArticlesItem>, context: Context) {
        val adapter = ArticleAdapter(context)
        adapter.submitList(listArticle)
        binding.rvArticles.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToastError(isError: Boolean) {
        if (isError) Toast.makeText(
            this.context,
            "Terjadi kesalahan!! Mohon Bersabar",
            Toast.LENGTH_SHORT
        ).show()
    }
}