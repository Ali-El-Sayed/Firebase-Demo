package com.example.firebase.screens.addUser

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.firebase.R
import com.example.firebase.databinding.FragmentAddUserBinding

class AddUserFragment : Fragment(R.layout.fragment_add_user) {
    private lateinit var addUserViewModel: AddUserViewModel

    private var binding: FragmentAddUserBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddUserBinding.bind(view)
        addUserViewModel = ViewModelProvider(this)[AddUserViewModel::class.java]

        binding?.btAddUser?.setOnClickListener {
            val name: String = binding?.etName?.text.toString()
            val email: String = binding?.etEmail?.text.toString()
            val age: Int = binding?.etAge?.text.toString().toIntOrNull() ?: 0
            addUserViewModel.addUser(name, age, email)
            it.findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}