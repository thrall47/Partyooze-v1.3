package com.fenrir4.partyooze.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.core.extensions.makeGone
import com.core.extensions.makeVisible
import com.core.extensions.toast
import com.facebook.*
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.fenrir4.partyooze.R
import com.fenrir4.partyooze.databinding.ActivityAuthenticationBinding
import com.fenrir4.partyooze.sharedpref.SharedPrefData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class AuthenticationActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        fun startActivity(context: Context) {
            val intent = Intent(context, AuthenticationActivity::class.java)
            context.startActivity(intent)
        }
    }
    private lateinit var binding: ActivityAuthenticationBinding
    private lateinit var auth: FirebaseAuth
    private var mCallbackManager: CallbackManager? = null
    private var currentUser: FirebaseUser? = null

    private val rcSignIn = 123
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        mCallbackManager = CallbackManager.Factory.create()
//        binding.facebookLogin1.setReadPermissions("email", "public_profile")

//        binding.facebookLogin.setOnClickListener(this)
//        onSkipClick()
    }

    fun signIn(view: android.view.View) {
        val emailET: EditText = findViewById(R.id.btnSignInGoogle)
        val passET: EditText = findViewById(R.id.facebook_login)
        val email = emailET.text.toString()
        val pass = passET.text.toString()
        val db = Firebase.firestore

        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val user = auth.currentUser
                    val uid = user!!.uid
                    db.collection("Users").document(uid)
                        .get()
                        .addOnSuccessListener {
                            val sharedPrefData= SharedPrefData(this)
                            sharedPrefData.setName((it.get("name") as String?))
                        }

                    Toast.makeText(this@AuthenticationActivity, "Signed in successfully.", Toast.LENGTH_SHORT).show()
                    val i = Intent(this, MainActivity::class.java)
                    startActivity(i)
                    finish()
                } else {
                    Toast.makeText(this@AuthenticationActivity, "Failed to sign in.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mCallbackManager!!.onActivityResult(requestCode, resultCode, data)
        // RC_SIGN_IN is the request code you passed when starting the sign in flow.

        if (requestCode == rcSignIn) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {

                currentUser = FirebaseAuth.getInstance().currentUser

                val sharedPrefData= SharedPrefData(this@AuthenticationActivity)
                sharedPrefData.setName(currentUser!!.displayName)
                sharedPrefData.setEmail(currentUser!!.email)
                MainActivity.startActivity(this)
                finish()
                return
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    toast(getString(R.string.authentication_login_canceled))
                    return
                }
                if (response.error!!.errorCode == ErrorCodes.NO_NETWORK) {
                    toast(getString(R.string.all_error_internet_connectivity))
                    return
                }
                if (response.error!!.errorCode == ErrorCodes.UNKNOWN_ERROR) {
                    toast(getString(R.string.all_error_unknown))
                    return
                }
            }
        }
    }

    /**
     * Sets the visibility of the chipSkip based on the mandatory authentication configuration
     */
//    private fun showSkipSignIn() {
//        if (resources.getBoolean(R.bool.enable_mandatory_authentication))
//            binding.chipSkip.makeGone() else binding.chipSkip.makeVisible()
//    }

//    private fun onSignInWithGoogleClick() {
//        binding.btnSignInGoogle.setOnClickListener {
//            startSignInFlow(AuthUI.IdpConfig.GoogleBuilder().build())
//        }
//    }

//    private fun onSignInWithEmailClick() {
//        binding.btnSignInEmail.setOnClickListener {
//            startSignInFlow(AuthUI.IdpConfig.EmailBuilder().build())
//        }
//    }

//    private fun onSkipClick() {
//        binding.chipSkip.setOnClickListener {
//            MainActivity.startActivity(this)
//            finish()
//            val sharedPrefData= SharedPrefData(this@AuthenticationActivity)
//            sharedPrefData.setSkip("Skip")
//        }
//    }

    private fun startSignInFlow(idpConfig: AuthUI.IdpConfig) {
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(
                    listOf(
                        idpConfig
                    )
                )
                .setTheme(R.style.AppTheme)
                .setIsSmartLockEnabled(false)
                .build(),
            rcSignIn
        )
    }

//    override fun onClick(v: View?) {
//        if (v!!.id == R.id.facebook_login) {
//            binding.facebookLogin1.performClick()
//
//            binding.facebookLogin1.registerCallback(mCallbackManager,
//                object : FacebookCallback<LoginResult> {
//                    override fun onSuccess(result: LoginResult?) {
//
//                        handleFaceBookToken(result!!.accessToken)
//
//
//                        val request = GraphRequest.newMeRequest(
//                            result.accessToken
//                        ) { `object`: JSONObject, response: GraphResponse ->
//                            Log.v("LoginActivity", response.toString())
//                            try {
//                                val email = `object`.getString("email")
//                                val first_name = `object`.getString("first_name")
//                                val last_name = `object`.getString("last_name")
//                                val auth_uid = `object`.getString("id")
//                                val sharedPrefData= SharedPrefData(this@AuthenticationActivity)
//                                sharedPrefData.setName(first_name+last_name)
//                                sharedPrefData.setEmail(email)
//                                sharedPrefData.setAuthId(auth_uid)
//                                Log.d("loginSucces", first_name)
//                                Log.d("loginSucces", email)
//                                val intent = Intent(this@AuthenticationActivity, MainActivity::class.java)
//                                intent.putExtra("email", email)
//                                intent.putExtra("first_name", first_name)
//                                intent.putExtra("last_name", last_name)
//                                intent.putExtra("auth_uid", auth_uid)
//
//                                startActivity(intent)
//                                finish()
//                            } catch (e: JSONException) {
//                                e.printStackTrace()
//                            }
//                        }
//                        val parameters = Bundle()
//                        parameters.putString("fields", "email,first_name,last_name")
//                        request.parameters = parameters
//                        request.executeAsync()
//                        // Toast.makeText(SignInActivity.this, loginResult.getAccessToken().getUserId(), Toast.LENGTH_SHORT).show();
//
//                    }
//
//                    override fun onCancel() {
//                        Log.d("cancelLogin", "canceled")
//                    }
//
//                    override fun onError(error: FacebookException?) {
//                        Log.d("majorError", error.toString())
//
//                    }
//                })
//
//
//        }
//    }

    private fun handleFaceBookToken(accessToken: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(accessToken.token)
        auth.signInWithCredential(credential).addOnCompleteListener(
            this
        ) { task: Task<AuthResult?> ->
            if (task.isSuccessful) {
                val currentuser: FirebaseUser = auth.currentUser!!

                /*if (currentuser!=null)
                {
                    MainActivity.startActivity(this)

                }*/
            }
        }
    }

    fun SignUp(view: android.view.View) {
        val i = Intent(this, SignUpAct::class.java)
        startActivity(i)
    }

    override fun onClick(v: View?) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(v?.windowToken, 0)
    }

}
