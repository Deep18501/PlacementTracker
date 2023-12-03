package com.example.placementtracker.home_screens

import android.content.Context
import android.net.Uri
import android.util.Log
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.placementtracker.R
import com.example.placementtracker.models.CompaniesListInfo
import com.example.placementtracker.models.Company
import com.example.placementtracker.models.CompanyApplications
import com.example.placementtracker.models.JobProfile
import com.example.placementtracker.ui.theme.Bluecolor
import com.example.placementtracker.ui.theme.LightBlue
import com.example.placementtracker.utils.formatDate
import com.example.placementtracker.utils.getCurrentTimestamp
import com.example.placementtracker.utils.toaster
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.util.Date

@Composable
fun CompanyInfo() {
    val context= LocalContext.current

    var allApplications by remember { mutableStateOf<List<CompanyApplications>?>(null) }
    var companiesListInfo by remember { mutableStateOf<List<CompaniesListInfo>?>(null) }

    LaunchedEffect(companiesListInfo){
        allApplications= fetchApplications(context)
        Log.d("Loading application",allApplications.toString())
        companiesListInfo= fetchCompList(context, allApplications!!)
    }
    Column {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.uni_logo),
                contentDescription = "uni logo",
                modifier = Modifier.size(50.dp)
            )

            Text(
                text = "Companies",
                style = MaterialTheme.typography.displaySmall,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight(20),
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        companiesListInfo?.let { CompanyLazyList(companiesListInfo = it) }
    }
}

@Composable
fun CompanyLazyList(companiesListInfo: List<CompaniesListInfo>){
    var companiesListCurrent=mutableListOf<CompaniesListInfo>()
    var upcomingListCompany=mutableListOf<CompaniesListInfo>()
    companiesListInfo.forEach{
        if(it.applications.endDate.compareTo(getCurrentTimestamp())>=0 && it.applications.startDate.compareTo(getCurrentTimestamp())<0){
            companiesListCurrent.add(it)
        }
        if(it.applications.startDate.compareTo(getCurrentTimestamp())>=0){
            upcomingListCompany.add(it)
        }
    }

    LazyRow(Modifier.padding(8.dp)){
        items(upcomingListCompany.size){
            val upcomingList=upcomingListCompany[it]
            CompanyRowCard(
                companyName = upcomingList.company.name,
                companyService = upcomingList.company.industry,
                companyPackage = upcomingList.jobProfile.salary,
                applicationStart = upcomingList.applications.startDate.toDate(),
                companyId = upcomingList.applications.companyId
            )
        }
    }
    LazyColumn(Modifier.padding(8.dp) ){
        items(companiesListCurrent.size){
            val currentComp=companiesListCurrent[it]
            currentComp.applications.endDate.toDate().let { it1 ->
                currentComp.jobProfile.skillsRequired?.let { it2 ->
                    CompanyCard(
                        companyName = currentComp.company.name,
                        companyService = currentComp.company.industry,
                        companyPackage =currentComp.jobProfile.salary ,
                        applied = false,
                        applicationStart = currentComp.applications.startDate.toDate(),
                        applicationEnd = it1,
                        companyId = currentComp.applications.companyId,
                        jobName = currentComp.jobProfile.title,
                        jobSkills = it2
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun tester() {
    val context= LocalContext.current
//
//    var allApplications by remember { mutableStateOf<List<CompanyApplications>?>(null) }
//    var companiesListInfo by remember { mutableStateOf<List<CompaniesListInfo>?>(null) }
//
//    LaunchedEffect(companiesListInfo){
//        allApplications= fetchApplications(context)
////        Log.d("Loading application",allApplications.toString())
//        companiesListInfo= fetchCompList(context, allApplications!!)
//        Log.d("Loading application",companiesListInfo.toString())
//    }


}
suspend fun fetchApplications(context: Context):List<CompanyApplications>{
    val applicationsList = mutableListOf<CompanyApplications>()
    val userCollection = Firebase.firestore.collection("applications")

    try {
        val querySnapshot=userCollection.get().await()
        for (document in querySnapshot.documents) {
            val jobProfileId = document.getString("jobId") ?: ""
            val companyId = document.getString("companyId") ?: ""
            val startDate = document.getTimestamp("startDate") ?: Timestamp.now()
            val endDate = document.getTimestamp("endDate")

            val companyApplication = endDate?.let {
                CompanyApplications(
                    jobProfileId = jobProfileId,
                    companyId = companyId,
                    startDate = startDate,
                    endDate = it
                )
            }

            if (companyApplication != null) {
                applicationsList.add(companyApplication)
            }
        }
    }catch (e:Exception){
        e.printStackTrace()
        toaster(context,e.toString())
    }
        return applicationsList

}

suspend fun fetchCompList(context:Context,applicationsList:List<CompanyApplications>):MutableList<CompaniesListInfo>{
    val companyListInfo=mutableListOf<CompaniesListInfo>()

    var CollectionJobs = Firebase.firestore.collection("jobProfiles")
    var CollectionCompanies = Firebase.firestore.collection("companies")
    try {

        applicationsList.forEach { appli ->

            val documentSnapshot = CollectionCompanies.document(appli.companyId).get().await()
            val jobSnapshot=CollectionJobs.document(appli.jobProfileId).get().await()
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
                    var jobProfile1 =JobProfile("","","","")
                        jobTitle?.let {
                            if (jobDescription != null) {
                                if (jobSalary != null) {
                                    jobProfile1=JobProfile(it, jobDescription, jobSkills, jobSalary)
                                }
                            }
                        }
                    companyListInfo.add(CompaniesListInfo(jobProfile1, company, appli))
                }
            }
        }
        return companyListInfo

    }catch (e:Exception){
        e.printStackTrace()
        toaster(context,e.toString())
        return companyListInfo
    }
}


suspend fun fetchImageUrlFromFolder(context: Context, companyId: String): Uri? {
    val resourceId =
        context.resources.getIdentifier("ic_dept", "drawable", context.packageName)
    val defaultImageUri = Uri.parse("android.resource://${context.packageName}/$resourceId")
    try {
        val storageRef = Firebase.storage.reference
        val imageRef = storageRef.child("/companies/$companyId.png")

        // Download URL from Firebase Storage
        val imageUrl = imageRef.downloadUrl.await()

        // Return the Uri obtained from the download URL
        return Uri.parse(imageUrl.toString())

    } catch (e: Exception) {
        Log.d("Error image", "Error is $e")
        return defaultImageUri
    }
}


@Composable
fun CompanyCard(
    companyName:String,
    companyService:String,
    companyPackage:String,
    applied:Boolean,
    applicationStart:Date,
    applicationEnd:Date,
    companyId:String,
    jobName:String,
    jobSkills:String
) {
    val context= LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    LaunchedEffect(true) {
        println("ImageUri is " + "Corutins Scope")
        imageUri = fetchImageUrlFromFolder(context, companyId)
    }
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp, 4.dp)
        .border(1.dp, color = LightBlue, shape = RoundedCornerShape(8.dp))
        .clip(RoundedCornerShape(8.dp))
        .background(Color.White)
         ){
        val formattedsDate = formatDate(applicationStart)
        val formattedeDate = formatDate(applicationEnd)

        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp), horizontalArrangement = Arrangement.SpaceBetween){
            Column(modifier = Modifier.weight(0.7f)){
            Text(text = companyName, color = Bluecolor, fontWeight = FontWeight(600), fontSize = 20.sp,maxLines=1, overflow = TextOverflow.Ellipsis)
                Text(text = companyService)
                Text(text = jobName)
                Text(text = stringResource(id = R.string.Rs)+companyPackage, color = Bluecolor, fontWeight = FontWeight(400), fontSize = 14.sp)
                Text(text = "Skills :- "+jobSkills)
//                if (applied){
//                    Row (verticalAlignment = Alignment.CenterVertically){
//                     Text(text = "Applied", color = Color.Blue)
//                        Spacer(modifier = Modifier.width(5.dp))
//                        Icon(Icons.Default.Done, contentDescription = "Applies")
//                    }
//                }else{
//                    Row (verticalAlignment = Alignment.CenterVertically){
//                        Text(text = "Not Applied", color = Color.Red)
//                        Spacer(modifier = Modifier.width(5.dp))
//                        Icon(Icons.Default.Clear, contentDescription = "Applies")
//                    }
//                }



            }
            Column (modifier = Modifier
                .weight(0.3f)
                .padding(8.dp, 8.dp, 0.dp, 0.dp),
                horizontalAlignment = Alignment.CenterHorizontally){
                Image(painter = rememberImagePainter(data = imageUri), contentDescription = "Company pic", modifier = Modifier.height(34.dp))
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Started On" ,fontSize = 10.sp)
                Text(text = formattedsDate, color = Color.Green)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "End on", fontSize = 10.sp)
                Text(text = formattedeDate, color = Color.Red)
            }
        }
    }
}


@Composable
fun CompanyRowCard(
    companyName:String,
    companyService:String,
    companyPackage:String,
    applicationStart:Date,
    companyId:String
) {
    val context= LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    LaunchedEffect(true) {
        println("ImageUri is " + "Corutins Scope")
        imageUri = fetchImageUrlFromFolder(context, companyId)
    }
    Box(modifier = Modifier
        .width(350.dp)
        .padding(8.dp, 4.dp)
        .border(1.dp, color = LightBlue, shape = RoundedCornerShape(8.dp))
        .clip(RoundedCornerShape(8.dp))
        .background(Color.White)
         ){
        val formattedsDate = formatDate(applicationStart)
        Row (modifier = Modifier
            .padding(10.dp), horizontalArrangement = Arrangement.SpaceEvenly){
            Column(modifier = Modifier.weight(0.7f)){
            Text(text = companyName, color = Bluecolor, fontWeight = FontWeight(600), fontSize = 20.sp,maxLines=1, overflow = TextOverflow.Ellipsis)
                Text(text = companyService)
                Text(text = stringResource(id = R.string.Rs)+companyPackage, color = Bluecolor, fontWeight = FontWeight(400), fontSize = 14.sp)
            }
            Column (modifier = Modifier
                .weight(0.3f)
                .padding(8.dp, 8.dp, 0.dp, 0.dp),
                horizontalAlignment = Alignment.CenterHorizontally){
                Image(painter = rememberImagePainter(data = imageUri), contentDescription = "Company pic", modifier = Modifier.height(34.dp))
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Started On" ,fontSize = 10.sp)
                Text(text = formattedsDate, color = Color.Green, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

