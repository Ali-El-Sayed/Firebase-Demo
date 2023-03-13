package com.example.firebase.screens.addUser

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.firebase.R
import com.example.firebase.databinding.FragmentAddUserBinding
import com.google.firebase.database.FirebaseDatabase

class AddUserFragment : Fragment(R.layout.fragment_add_user) {
    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var binding: FragmentAddUserBinding? = null

    // Setup ViewModel
    private val viewmodel: AddUserViewModel by viewModels {
        AddUserViewModelFactory(firebaseDatabase, requireActivity().application)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup ViewBinding
        binding = FragmentAddUserBinding.bind(view)
        binding?.lifecycleOwner = viewLifecycleOwner

        viewmodel.isloading.observe(viewLifecycleOwner) {
            if (it) binding?.pbLoading?.visibility = View.VISIBLE
            else binding?.pbLoading?.visibility = View.INVISIBLE
        }

        binding?.btAddUser?.setOnClickListener {
            performAdding()
            it.findNavController().navigateUp()
        }
    }

    private fun performAdding() {
        val name: String = binding?.etName?.text.toString().trim()
        val email: String = binding?.etEmail?.text.toString().trim()
        val age: Int = binding?.etAge?.text.toString().trim().toIntOrNull() ?: 0
        viewmodel.addUser(name, age, email)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}