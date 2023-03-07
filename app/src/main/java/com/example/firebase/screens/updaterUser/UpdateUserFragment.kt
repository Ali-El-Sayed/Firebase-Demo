package com.example.firebase.screens.updaterUser

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.firebase.R
import com.example.firebase.databinding.FragmentUpdateUserBinding

class UpdateUserFragment : Fragment(R.layout.fragment_update_user) {
    private var binding: FragmentUpdateUserBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUpdateUserBinding.bind(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}