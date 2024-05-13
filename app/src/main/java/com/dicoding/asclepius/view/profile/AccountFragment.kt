package com.dicoding.asclepius.view.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dicoding.asclepius.R
import de.hdodenhof.circleimageview.CircleImageView

class AccountFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)

        val imageView: CircleImageView = view.findViewById(R.id.imageView)
        imageView.setImageResource(R.drawable.foto_archery)

        return view
    }
}