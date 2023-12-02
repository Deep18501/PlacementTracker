package com.example.placementtracker.repository

import android.icu.text.SimpleDateFormat
import com.example.placementtracker.models.Company
import com.example.placementtracker.models.JobProfile
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date

class CompanyRepository {

    private val firestore = FirebaseFirestore.getInstance()

    // Function to retrieve company information from Firestore by companyID
    fun retrieveCompanyInfo(companyID: String, callback: (Company?) -> Unit) {
        firestore.collection("companies").document(companyID)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val company = documentSnapshot.toObject(Company::class.java)
                    callback(company)
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener { e ->
                callback(null)
            }
    }

    // Function to update company information in Firestore
    fun updateCompanyInfo(companyID: String, updatedCompany: Company) {
        firestore.collection("companies").document(companyID)
            .set(updatedCompany)
            .addOnSuccessListener {
                // Update successful.
            }
            .addOnFailureListener { e ->
                // Handle update failure.
            }
    }

    // Function to retrieve job profiles for a company from Firestore
    fun retrieveJobProfilesForCompany(companyID: String, callback: (List<JobProfile>) -> Unit) {
        firestore.collection("companies").document(companyID)
            .collection("jobProfiles")
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

    // Function to upload a new job profile for a company
    fun uploadJobProfile(companyID: String, jobProfile: JobProfile) {
        firestore.collection("companies").document(companyID)
            .collection("jobProfiles")
            .add(jobProfile)
            .addOnSuccessListener { documentReference ->
                // Upload successful.
            }
            .addOnFailureListener { e ->
                // Handle upload failure.
            }
    }
    fun retrieveUpcomingCompanies(callback: (List<Company>) -> Unit) {
        val today = getCurrentDate() // You need to implement a function to get the current date.

        // Query companies with placement dates after today
        firestore.collection("companies")
            .whereGreaterThan("placementDate", today)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val upcomingCompanies = querySnapshot.documents.mapNotNull { doc ->
                    doc.toObject(Company::class.java)
                }
                callback(upcomingCompanies)
            }
            .addOnFailureListener { e ->
                callback(emptyList())
            }
    }
    fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy") // Adjust the date format to match your Firestore data format
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }
}
