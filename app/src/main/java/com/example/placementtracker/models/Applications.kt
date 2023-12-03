package com.example.placementtracker.models

import com.google.firebase.Timestamp
import com.google.firebase.ktx.Firebase

data class CompanyApplications(
    val jobProfileId: String,
    val companyId:String,
    val startDate:Timestamp,
    val endDate:Timestamp
)
