package com.fenrir4.partyooze.activity

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fenrir4.partyooze.R
import com.fenrir4.partyooze.databinding.SignUpActBinding
import com.fenrir4.partyooze.sharedpref.SharedPrefData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class SignUpAct : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    private var sharedPrefData:SharedPrefData?=null

    private lateinit var binding: SignUpActBinding

    private var currentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignUpActBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentUser = FirebaseAuth.getInstance().currentUser
        sharedPrefData = SharedPrefData(this)
        auth = FirebaseAuth.getInstance()


    }

    fun SignUpBtn(view: android.view.View) {
        val emailET: EditText = findViewById(R.id.emailET)
        val passET: EditText = findViewById(R.id.passET)
        val nameET: EditText = findViewById(R.id.nameET)
        val email = emailET.text.toString()
        val pass = passET.text.toString()
        val name = nameET.text.toString()

        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
//                    val profileUpdates = UserProfileChangeRequest.Builder()
//                        .setDisplayName(name).build()
//                   currentUser?.updateProfile(profileUpdates)

                    val user = auth.currentUser
                    val uid = user!!.uid
                    val name = hashMapOf(
                        "name" to name
                    )
                    val db = Firebase.firestore
                    db.collection("Users").document(uid)
                        .set(name)
//                        .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully written!") }
//                        .addOnFailureListener { e -> Log.w("TAG", "Error writing document", e) }

                    Toast.makeText(this, "Signed up successfully.", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Failed to sign up.", Toast.LENGTH_SHORT).show()
                }
            }
    }

}


