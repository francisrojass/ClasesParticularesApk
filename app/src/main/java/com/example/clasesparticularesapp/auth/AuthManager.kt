package com.example.clasesparticularesapp.auth

import android.app.Activity
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.AuthCredential
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class AuthManager(private val activity: Activity) {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val googleSignInClient: GoogleSignInClient

    init {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("TU_CLIENT_ID_AQUI") // 🔹 Reemplaza esto con el Client ID de Firebase
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(activity, gso)
    }

    fun registerUser(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                callback(task.isSuccessful, task.exception?.message)
            }
    }

    fun loginUser(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                callback(task.isSuccessful, task.exception?.message)
            }
    }

    fun signInWithGoogle(callback: (Intent) -> Unit) {
        val signInIntent = googleSignInClient.signInIntent
        callback(signInIntent)
    }

    fun handleGoogleSignInResult(data: Intent?, callback: (Boolean, String?) -> Unit) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            auth.signInWithCredential(credential)
                .addOnCompleteListener(activity) { authTask ->
                    callback(authTask.isSuccessful, authTask.exception?.message)
                }
        } catch (e: ApiException) {
            callback(false, "Error en el inicio de sesión con Google: ${e.message}")
        }
    }

    fun signOut(callback: () -> Unit) {
        auth.signOut()
        googleSignInClient.signOut().addOnCompleteListener { callback() }
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }
}


