package com.example.placementtracker.home_screens


import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.placementtracker.R
import com.example.placementtracker.Routes
import com.example.placementtracker.models.Statics
import com.example.placementtracker.ui.theme.SkyBlue
import com.example.placementtracker.ui.theme.backgroundGrey
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@Composable
fun HomeDashboard(
    navController: NavController
) {
    var dashboardData by remember {
        mutableStateOf<Statics>(Statics())
    }
    val studentCollection = "student"
    val companiesCollection = "companies"
    val applicationsCollection = "applications"
    val placementCollection = "placements"

    LaunchedEffect(dashboardData) {
        val numberOfStudent = getNumberOfDocuments(studentCollection)
        val numberOfCompanies = getNumberOfDocuments(companiesCollection)
        val numberOfApplications = getNumberOfDocuments(applicationsCollection)
        val numberOfPlacements = getNumberOfDocuments(placementCollection)
        println(numberOfApplications + numberOfCompanies + numberOfStudent + numberOfPlacements)
        dashboardData = Statics(
            numberOfStudent, numberOfCompanies, numberOfApplications, numberOfPlacements
        )
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(backgroundGrey)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(id = R.drawable.uni_logo),
                contentDescription = "uni logo",
                modifier = Modifier.size(50.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Placement Statistics",
                style = MaterialTheme.typography.headlineMedium,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        Divider()
        Text(
            text = "Deenbandhu Chhotu Ram University Of Science And Technology",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
        totalDetails(
            noStudent = dashboardData.numberOfStudent,
            noApp = dashboardData.numberOfApplications,
            noComp = dashboardData.numberOfCompanies,
            noPlace = dashboardData.numberOfPlacements
        )
        Text(
            text = "Total Placement Record",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
        CircularProgressBar(
            percent = dashboardData.numberOfPlacements.toFloat() / dashboardData.numberOfStudent.toFloat(),
            number = dashboardData.numberOfPlacements,
        ) {
            navController.navigate(Routes.STUDENTS_PLACED)
        }
    }
}


suspend fun getNumberOfDocuments(collectionPath: String): Int {
    val db = FirebaseFirestore.getInstance()

    try {
        val querySnapshot = db.collection(collectionPath)
            .get()
            .await()

        // Return the size of the documents in the collection
        return querySnapshot.size()
    } catch (e: Exception) {
        // Handle exceptions here
        e.printStackTrace()
    }

    // Return 0 if there's an issue
    return 0
}

@Preview
@Composable
fun test() {
    totalDetails(noStudent = 220, noApp = 148, noComp = 45, noPlace = 75)
}

@Composable
fun totalDetails(noStudent: Int, noApp: Int, noComp: Int, noPlace: Int) {

    Column(
        modifier = Modifier

            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            detCard(
                title = "Number Of Students",
                no = noStudent,
                icon = R.drawable.ic_total_students
            )
            detCard(
                title = "Number Of Companies",
                no = noComp,
                icon = R.drawable.ic_total_companies
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            detCard(
                title = "Number Of Applications",
                no = noApp,
                icon = R.drawable.ic_total_applications
            )
            detCard(
                title = "Number Of Offers Given",
                no = noPlace,
                icon = R.drawable.ic_total_placements
            )
        }
    }
}

@Composable
fun detCard(title: String, no: Int, icon: Int) {

    Card(
        modifier = Modifier
            .padding(8.dp)
            .size(width = 170.dp, height = 80.dp),
        elevation = CardDefaults.cardElevation(8.dp), colors = CardDefaults.cardColors(
            containerColor = Color.White,
        )
    ) {

        Text(
            text = title,
            maxLines = 1,
            fontSize = 13.sp,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(start = 10.dp, top = 8.dp)
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = "Icon",
                modifier = Modifier
                    .size(height = 80.dp, width = 80.dp)
                    .padding(8.dp)
            )
            Text(
                text = no.toString(),
                modifier = Modifier.padding(8.dp),
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}




@Composable
fun CircularProgressBar(
    percent: Float,
    number: Int,
    fontSize: TextUnit = 28.sp,
    radius: Dp = 130.dp,
    color: Color = Color.Blue,
    strokeWidth: Dp = 28.dp,
    animationDuration: Int = 1000,
    animationDelay: Int = 0,
    onClick: () -> Unit
) {
    var animationPlayed by remember {
        mutableStateOf(false)
    }

    // Ensure that percent is within the valid range (0 to 1)
    val validPercent =
        if (percent.isNaN() || percent < 0f) 0f else if (percent > 1f) 1f else percent

    val curPercent = animateFloatAsState(
        targetValue = if (animationPlayed) validPercent else 0f,
        animationSpec = tween(
            durationMillis = animationDuration,
            delayMillis = animationDelay
        )
    )

    LaunchedEffect(true) {
        animationPlayed = true
    }

    Box(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()
            .height(radius * 2f + 50.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(Color.White)
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(radius * 2f)) {
            drawArc(
                color = Color.LightGray,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(
                    strokeWidth.toPx(), cap = StrokeCap.Round
                )
            )
            drawArc(
                color = SkyBlue,
                startAngle = 0f,
                sweepAngle = 360 * curPercent.value,
                useCenter = false,
                style = Stroke(
                    strokeWidth.toPx(), cap = StrokeCap.Round
                )
            )

        }
        Text(
            text = "${(curPercent.value * 100).toInt()}%",
            color = Color.Black,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Placement Record",
            color = Color.Gray,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold, modifier = Modifier.offset(x=0.dp,y=40.dp)
        )
    }
}
