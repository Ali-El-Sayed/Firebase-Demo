package com.example.firebase.screens.signUp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.motion.widget.TransitionBuilder.validate
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.firebase.R
import com.example.firebase.databinding.FragmentSignUpBinding
import com.example.firebase.util.validation.EmailValidator
import com.example.firebase.util.validation.PasswordValidator
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpFragment : Fragment(R.layout.fragment_sign_up) {
    private var binding: FragmentSignUpBinding? = null
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Setup ViewModel
    private val viewModel: SignUpViewModel by viewModels {
        SignUpViewModelFactory(auth, requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater)

        binding?.btSignUp?.setOnClickListener {
            signUp()
        }
        return binding?.root
    }

    private fun signUp() {
        val email = binding?.etEmail?.text.toString().trim()
        val password = binding?.etPassword?.text.toString().trim()

        val (emailValidation, passwordValidation) = validate(email, password)

//        if (emailValidation == null && passwordValidation == null)
        signUpWithFirebase(email, password)
//        else updateUi(emailValidation, passwordValidation)
    }

    private fun updateUi(emailValidation: String?, passwordValidation: String?) {
        binding?.etEmailLayout?.error = emailValidation
        binding?.etPasswordLayout?.error = passwordValidation
    }

    private fun validate(
        email: String, password: String
    ): Pair<String?, String?> {
        val emailValidation = viewModel.validateEmail(email)
        val passwordValidation = viewModel.validatePassword(password)
        return Pair(emailValidation, passwordValidation)
    }


    private fun signUpWithFirebase(email: String, password: String) {
        viewModel.signUpWithFirebase(email, password)
        viewModel.status.observe(viewLifecycleOwner) {
            if (it) navigateToSignInScreen()
        }
    }

    private fun navigateToSignInScreen() {
        val options = NavOptions.Builder().setPopUpTo(R.id.fLogin, true).build()
        findNavController().navigate(R.id.action_signUp_to_fLogin, null, options)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}