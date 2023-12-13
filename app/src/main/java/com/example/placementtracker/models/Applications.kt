package com.example.placementtracker.models

import com.google.firebase.Timestamp

data class CompanyApplications(
    val jobId: String,
    val companyId:String,
    val startDate:Timestamp,
    val endDate:Timestamp,
    val appId:String
)
data class CompanyDataUploadApplications(
    val jobId: String,
    val companyId:String,
    val startDate:Timestamp,
    val endDate:Timestamp,
)
