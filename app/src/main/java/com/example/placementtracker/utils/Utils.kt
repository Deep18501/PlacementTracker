package com.example.placementtracker.utils

import android.content.Context
import android.widget.Toast
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Date

fun toaster(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
fun formatDate(date: Date): String {
    val format = SimpleDateFormat("dd/MM/yyyy")
    return format.format(date)
}
fun getCurrentTimestamp(): Timestamp {
    return Timestamp.now()
}
