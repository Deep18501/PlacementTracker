package com.example.placementtracker.models

import android.net.Uri

data class Student(
    var name: String,
    val email: String,
    var phone: String="",
    val branch:String,
    val rollNo: String,
    var placed:Boolean=false,
    var companyId:String="",
    var description:String="Tell us about Yourself",
    var uploadedDocuments: Uri= Uri.EMPTY
)