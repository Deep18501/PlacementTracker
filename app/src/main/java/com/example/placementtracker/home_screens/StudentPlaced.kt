package com.example.placementtracker.home_screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.magnifier
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.placementtracker.R
import com.example.placementtracker.models.Student

@Composable
fun StudentPlaced() {
    Column {
        Row(Modifier.padding(8.dp)) {


            Image(
                painter = painterResource(id = R.drawable.uni_logo),
                contentDescription = "uni logo",
                modifier = Modifier.size(50.dp)
            )
        Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "All Placements",
                style = MaterialTheme.typography.displaySmall,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight(20),
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(text ="Name", fontSize = 10.sp, fontWeight = FontWeight(80))
            Text(text = "RollNo", fontSize = 10.sp, fontWeight = FontWeight(80))
            Text(text = "Company", fontSize = 10.sp, fontWeight = FontWeight(70))
//            Text(text = "Year", fontSize = 10.sp, fontWeight = FontWeight(80))
        }
     repeat (10){i->
        studentPlacedCard(name = studentName[i], rollNo = rollNo[i], company = compName[i], year = year[i] )
    }
    }
}

@Composable
fun studentPlacedCard(
    name:String,
    rollNo:String,
    company:String,
    year:Int
){
    Row (
       modifier = Modifier
           .fillMaxWidth()
           .padding(horizontal = 10.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween
    ){
    Text(text = name, fontSize = 16.sp, fontWeight = FontWeight(20), color = Color.Black)
    Text(text = rollNo, fontSize = 16.sp, fontWeight = FontWeight(20), color = Color.Black)
    Text(text = company, fontSize = 16.sp, fontWeight = FontWeight(20), color = Color.Black)
//    Text(text = year.toString(), fontSize = 16.sp, fontWeight = FontWeight(20))
    }
}

val studentName=arrayOf(
        "ABHISHEK",
        "SHIKHAR",
        "AKASH",
        "APOORV",
        "AMISHA",
        "CHETNA",
        "VIKRAM",
        "ANIKET",
        "DEEPANSHU",
        "SIMAR",
        "ANANYE",
        "JHANVI",
)

val rollNo= arrayOf(
    "19001002002",
    "19001002054",
    "19001002005",
    "19001002009",
    "18001002007",
    "18001002015",
    "18001002059",
    "18001002010",
    "18001002017",
    "19001002050",
    "18001002010",
    "19001002023",
)
val compName= arrayOf(
            "ISGEC LTD",
            "BNY MELLON",
            "HAVELLS LTD",
            "AVIONICS LTD",
            "BNY MELLON",
            "INTELLIPAT",
            "INTELLIPAT",
            "MICROSOFT",
            "GOOGLE",
            "DELHIVERY",
            "JPMC",
            "HAVELLS LTD",
)

val year= arrayOf(
    2023,
    2023,
    2023,
    2023,
    2022,
    2022,
    2022,
    2022,
    2022,
    2023,
    2022,
    2023,
)