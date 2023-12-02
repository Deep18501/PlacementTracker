package com.example.placementtracker.repository

import androidx.core.net.toUri
import com.example.placementtracker.models.Student
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class StudentRepository {
    private val firestore = FirebaseFirestore.getInstance()

//    fun uploadDocument(studentID: String, documentFile: File, documentType: String) {
//        // Generate a unique document ID, e.g., using Firebase's push() function.
//        val documentID = firestore.collection("students").document(studentID).collection("documents").document().id
//        val store=FirebaseStorage.getInstance()
//        // Upload the document file to Firebase Storage.
//        val storageRef = store.reference.child("documents").child(studentID).child("$documentID.$documentType")
//        storageRef.putFile(documentFile.toUri())
//            .addOnSuccessListener { taskSnapshot ->
//                // Document upload successful.
//                // Get the download URL of the uploaded document.
//                val downloadUrl = taskSnapshot.storage.downloadUrl.toString()
//
//                // Update the 'uploadedDocuments' field in Firestore.
//                firestore.collection("students").document(studentID)
//                    .update("uploadedDocuments", FieldValue.arrayUnion(downloadUrl))
//                    .addOnSuccessListener {
//                        // Update successful.
//                    }
//                    .addOnFailureListener { e ->
//                        // Handle update failure.
//                    }
//            }
//            .addOnFailureListener { e ->
//                // Handle document upload failure.
//            }
//    }

    fun retrieveStudentInfo(userID: String, callback: (Student?) -> Unit) {
        firestore.collection("students").document(userID)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val student = documentSnapshot.toObject(Student::class.java)
                    callback(student)
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener { e ->
                callback(null)
            }
    }

    fun updateStudentInfo(studentID: String, updatedStudent: Student) {
        firestore.collection("students").document(studentID)
            .set(updatedStudent)
            .addOnSuccessListener {
                // Update successful.
            }
            .addOnFailureListener { e ->
                // Handle update failure.
            }
    }
}