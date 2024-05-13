package com.dicoding.asclepius.view.article

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.BuildConfig
import com.dicoding.asclepius.data.remote.response.ArticlesItem
import com.dicoding.asclepius.data.remote.response.DetailNewsResponse
import com.dicoding.asclepius.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArticleViewModel : ViewModel() {
    private val _listArticle = MutableLiveData<List<ArticlesItem>>()
    val listArticle: LiveData<List<ArticlesItem>> = _listArticle

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    companion object {
        private const val TAG = "ArticleViewModel"
        private const val API_KEY = BuildConfig.API_KEY
        private const val category = BuildConfig.CATEGORY
        private const val language = BuildConfig.LANGUAGE
        private const val query = BuildConfig.QUERY
    }

    init {
        getArticle(query, category, language, API_KEY)
    }

    fun getArticle(query: String, category: String, language: String, apiKey: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getTopHeadlines(query, category, language, apiKey)
        client.enqueue(object : Callback<DetailNewsResponse> {
            override fun onResponse(
                call: Call<DetailNewsResponse>,
                response: Response<DetailNewsResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _isError.value = false
                    _listArticle.value = response.body()?.articles
                } else {
                    _isError.value = true
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailNewsResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}