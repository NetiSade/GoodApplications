package com.android.goodapplications.shira

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View
import android.widget.TextView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.LinearLayout
import com.android.goodapplications.shira.ViewModel.AuthViewModel
import com.google.android.gms.common.SignInButton

/**
 * Demonstrate Firebase Authentication using a Google ID Token.
 */
class AuthActivity : AppCompatActivity(), View.OnClickListener {

    private var mAuth: FirebaseAuth? = null
    private lateinit var viewModel : AuthViewModel
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private var mStatusTextView: TextView? = null
    private var mDetailTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        viewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)
        // Views
        mStatusTextView = findViewById(R.id.status)
        mDetailTextView = findViewById(R.id.detail)

        // Button listeners
        findViewById<SignInButton>(R.id.sign_in_button).setOnClickListener(this)
        findViewById<Button>(R.id.sign_out_button).setOnClickListener(this)
        findViewById<Button>(R.id.disconnect_button).setOnClickListener(this)


        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        mAuth = viewModel.getFirebaseAuth()

    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = viewModel.getCurrentUser()//mAuth!!.currentUser
        updateUI(currentUser)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                updateUI(null)
            }

        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id!!)
      //  showProgressDialog()

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)

        mAuth!!.signInWithCredential(credential)
                .addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success")
                        val user = mAuth!!.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                        Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                        updateUI(null)
                    }
                   // hideProgressDialog()
                })
    }

    /*
    private fun hideProgressDialog() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun showProgressDialog() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }*/

    private fun signIn() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    private fun signOut() {
        // Firebase sign out
        mAuth!!.signOut()

        // Google sign out
        mGoogleSignInClient!!.signOut().addOnCompleteListener(this,
                OnCompleteListener<Void> { updateUI(null) })
    }

    private fun revokeAccess() {
        // Firebase sign out
        mAuth!!.signOut()

        // Google revoke access
        mGoogleSignInClient!!.revokeAccess().addOnCompleteListener(this,
                OnCompleteListener<Void> { updateUI(null) })
    }

    private fun updateUI(user: FirebaseUser?) {
     //   hideProgressDialog()
        if (user != null) {
            mStatusTextView!!.text = getString(R.string.google_status_fmt, user.email)
            mDetailTextView!!.text = getString(R.string.firebase_status_fmt, user.uid)

            findViewById<SignInButton>(R.id.sign_in_button).visibility = View.GONE
            findViewById<LinearLayout>(R.id.sign_out_and_disconnect).visibility = View.VISIBLE
        } else {
            mStatusTextView!!.setText(R.string.signed_out)
            mDetailTextView!!.text = null

            findViewById<SignInButton>(R.id.sign_in_button).visibility = View.VISIBLE
            findViewById<LinearLayout>(R.id.sign_out_and_disconnect).visibility = View.GONE
        }
    }

    override fun onClick(v: View) {
        val i = v.id
        when (i) {
            R.id.sign_in_button -> signIn()
            R.id.sign_out_button -> signOut()
            R.id.disconnect_button -> revokeAccess()
        }
    }

    companion object {

        private val TAG = "GoogleActivity"
        private val RC_SIGN_IN = 9001
    }
}