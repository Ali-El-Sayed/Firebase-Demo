package com.example.firebase.screens.logIn

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.TransitionBuilder.validate
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.firebase.R
import com.example.firebase.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment(R.layout.fragment_login) {
    private var binding: FragmentLoginBinding? = null
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    //  Setup ViewModel
    private val viewModel: LogInViewModel by viewModels {
        LogInViewModelFactory(auth, requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)

        // Setup Buttons Clicks
        binding?.btSignUp?.setOnClickListener {
            findNavController().navigate(R.id.action_fLogin_to_signUp)
        }
        binding?.btSignIn?.setOnClickListener {
            logIn()
        }

        return binding?.root
    }

    private fun logIn() {
        val email = binding?.etEmail?.text.toString().trim()
        val password = binding?.etPassword?.text.toString().trim()

        val (emailValidation, passwordValidation) = validate(email, password)

        if (emailValidation == null && passwordValidation == null)
            logInWithFirebase(email, password)
        else
            updateUi(emailValidation, passwordValidation)
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

    private fun navigateToListFragment() {
        val navOptions = NavOptions.Builder().setPopUpTo(R.id.fLogin, true).build()
        findNavController().navigate(R.id.action_fLogin_to_rvUserList, null, navOptions)
        activity?.let { act ->
            val actionBar = (act as AppCompatActivity).supportActionBar
            actionBar?.setDisplayHomeAsUpEnabled(false)
        }
    }

    private fun logInWithFirebase(email: String, password: String) {
        viewModel.logInWithFirebase(email, password)
        viewModel.status.observe(viewLifecycleOwner) {
            if (it) navigateToListFragment()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}