package com.example.placementtracker.firebase_auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class FirebaseAuthenticator(private val firebaseAuth: FirebaseAuth= Firebase.auth){

    suspend fun signUpWithEmailPassword(email: String, password: String): FirebaseUser? {
        try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            return firebaseAuth.currentUser
        } catch (e: Exception) {
            // Handle the exception, e.g., log it or return null
            return null
        }
    }

    suspend fun signInWithEmailPassword(email: String, password: String): FirebaseUser? {
        try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            return firebaseAuth.currentUser
        } catch (e: Exception) {
            // Handle the exception, e.g., log it or return null
            return null
        }
    }

    fun signOut(): FirebaseUser? {
        firebaseAuth.signOut()
        return firebaseAuth.currentUser
    }

    fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    suspend fun sendPasswordReset(email: String):Boolean {
        try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            return true
        } catch (e: Exception) {
            // Handle the exception, e.g., log it or throw it
            println("Password reset exception"+e)
            return false
        }
    }
}