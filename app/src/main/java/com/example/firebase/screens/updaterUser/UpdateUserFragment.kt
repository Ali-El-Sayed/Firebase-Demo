package com.example.firebase.screens.updaterUser

import android.opengl.Visibility
import android.os.Bundle
import android.os.SystemClock.sleep
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.firebase.R
import com.example.firebase.databinding.FragmentUpdateUserBinding
import com.example.firebase.models.User
import com.google.firebase.database.FirebaseDatabase

class UpdateUserFragment : Fragment(R.layout.fragment_update_user) {
    private var binding: FragmentUpdateUserBinding? = null
    private  val viewmodel: UpdateUserViewmodel by viewModels {
        UpdateUserViewModelFactory(firebaseDatabase, requireActivity().application)
    }
    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup ViewBinding
        binding = FragmentUpdateUserBinding.bind(requireView())
        binding?.lifecycleOwner = viewLifecycleOwner

        // Update Viewmodel state
        viewmodel.updatedUser.value = User(
            arguments?.getString("userId") ?: "",
            arguments?.getString("userName") ?: "",
            arguments?.getString("userAge")?.toInt() ?: 0,
            arguments?.getString("userEmail") ?: ""
        )

        // Loading Spinner Observer
        viewmodel.isloading.observe(viewLifecycleOwner) {
            if (it) binding?.pbLoading?.visibility = View.VISIBLE
            else binding?.pbLoading?.visibility = View.INVISIBLE
        }

        bindUi()
        binding?.btUpdateUser?.setOnClickListener {
            performUpdate()
            it.findNavController().navigateUp()
        }
    }

    private fun bindUi() {
        binding?.etUpdateName?.setText(viewmodel.updatedUser.value?.name ?: "")
        binding?.etUpdateEmail?.setText(viewmodel.updatedUser.value?.email ?: "")
        binding?.etUpdateAge?.setText(viewmodel.updatedUser.value?.age.toString())
    }

    private fun performUpdate() {
        getDataFromUi()
        viewmodel.updateUser()
    }

    private fun getDataFromUi() {
        viewmodel.updatedUser.value?.age = binding?.etUpdateAge?.text.toString().toInt()
        viewmodel.updatedUser.value?.name = binding?.etUpdateName?.text.toString()
        viewmodel.updatedUser.value?.email = binding?.etUpdateEmail?.text.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}