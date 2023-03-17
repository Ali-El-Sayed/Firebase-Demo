package com.example.firebase.screens.addUser

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.firebase.R
import com.example.firebase.databinding.FragmentAddUserBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class AddUserFragment : Fragment(R.layout.fragment_add_user) {
    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()
    private var binding: FragmentAddUserBinding? = null

    // Setup ViewModel
    val viewmodel: AddUserViewModel by viewModels {
        AddUserViewModelFactory(
            firebaseDatabase,
            firebaseStorage,
            requireActivity().application,
            requireActivity().activityResultRegistry
        )
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

        // setup add user button click listener
        binding?.btAddUser?.setOnClickListener {
            performAdding()
            it.findNavController().navigateUp()
        }

        // select picture from gallery and set it to the image view
        binding?.ivUserImage?.setOnClickListener {
            pickImage()
        }
    }

    private fun pickImage() {
        viewmodel.getStoragePermission()
        viewmodel.imgUri.observe(viewLifecycleOwner) {
            binding?.ivUserImage?.setImageURI(it)
        }
    }


    private fun performAdding() {
        binding?.pbLoading?.visibility = View.VISIBLE
        binding?.btAddUser?.isEnabled = false
        val name: String = binding?.etName?.text.toString().trim()
        val email: String = binding?.etEmail?.text.toString().trim()
        val age: Int = binding?.etAge?.text.toString().trim().toIntOrNull() ?: 0
        viewmodel.addUser(name, age, email)
        binding?.pbLoading?.visibility = View.INVISIBLE
        binding?.btAddUser?.isEnabled = true
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}