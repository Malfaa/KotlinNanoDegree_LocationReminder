package com.udacity.project4.authentication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.udacity.project4.R
import com.udacity.project4.locationreminders.RemindersActivity


/**
 * This class should be the starting point of the app, It asks the users to sign in / register, and redirects the
 * signed in users to the RemindersActivity.
 */
class AuthenticationActivity : AppCompatActivity() {

    companion object {
        const val SIGN_IN_RESULT_CODE = 1001
    }

    private val viewModel by viewModels<AuthenticationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        //Instantiating button
        val login = findViewById<Button>(R.id.login)

        //Observing the livedata to changes
        viewModel.authenticationState.observe(this, Observer { authenticationState ->
            when(authenticationState){
                AuthenticationViewModel.AuthenticationEnum.AUTHENTICATED -> toRemindersActivity()
                else -> assert(true)
            }
        })

        //Deploying sign in method
        login.setOnClickListener {
            launchSignInFlow()
        }

        // You must provide a custom layout XML resource and configure at least one
         // provider button ID. It's important that that you set the button ID for every provider
        // that you have enabled.
        // You must provide a custom layout XML resource and configure at least one
        // provider button ID. It's important that that you set the button ID for every provider
        // that you have enabled.
        /*
        val customLayout = AuthMethodPickerLayout.Builder(com.udacity.project4.R.layout.your_custom_layout_xml)
            .setGoogleButtonId(com.udacity.project4.R.id.bar)
            .setEmailButtonId(com.udacity.project4.R.id.foo) // ...
            .setTosAndPrivacyPolicyId(com.udacity.project4.R.id.baz)
            .build()

        val signInIntent = AuthUI.getInstance().createSignInIntentBuilder() // ...
            .setAuthMethodPickerLayout(customLayout)
            .build()*/

    }

    //Open the Reminder Activity
    private fun toRemindersActivity() {
        val intent = Intent(this, RemindersActivity::class.java)
        startActivity(intent)
    }

    private fun launchSignInFlow() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        startActivityForResult(
            AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(
                providers
            ).build(), SIGN_IN_RESULT_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_RESULT_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in user.
                toRemindersActivity()

            } else {
                // Sign in failed. If response is null the user canceled the sign-in flow using
                // the back button. Otherwise check response.getError().getErrorCode() and handle
                // the error.
                Log.e("Authetication Error", response?.error.toString())
            }
        }
    }
}
