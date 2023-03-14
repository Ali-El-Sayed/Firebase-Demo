package com.example.firebase.screens.forgetPassword

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.TransitionBuilder.validate
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.firebase.R
import com.example.firebase.databinding.FragmentForgetPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgetPasswordFragment : Fragment(R.layout.fragment_forget_password) {

    private var binding: FragmentForgetPasswordBinding? = null
    private val auth = FirebaseAuth.getInstance()
    private val viewmodel: ForgetPasswordViewModel by viewModels {
        ForgetPasswordViewModelFactory(auth, requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForgetPasswordBinding.inflate(inflater)

        binding?.btResetPassword?.setOnClickListener {
            resetPassword()
            viewmodel.status.observe(viewLifecycleOwner) {
                if (it == true) findNavController().popBackStack()
            }
        }

        return binding?.root
    }

    private fun resetPassword() {
        val email = binding?.etEmail?.text.toString().trim()
        val emailValidation = viewmodel.validateEmail(email)

        if (emailValidation == null) viewmodel.resetPassword(email)
        else updateUi(emailValidation)
    }

    private fun updateUi(emailValidation: String?) {
        binding?.etEmailLayout?.error = emailValidation
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}