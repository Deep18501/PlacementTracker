package com.example.placementtracker.home_screens

import android.graphics.Typeface.BOLD
import android.graphics.Typeface.DEFAULT_BOLD
import android.graphics.Typeface.ITALIC
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.placementtracker.R
import com.example.placementtracker.ui.theme.Bluecolor
import com.example.placementtracker.ui.theme.LightBlue
import java.net.URI

@Composable
fun CompanyInfo() {
    Column(modifier = Modifier
        .verticalScroll(rememberScrollState())
        .fillMaxSize()
        .background(Color.Red)){
        Image(painter = painterResource(id = R.drawable.trial_company),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview
@Composable
fun CompanyList(){

    LazyColumn(Modifier.padding(8.dp) ){
        items(5){

//            CompanyCard()
        }
    }
}


@Composable
fun CompanyCard(
    companyName:String,
    companyService:String,
    companyPackage:String,
    applied:Boolean,
    appliedDate:String,
    companyLogo: Uri
) {

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp,4.dp)
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
                Text(text = stringResource(id = R.string.Rs)+companyPackage, color = Bluecolor, fontWeight = FontWeight(400), fontSize = 14.sp)
                if (applied){
                    Row (verticalAlignment = Alignment.CenterVertically){
                     Text(text = "Applied", color = Color.Blue)
                        Spacer(modifier = Modifier.width(5.dp))
                        Icon(Icons.Default.Done, contentDescription = "Applies")
                    }
                }else{
                    Row (verticalAlignment = Alignment.CenterVertically){
                        Text(text = "Not Applied", color = Color.Red)
                        Spacer(modifier = Modifier.width(5.dp))
                        Icon(Icons.Default.Clear, contentDescription = "Applies")
                    }
                }
            }
            Column (modifier = Modifier
                .weight(0.3f)
                .padding(8.dp, 8.dp, 0.dp, 0.dp),
                horizontalAlignment = Alignment.CenterHorizontally){
                Image(painter = rememberImagePainter(data = companyLogo), contentDescription = "Company pic", modifier = Modifier.height(34.dp))
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Applied on", fontSize = 10.sp)
                Text(text = appliedDate, color = Color.Green)
            }
        }
    }
}

