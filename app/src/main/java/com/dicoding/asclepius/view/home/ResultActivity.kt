package com.dicoding.asclepius.view.home

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import org.tensorflow.lite.task.vision.classifier.Classifications

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var imageClassifierHelper: ImageClassifierHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))
        showLoading(true)
        imageUri?.let {
            Log.d("Image URI", "Show image: $it")
            binding.resultImage.setImageURI(it)
            analyzeImage(it)
        }

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun analyzeImage(uri: Uri) {
        showLoading(false)
        imageClassifierHelper = ImageClassifierHelper(
            context = this,
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    runOnUiThread {
                        showToast("Error analyze image: $error")
                    }
                }

                override fun onResults(
                    results: List<Classifications>?,
                    inferenceTime: Long
                ) {
                    runOnUiThread {
                        results?.let {
                            if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                                val resultLabel = it[0].categories[0].label
                                val resultScore = it[0].categories[0].score * 100

                                binding.resultText.text = getString(R.string.result_text, resultLabel.toString(), resultScore)
                                binding.inferenceText.text = getString(R.string.inference_time, inferenceTime.toString())

                            } else {
                                binding.resultText.text = getString(R.string.tflite_failed_to_load_model_with_error)
                            }
                        }
                    }
                }
            }
        )

        imageClassifierHelper.classifyStaticImage(uri)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
    }
}