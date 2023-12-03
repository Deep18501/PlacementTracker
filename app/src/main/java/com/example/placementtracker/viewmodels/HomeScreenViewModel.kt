package com.example.placementtracker.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import com.example.placementtracker.models.Student
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class HomeScreenViewModel :ViewModel(){
val auth=FirebaseAuth.getInstance()
    var currentUser by mutableStateOf<FirebaseUser?>(auth.currentUser)
    var userCollection = Firebase.firestore.collection("student")

    suspend fun loadPersonDet(): Student {
        try {
            val documentSnapshot = userCollection.document(auth.currentUser?.uid.toString()).get().await()

            if (documentSnapshot.exists()) {
                val userName = documentSnapshot.getString("name").toString()
                val userEmail = documentSnapshot.getString("email").toString()
                val userPhone = documentSnapshot.getString("phone").toString()
                val description = documentSnapshot.getString("description").toString()
                val branch = documentSnapshot.getString("branch").toString()
                val rollNo = documentSnapshot.getString("rollNo").toString()
                val placed=documentSnapshot.getBoolean("placed")
                var placement=false

                placed?.let {
                    placement=placed
                }
                return Student(userName, userEmail, userPhone, branch,rollNo, placement, description = description)
            }
            else {
                return Student("Not Loaded Properly", "Not Loaded Properly", "Not Loaded Properly", "Not Loaded Properly","Not Loaded Properly",false, description = "Not Loaded Properly")
            }
        } catch (e: Exception) {
            return Student("Some Error Occurred", "Some Error Occurred", "Some Error Occurred", "Some Error Occurred","Some Error Occurred",false, description = "Some Error Occurred")
        }
    }
}