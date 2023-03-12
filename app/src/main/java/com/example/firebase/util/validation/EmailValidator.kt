package com.example.firebase.util.validation

private const val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

class EmailValidator : Validator {
    override fun validate(input: String): String? {
        return when {
            input.isNotBlank() && input.matches(emailPattern.toRegex()) -> null
            else -> "Invalid email address"
        }
    }

}