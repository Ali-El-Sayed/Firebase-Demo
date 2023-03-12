package com.example.firebase.screens.logIn

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.firebase.R
import com.example.firebase.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater)


        // Setup Buttons Clicks
        binding.btSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_fLogin_to_signUp)
        }
        binding.btSignIn.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {}

            val navOptions = NavOptions.Builder().setPopUpTo(R.id.fLogin, true).build()
            findNavController().navigate(R.id.action_fLogin_to_rvUserList, null, navOptions)
            activity?.let { act ->
                val actionBar = (act as AppCompatActivity).supportActionBar
                actionBar?.setDisplayHomeAsUpEnabled(false)
            }
        }

        return binding.root
    }
}