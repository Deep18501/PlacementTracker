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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.placementtracker.R
import com.example.placementtracker.ui.theme.Bluecolor
import com.example.placementtracker.ui.theme.LightBlue
import com.example.placementtracker.utils.formatDate
import java.util.Date

@Composable
fun Opportunities() {

    Column {
        Row(Modifier.padding(8.dp)) {

        Image(painter = painterResource(id = R.drawable.uni_logo), contentDescription = "uni logo", modifier = Modifier.size(50.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "My Applications",
            style = MaterialTheme.typography.displaySmall,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight(20),
            modifier = Modifier.padding(start = 16.dp)
        )
        }
        CompanyCard(
            companyName = "Microsoft",
            companyService = "IT",
            companyPackage = "8 lakh/annum",
            companyId = "1",
            jobName = "Web Developer",
            jobSkills = "Html,CSS and Js"
        )
        CompanyCard(
            companyName = "Reliance Digital",
            companyService = "Telecom",
            companyPackage = "7 lakh/annum",
            companyId = "3",
            jobName = "Software Developer",
            jobSkills = "C++"
        )
        CompanyCard(
            companyName = "Amazon",
            companyService = "Cloud",
            companyPackage = "11 lakh/annum",
            companyId = "4",
            jobName = "Cloud Technical",
            jobSkills = "Cloud Computing"
        )
    }
}

@Composable
fun CompanyCard(
    companyName:String,
    companyService:String,
    companyPackage:String,
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

        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp), horizontalArrangement = Arrangement.SpaceBetween){
            Column(modifier = Modifier.weight(0.7f)){
                Text(text = companyName, color = Bluecolor, fontWeight = FontWeight(600), fontSize = 20.sp,maxLines=1, overflow = TextOverflow.Ellipsis)
                Text(text = companyService)
                Text(text = jobName)
                Text(text = stringResource(id = R.string.Rs) +companyPackage, color = Bluecolor, fontWeight = FontWeight(400), fontSize = 14.sp)
                Text(text = "Skills :- "+jobSkills)
//                if (applied){
                    Row (verticalAlignment = Alignment.CenterVertically){
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
            Column (modifier = Modifier
                .weight(0.3f)
                .padding(8.dp, 8.dp, 0.dp, 0.dp),
                horizontalAlignment = Alignment.CenterHorizontally){
                Image(painter = rememberImagePainter(data = imageUri), contentDescription = "Company pic", modifier = Modifier.height(40.dp))
                Spacer(modifier = Modifier.height(4.dp))

            }
        }
    }
}
