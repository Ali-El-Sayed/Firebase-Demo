package com.example.firebase.screens.addUser

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.firebase.R
import com.example.firebase.databinding.FragmentAddUserBinding

class AddUserFragment : Fragment(R.layout.fragment_add_user) {
    private lateinit var viewmodel: AddUserViewModel

    private var binding: FragmentAddUserBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddUserBinding.bind(view)
        viewmodel = ViewModelProvider(this)[AddUserViewModel::class.java]

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
        val name: String = binding?.etName?.text.toString()
        val email: String = binding?.etEmail?.text.toString()
        val age: Int = binding?.etAge?.text.toString().toIntOrNull() ?: 0
        viewmodel.addUser(name, age, email)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}