package com.example.placementtracker.home_screens

import android.content.Context
import android.graphics.Typeface
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.placementtracker.R
import com.example.placementtracker.Routes
import com.example.placementtracker.models.Student
import com.example.placementtracker.ui.theme.DarkBlue
import com.example.placementtracker.ui.theme.LightBlue
import com.example.placementtracker.ui.theme.whiteGrey
import com.example.placementtracker.utils.toaster
import com.example.placementtracker.viewmodels.HomeScreenViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun StudentInfo(
    viewModel: HomeScreenViewModel,
    navController:NavHostController
) {
    val context = LocalContext.current
    var loadProfile by remember { mutableIntStateOf(0) }
    var loadProfileDetails by remember { mutableIntStateOf(0) }
    var user by remember { mutableStateOf<Student?>(null) }
    var userProfile by remember { mutableStateOf<Student?>(null) }
    var userCollection = Firebase.firestore.collection("student")

    LaunchedEffect(loadProfileDetails) {
        println("ImageUri is " + "Corutins Scope")

        user = viewModel.loadPersonDet()
        userProfile = user
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .background(whiteGrey)
    ) {
    Row(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
        Image(painter = painterResource(id = R.drawable.uni_logo), contentDescription = "uni logo", modifier = Modifier.size(50.dp))

        Text(
            text = "Student Profile",
            style = MaterialTheme.typography.displaySmall,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight(20),
            modifier = Modifier.padding(start = 16.dp)
        )
    }
        userProfile?.let {
            ProfileInfoItem(
                viewModel = viewModel,
                context = context,
                icon = Icons.Default.Person,
                title = "Name",
                value = it.name,
                edit = true,
                user = it,
                field = 0,
            ) {
                loadProfileDetails += 1
            }
            Spacer(modifier = Modifier.height(2.dp))

            ProfileInfoItem(
                viewModel = viewModel,
                context = context,
                icon = Icons.Default.Email,
                title = "Email",
                value = it.email,
                edit = false,
                user = user!!,
                field = 1,
            ) {
                loadProfileDetails += 1
            }
            Spacer(modifier = Modifier.height(2.dp))
            ProfileInfoItem(
                viewModel = viewModel,
                context = context,
                icon = Icons.Default.Phone,
                title = "Phone",
                value = it.phone,
                edit = true,
                user = user!!,
                field = 2,
            ) {
                loadProfileDetails += 1
            }

            Spacer(modifier = Modifier.height(2.dp))
            ProfileInfoItem(
                viewModel = viewModel,
                context = context,
                icon = Icons.Default.ArrowForward,
                title = "Roll No.",
                value = it.rollNo,
                edit = false,
                user = user!!,
                field = 3,
            ) {
                loadProfileDetails += 1
            }

            Spacer(modifier = Modifier.height(2.dp))

            ProfileInfoItem(
                viewModel = viewModel,
                context = context,
                icon = Icons.Default.ArrowForward,
                title = "Branch",
                value = it.branch,
                edit = false,
                user = user!!,
                field = 4,
            ) {
                loadProfileDetails += 1
            }
            Spacer(modifier = Modifier.height(2.dp))

            ProfileInfoItem(
                viewModel = viewModel,
                context = context,
                icon = Icons.Default.ArrowForward,
                title = "About Yourself",
                value = it.description,
                edit = true,
                user = user!!,
                field = 6,
            ) {
                loadProfileDetails += 1
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(text = "Are You placed yet?")
                Switch(
                    checked = it.placed,
                    onCheckedChange = {newplaced ->
                        it.placed = newplaced
                        viewModel.currentUser?.let { it1 ->
                            updateFirebaseData(context = context, userCollection, it1, it) {
                                loadProfileDetails += 1
                            }
                        }
                    }
                )

            }
            Spacer(modifier = Modifier.height(8.dp))
            Box (modifier = Modifier.fillMaxWidth() , contentAlignment = Alignment.Center){


                Button(onClick = {
                    viewModel.auth.signOut()
                    navController.navigate(Routes.LOGIN_SCREEN) {
                        this.popUpTo(Routes.LOGIN_SCREEN) {
                            inclusive = true
                        }
                    }
                }) {
                    Text("Logout", fontSize = 20.sp)
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileInfoItem(
    viewModel: HomeScreenViewModel,
    context: Context,
    icon: ImageVector,
    title: String,
    value: String,
    edit: Boolean,
    user: Student,
    field: Int,
    onDataEditing: (Int) -> Unit // Callback function for data editing
) {

    var editable = edit
    var userCollection = Firebase.firestore.collection("student")
    var currentUser = viewModel.currentUser
    if (value == "Some Error Occurred") {
        editable = false
    }
    var txt by rememberSaveable { mutableStateOf(value) }
    var editing by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .shadow(4.dp, shape = RoundedCornerShape(8.dp))
            .background(Color.White)

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(horizontal = 16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.defaultMinSize(minHeight = 50.dp)
            ) {
                Icon(imageVector = icon, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = title, style = MaterialTheme.typography.bodyMedium, fontFamily = FontFamily(typeface = Typeface.DEFAULT_BOLD))
            }
            if (editable) {
                if (!editing) {
                    IconButton(onClick = {
                        editing = !editing
                    }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit", modifier = Modifier.size(24.dp))
                    }
                } else {
                    IconButton(onClick = {
                        editing = !editing
                        // Update the user object with the new value
                        when (field) {
                            0 -> user.name = txt
                            2 -> user.phone = txt
                            6 -> user.description = txt
                        }
                        if (currentUser != null) {
                            updateFirebaseData(context,userCollection,currentUser,user){
                                onDataEditing(it)
                            }
                        }

                    }) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = "Done", modifier = Modifier.size(24.dp))
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (!editing) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 20.dp, bottom = 10.dp )
            )
        } else {
            // Use TextField for editing the value
            TextField(
                value = txt,
                onValueChange = {
                    txt = it // Update txt immediately as the user types
                }
                , modifier = Modifier.padding(start = 20.dp, bottom = 10.dp )
            )
        }
    }
}

fun updateFirebaseData(context: Context,userCollection:CollectionReference,currentUser:FirebaseUser,user: Student,onDataEditing:(Int)->Unit){
    userCollection.document(currentUser.uid).set(user)
        .addOnSuccessListener {
            onDataEditing(1) // Callback with the edited field
            Log.d("Data sent to Firebase", "Saved Successfully")
            toaster(context, "Profile Updated Successfully")
        }
        .addOnFailureListener {
            Log.d("Data sent to Firebase", "Not Saved Successfully")
            toaster(context, "Failed To Update Profile")
        }

}

