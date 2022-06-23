package com.example.go4launch.activities
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.go4launch.R
import com.example.go4launch.constants.Constants.Companion.RC_SIGN_IN
import com.example.go4launch.databinding.SignInActivityBinding
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
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
private lateinit var callbackManager: CallbackManager



@Suppress("DEPRECATION")
class SignInActivity: AppCompatActivity() {
    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_in_activity)
        binding = SignInActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val TAG=""

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        mAuth = FirebaseAuth.getInstance()

        binding.btnLoginGoogle.setOnClickListener {
            singIn()
        }
        callbackManager= CallbackManager.Factory.create()
        // Initialize Facebook Login button
        callbackManager = CallbackManager.Factory.create()

        binding.btnLoginFb.setReadPermissions("email", "public_profile")
        binding.btnLoginFb.registerCallback(callbackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d(TAG, "facebook:onSuccess:$loginResult")
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                Log.d(TAG, "facebook:onCancel")
                // ...
            }

            override fun onError(error: FacebookException) {
                Log.d(TAG, "facebook:onError", error)
                // ...
            }
        })


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
        callbackManager.onActivityResult(requestCode, resultCode, data)
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

                  val username= mAuth.currentUser?.displayName
                    Log.d("myuser",username.toString())
                   val preferences=getSharedPreferences("name", MODE_PRIVATE)
                    val editor=preferences.edit()
                    editor.putString("myName",username)
                    editor.apply()
                    Log.d("userName",editor.putString("myname",username).toString())

                } else {
                    Log.w("SignInActivity", "signInWithCredential:failure", task.exception)

                }

            }
    }
    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()

                }
            }
    }
}