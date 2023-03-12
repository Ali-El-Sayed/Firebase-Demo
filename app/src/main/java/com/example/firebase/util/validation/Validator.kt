package com.example.firebase.util.validation

interface Validator {

    fun validate(input: String): String?
}