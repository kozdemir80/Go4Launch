package com.example.go4launch.activities
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.go4launch.R
import com.example.go4launch.constants.Constants.Companion.RC_SIGN_IN
import com.example.go4launch.databinding.SignInActivityBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.auth.User
import com.google.firebase.storage.StorageReference


private lateinit var binding: SignInActivityBinding
 private lateinit var mAuth: FirebaseAuth
 @SuppressLint("StaticFieldLeak")
 private lateinit var googleSignInClient: GoogleSignInClient
 private lateinit var databaseReference:DatabaseReference
 private lateinit var storageReference: StorageReference
 private lateinit var dialog:Dialog
 private lateinit var user:User
 private lateinit var uid:String




class SignInActivity: AppCompatActivity() {
    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_in_activity)
        binding = SignInActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        mAuth = FirebaseAuth.getInstance()

        binding.btnLoginGoogle.setOnClickListener {
            singIn()
        }



    }







    private fun singIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful) {
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d("SignInActivity", "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken)
                } catch (e: ApiException) {
                    Log.w("SignInActivity", "Google sign in failed", e)
                }
            } else {
                Log.w("SignInActivity", exception.toString())
            }

        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credentials = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credentials)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("SingInActivity", "signInWithCredential:success")
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    val name = mAuth.currentUser?.displayName
                    val email = mAuth.currentUser?.email
                    val userPhoto = mAuth.currentUser?.photoUrl
                    val preferences = getSharedPreferences("userDetails", MODE_PRIVATE)
                    val editor = preferences.edit()
                    editor.putString("name", name)
                    Log.d("mName",name.toString())
                    editor.putString("email", email)
                    editor.putString("userPhoto", userPhoto.toString())
                    editor.apply()

                } else {
                    Log.w("SignInActivity", "signInWithCredential:failure", task.exception)

                }

            }
    }
}