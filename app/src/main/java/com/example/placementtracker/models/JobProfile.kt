package com.example.placementtracker.models

data class JobProfile(
    val jobProfileID: String, // You may use an auto-generated or custom job profile ID
    val title: String,
    val description: String,
    val skillsRequired: List<String>,
    val salary: Double
)

