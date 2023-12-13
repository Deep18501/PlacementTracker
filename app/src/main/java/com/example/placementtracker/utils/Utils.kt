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
class Constants {
    companion object{
        const val NOTIFICATION_TAG="Notication all data"
        const val BASE_URL="https://fcm.googleapis.com"
        const val SERVER_KEY="AAAAythlACw:APA91bFE0OJnJYq67mHrhKJjvJirZU9Dgo2y71TXLdYBL5buNyyyUkolY6qBJhSXDfBbMRHyDjnuoJjVfad_I0S9Gn0-t9CnuWvvdig05uXn122IOpHCFq-Bd23x7B1c_I7VnGOAOGJw"
        const val CONTENT_TYPE="application/json"
    }
}