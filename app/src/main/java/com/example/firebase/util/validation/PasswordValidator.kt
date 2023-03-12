package com.example.firebase.util.validation

private const val passwordPattern =
    "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}"


class PasswordValidator : Validator {

    override fun validate(input: String): String? {

        val hasDigit = input.matches(".*\\d.*".toRegex())
        val hasLowerCase = input.matches(".*[a-z].*".toRegex())
        val hasUpperCase = input.matches(".*[A-Z].*".toRegex())
        val hasSpecialChar = input.matches(".*[@#$%^&+=].*".toRegex())
        val isLengthValid = input.length >= 8

        return when {
            !isLengthValid -> "Password should be at least 8 characters long"
            !hasLowerCase -> "Password should contain at least one lower case letter"
            !hasUpperCase -> "Password should contain at least one upper case letter"
            !hasSpecialChar -> "Password should contain at least one special character (@#$%^&+=)"
            !hasDigit -> "Password should contain at least one digit"
            else -> null
        }
    }
}