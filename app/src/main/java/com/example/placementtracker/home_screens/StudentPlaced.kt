package com.example.placementtracker.home_screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.placementtracker.R
import com.example.placementtracker.models.Placement
import com.example.placementtracker.ui.theme.backgroundGrey
import com.example.placementtracker.utils.toaster
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.Locale

enum class SortOrder {
    NONE,
    ASCENDING,
    DESCENDING
}
enum class SortableColumns {
    NAME,
    ROLL_NO,
    PACKAGE,
    YEAR
}
@Composable
fun StudentPlaced() {
    val context= LocalContext.current
    var allPlacement by  remember { mutableStateOf<List<Placement>?>(null) }
    var sortOrder by remember { mutableStateOf(SortOrder.NONE) }
    var sortByType by remember { mutableStateOf(SortableColumns.NAME) }

    var isDropdownMenuExpanded by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(allPlacement){
        allPlacement= fetchAllPlacement(context)
        Log.d("Loading application",allPlacement.toString())
    }

    allPlacement?.let {
        sortBy(
            placementList = it,
            column = sortByType,
            order = sortOrder
        )
    }
    Column(modifier = Modifier
        .fillMaxSize()
        .background(backgroundGrey)) {
        Row(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.uni_logo),
                contentDescription = "uni logo",
                modifier = Modifier.size(50.dp)
            )

            Text(
                text = "All Placements",
                style = MaterialTheme.typography.headlineLarge,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        Divider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Sort by:", style = MaterialTheme.typography.bodyLarge)

            // Drop-down menu for sorting variables
            Box(
                modifier = Modifier
                    .clickable {
                        isDropdownMenuExpanded = !isDropdownMenuExpanded
                    }
            ) {
                Text(text = sortByType.name.lowercase(Locale.getDefault())) // Display the selected column

                DropdownMenu(
                    expanded = isDropdownMenuExpanded,
                    onDismissRequest = { isDropdownMenuExpanded = false },
                ) {
                    // List of sorting options
                    SortableColumns.values().forEach { column ->
                        DropdownMenuItem(onClick = {
                            sortByType=column
                            isDropdownMenuExpanded = false
                        }, text =  {
                            Text(text = column.name)
                        })
                    }
                }
            }

            // Buttons for ascending/descending order
            Row {
                Button(
                    onClick = {
                        sortOrder = SortOrder.ASCENDING
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = if (sortOrder == SortOrder.ASCENDING) Color.White else backgroundGrey), // Customize colors
                    modifier = Modifier
                        .size(80.dp, 40.dp)
                ) {
                    Text(text = "Ascending", color = Color.Black)
                }
                Button(
                    onClick = {
                        sortOrder = SortOrder.DESCENDING
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = if (sortOrder == SortOrder.DESCENDING) Color.White else backgroundGrey), // Customize colors
                    modifier = Modifier
                        .size(80.dp, 40.dp)
                ) {
                    Text(text = "Descending", color = Color.Black)
                }
            }
        }
        Divider()
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            allPlacement?.let { placements ->
                items(placements.size) { i ->
                    val placement = placements[i]
                    StudentPlacedCard(
                        name = placement.name,
                        rollNo = placement.rollNo,
                        company = placement.compName,
                        packageGot = placement.packageGot,
                        yearOfPlacement = placement.year
                    )
                }
            } ?: items(0) {
                Text(text = "Loading placements...")
            }
        }
    }
}

@Composable
fun StudentPlacedCard(
    name:String,
    rollNo:String,
    company:String,
    packageGot:String,
    yearOfPlacement:String
){
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Name:",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = name,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Roll No:", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
                Text(text = rollNo, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Company:", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
                Text(text = company.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Package:", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
                Text(text = packageGot, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Year of Placement:", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
                Text(text = yearOfPlacement, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
            }
        }
    }
}

suspend fun fetchAllPlacement(context: Context): List<Placement> {
    val applicationsList = mutableListOf<Placement>()
    val userCollection = Firebase.firestore.collection("studentPlaced")

    try {
        val querySnapshot=userCollection.get().await()
        for (document in querySnapshot.documents) {
            val compName = document.getString("compName") ?: ""
            val packageGot = document.getString("package") ?: ""
            val name = document.getString("name") ?: ""
            val year = document.getString("year")?:""
            val rollNo=document.id

            val placement =Placement(
                name, rollNo = rollNo,compName,packageGot,year
            )

            applicationsList.add(placement)
        }
    }catch (e:Exception){
        e.printStackTrace()
    }
    return applicationsList

}

private fun sortBy(placementList: List<Placement>, column: SortableColumns, order: SortOrder): List<Placement> {
    return when (column) {
        SortableColumns.NAME -> {
            if (order == SortOrder.ASCENDING) {
                placementList.sortedBy { it.name }
            } else {
                placementList.sortedByDescending { it.name }
            }
        }
        SortableColumns.ROLL_NO -> {
            if (order == SortOrder.ASCENDING) {
                placementList.sortedBy { it.rollNo }
            } else {
                placementList.sortedByDescending { it.rollNo }
            }
        }
        SortableColumns.PACKAGE -> {
            if (order == SortOrder.ASCENDING) {
                placementList.sortedBy { it.packageGot }
            } else {
                placementList.sortedByDescending { it.packageGot }
            }
        }
        SortableColumns.YEAR -> {
            if (order == SortOrder.ASCENDING) {
                placementList.sortedBy { it.year.toInt() }
            } else {
                placementList.sortedByDescending { it.year.toInt() }
            }
        }


    }
}
