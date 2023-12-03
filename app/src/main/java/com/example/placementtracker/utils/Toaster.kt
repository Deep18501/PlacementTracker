package com.example.placementtracker.utils

import android.content.Context
import android.widget.Toast

   public fun toaster(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
