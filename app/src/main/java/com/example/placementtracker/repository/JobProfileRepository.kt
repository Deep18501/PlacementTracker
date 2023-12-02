package com.example.placementtracker.repository

import com.example.placementtracker.models.JobProfile
import com.google.firebase.firestore.FirebaseFirestore

class JobProfileRepository {

    private val firestore = FirebaseFirestore.getInstance()

    // Function to retrieve a job profile from Firestore by jobProfileID
    fun retrieveJobProfile(jobProfileID: String, callback: (JobProfile?) -> Unit) {
        firestore.collection("jobProfiles").document(jobProfileID)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val jobProfile = documentSnapshot.toObject(JobProfile::class.java)
                    callback(jobProfile)
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener { e ->
                callback(null)
            }
    }

    // Function to update a job profile in Firestore
    fun updateJobProfile(jobProfileID: String, updatedJobProfile: JobProfile) {
        firestore.collection("jobProfiles").document(jobProfileID)
            .set(updatedJobProfile)
            .addOnSuccessListener {
                // Update successful.
            }
            .addOnFailureListener { e ->
                // Handle update failure.
            }
    }

    // Function to retrieve all job profiles from Firestore
    fun retrieveAllJobProfiles(callback: (List<JobProfile>) -> Unit) {
        firestore.collection("jobProfiles")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val jobProfiles = querySnapshot.documents.mapNotNull { doc ->
                    doc.toObject(JobProfile::class.java)
                }
                callback(jobProfiles)
            }
            .addOnFailureListener { e ->
                callback(emptyList())
            }
    }

    // Function to upload a new job profile to Firestore
    fun uploadJobProfile(jobProfile: JobProfile) {
        firestore.collection("jobProfiles")
            .add(jobProfile)
            .addOnSuccessListener { documentReference ->
                // Upload successful.
            }
            .addOnFailureListener { e ->
                // Handle upload failure.
            }
    }
}
