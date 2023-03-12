package com.example.firebase.screens.signUp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.motion.widget.TransitionBuilder.validate
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
    private lateinit var binding: FragmentSignUpBinding
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val emailValidator = EmailValidator()
    private val passwordValidator = PasswordValidator()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater)

        binding.btSignUp.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            if (perfromValidation()) signUpWithFirebase(email, password)
        }


        return binding.root
    }

    private fun perfromValidation(): Boolean {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        val emailIsValid = emailValidator.validate(email)
        val passwordIsValid = passwordValidator.validate(password)

        binding.etEmailLayout.error = emailIsValid
        binding.etPasswordLayout.error = passwordIsValid
        return (emailIsValid.isNullOrEmpty() && passwordIsValid.isNullOrBlank())
    }

    private fun signUpWithFirebase(email: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        context, "Account Created Successfully", Toast.LENGTH_SHORT
                    ).show()
                    val options = NavOptions.Builder().setPopUpTo(R.id.fLogin, true).build()
                    findNavController().navigate(R.id.action_signUp_to_fLogin, null, options)

                } else Toast.makeText(context, task.exception?.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

}