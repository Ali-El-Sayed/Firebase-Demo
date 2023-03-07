package com.example.firebase.screens.updaterUser

import android.opengl.Visibility
import android.os.Bundle
import android.os.SystemClock.sleep
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.firebase.R
import com.example.firebase.databinding.FragmentUpdateUserBinding
import com.example.firebase.models.User

class UpdateUserFragment : Fragment(R.layout.fragment_update_user) {
    private var binding: FragmentUpdateUserBinding? = null
    private lateinit var viewmodel: UpdateUserViewmodel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // view Binding
        binding = FragmentUpdateUserBinding.bind(requireView())
        // setup viewModel
        viewmodel = ViewModelProvider(this)[UpdateUserViewmodel::class.java]
        binding?.lifecycleOwner = viewLifecycleOwner

        viewmodel.updatedUser.value = User(
            arguments?.getString("userId") ?: "",
            arguments?.getString("userName") ?: "",
            arguments?.getString("userAge")?.toInt() ?: 0,
            arguments?.getString("userEmail") ?: ""
        )
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