package com.udacity.project4.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map

class AuthenticationViewModel : ViewModel(){

    enum class AuthenticationEnum {
        AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTHENTICATION
    }


    val authenticationState = FirebaseUserLiveData().map { user ->
        if (user != null) {
            AuthenticationEnum.AUTHENTICATED
        } else {
            AuthenticationEnum.UNAUTHENTICATED
        }
    }
}