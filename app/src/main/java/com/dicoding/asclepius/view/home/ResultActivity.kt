package com.dicoding.asclepius.view.home

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.text.SimpleDateFormat
import java.util.Date

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var imageClassifierHelper: ImageClassifierHelper
    private val resultViewModel by viewModels<ResultViewModel>()

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

        val pathSegments = imageUri.toString().split("/")
        val filename = pathSegments[pathSegments.size - 1]

        val date = SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Date())

        binding.backButton.setOnClickListener {
            resultViewModel.addVisited(filename, result, inference, date, imageUri.toString())
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
                                val resultText = getString(
                                    R.string.result_text,
                                    resultLabel.toString(),
                                    resultScore
                                )
                                val inferenceText =
                                    getString(R.string.inference_time, inferenceTime.toString())

                                result = resultText
                                inference = inferenceText

                                binding.resultText.text = resultText
                                binding.inferenceText.text = inferenceText

                            } else {
                                binding.resultText.text =
                                    getString(R.string.tflite_failed_to_load_model_with_error)
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
        lateinit var result: String
        lateinit var inference: String
    }
}