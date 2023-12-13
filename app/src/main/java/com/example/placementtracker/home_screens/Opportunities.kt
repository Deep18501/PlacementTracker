package com.example.placementtracker.home_screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.placementtracker.R
import com.example.placementtracker.models.CompaniesListInfo
import com.example.placementtracker.models.Company
import com.example.placementtracker.models.CompanyApplications
import com.example.placementtracker.models.JobProfile
import com.example.placementtracker.ui.theme.Bluecolor
import com.example.placementtracker.ui.theme.LightBlue
import com.example.placementtracker.ui.theme.backgroundGrey
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

@Composable
fun Opportunities() {
    var companiesListInfo by remember { mutableStateOf<List<CompaniesListInfo>?>(null) }
    val context = LocalContext.current
    val currentUser = Firebase.auth.currentUser?.uid
    LaunchedEffect(companiesListInfo) {
        companiesListInfo= currentUser?.let { getAllApplicationOfStudent(it) }
    }

    Column (modifier = Modifier
        .fillMaxSize()
        .background(backgroundGrey)){
        Row(
            Modifier
                .background(Color.White)
                .padding(8.dp)
                .fillMaxWidth()
            , verticalAlignment = Alignment.CenterVertically) {

            Image(
                painter = painterResource(id = R.drawable.uni_logo),
                contentDescription = "uni logo",
                modifier = Modifier.size(50.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "My Applications",
                style = MaterialTheme.typography.headlineLarge,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        Divider()
        LazyColumn(modifier = Modifier.fillMaxSize()){
            companiesListInfo?.let {
                println("all app size"+it.size)
                items(it.size){i->
                    val select= companiesListInfo!![i]
                    select.jobProfile.skillsRequired?.let { it1 ->
                        CompanyCard(
                            companyName =select.company.name,
                            companyService = select.company.industry,
                            companyPackage = select.jobProfile.salary,
                            companyId = select.applications.companyId,
                            jobName = select.jobProfile.title,
                            jobSkills = it1
                        )
                    }
                }
            }
        }

    }
}

@Composable
fun CompanyCard(
    companyName: String,
    companyService: String,
    companyPackage: String,
    companyId: String,
    jobName: String,
    jobSkills: String
) {
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    LaunchedEffect(true) {
        println("ImageUri is " + "Corutins Scope")
        imageUri = fetchImageUrlFromFolder(context, companyId)
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp, 4.dp)
            .border(1.dp, color = LightBlue, shape = RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(0.7f)) {
                Text(
                    text = companyName,
                    color = Bluecolor,
                    fontWeight = FontWeight(600),
                    fontSize = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(text = companyService)
                Text(text = jobName)
                Text(
                    text = stringResource(id = R.string.Rs) + companyPackage,
                    color = Bluecolor,
                    fontWeight = FontWeight(400),
                    fontSize = 14.sp
                )
                Text(text = "Skills :- $jobSkills")
//                if (applied){
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Applied", color = Color.Blue)
                    Spacer(modifier = Modifier.width(5.dp))
                    Icon(Icons.Default.Done, contentDescription = "Applies")
                }
//                }else{
//                    Row (verticalAlignment = Alignment.CenterVertically){
//                        Text(text = "Not Applied", color = Color.Red)
//                        Spacer(modifier = Modifier.width(5.dp))
//                        Icon(Icons.Default.Clear, contentDescription = "Applies")
//                    }
//                }


            }
            Column(
                modifier = Modifier
                    .weight(0.3f)
                    .padding(8.dp, 8.dp, 0.dp, 0.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = imageUri),
                    contentDescription = "Company pic",
                    modifier = Modifier.height(40.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))

            }
        }
    }
}


suspend fun getAllApplicationOfStudent(uid: String): List<CompaniesListInfo> {
    val companyListInfo = mutableListOf<CompaniesListInfo>()

    val collection = Firebase.firestore.collection("studentApplications")
    try {
        val documentSnapshot = collection.document(uid).get().await()
        if (documentSnapshot.exists()){
        var allApp = documentSnapshot.get("applicationId") as? MutableList<String>
            println("all application are"+allApp)
        if (allApp != null) {

            val userCollection = Firebase.firestore.collection("applications")

            allApp.forEach {
                val document = userCollection.document(it).get().await()
                val jobProfileId = document.getString("jobId") ?: ""
                val companyId = document.getString("companyId") ?: ""
                val startDate = document.getTimestamp("startDate") ?: Timestamp.now()
                val endDate = document.getTimestamp("endDate")
                val appId = document.id
                val companyApplication = endDate?.let {
                    CompanyApplications(
                        jobId = jobProfileId,
                        companyId = companyId,
                        startDate = startDate,
                        endDate = it,
                        appId = appId
                    )
                }

                var CollectionJobs = Firebase.firestore.collection("jobProfiles")
                var CollectionCompanies = Firebase.firestore.collection("companies")
                val documentSnapshot = CollectionCompanies.document(companyId).get().await()
                val jobSnapshot = CollectionJobs.document(jobProfileId).get().await()
                    if (documentSnapshot.exists()) {
                        if (jobSnapshot.exists()) {
                            val compIndustry = documentSnapshot.getString("industry").toString()
                            val compName = documentSnapshot.getString("name").toString()
                            val description = documentSnapshot.getString("description").toString()
                            val jobTitle = jobSnapshot.getString("title")
                            val jobDescription = jobSnapshot.getString("description")
                            val jobSkills = jobSnapshot.getString("skills")
                            val jobSalary = jobSnapshot.getString("salary")
                            val company = Company(compName, compIndustry, description)
                            var jobProfile1 = JobProfile("", "", "", "")
                            jobTitle?.let {
                                if (jobDescription != null) {
                                    if (jobSalary != null) {
                                        jobProfile1 =
                                            JobProfile(it, jobDescription, jobSkills, jobSalary)
                                    }
                                }
                            }
                            companyApplication?.let { it1 ->
                                CompaniesListInfo(jobProfile1, company,
                                    it1
                                )
                            }?.let { it2 -> companyListInfo.add(it2) }
                        }

                }
                println("Before return size"+companyListInfo.size)
            }
                return companyListInfo
        }}
        return companyListInfo
    } catch (e: Exception) {
        println("error is : " + e)
        e.printStackTrace()
        return companyListInfo

    }

}