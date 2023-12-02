package com.example.placementtracker.models

data class Student(
    val userID: String,
    val name: String,
    val email: String,
    val phone: String,
    val rollNo: String,
    val uploadedDocuments: List<String>
)