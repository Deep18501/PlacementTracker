package com.example.placementtracker

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.placementtracker.home_screens.CompanyInfo
import com.example.placementtracker.home_screens.HomeDashboard
import com.example.placementtracker.home_screens.Opportunities
import com.example.placementtracker.home_screens.StudentInfo
import com.example.placementtracker.home_screens.StudentPlaced
import com.example.placementtracker.teacher_screens.AddNewCompany
import com.example.placementtracker.teacher_screens.AllCompaniesScreen
import com.example.placementtracker.teacher_screens.AllStudentDetails
import com.example.placementtracker.teacher_screens.CompanyInfoTeacher
import com.example.placementtracker.teacher_screens.HomeDashboardTeacher
import com.example.placementtracker.teacher_screens.StudentPlacedTeacher
import com.example.placementtracker.ui.theme.PlacementTrackerTheme
import com.example.placementtracker.utils.FirebaseMessagingNotificationPermissionDialog
import com.example.placementtracker.viewmodels.AuthViewModel
import com.example.placementtracker.viewmodels.HomeScreenViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val showNotificationDialog = remember { mutableStateOf(false) }
            val notificationPermissionState = rememberPermissionState(
                permission = Manifest.permission.POST_NOTIFICATIONS
            )
            if (showNotificationDialog.value) FirebaseMessagingNotificationPermissionDialog(
                showNotificationDialog = showNotificationDialog,
                notificationPermissionState = notificationPermissionState
            )
            LaunchedEffect(key1=Unit){
                Firebase.messaging.subscribeToTopic("Tutorial")

                if (notificationPermissionState.status.isGranted ||
                    Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU
                ) {
                    Firebase.messaging.subscribeToTopic("Tutorial")
                } else showNotificationDialog.value = true
            }
            PlacementTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Routes.LOGIN_SCREEN
                    ) {
                        composable(Routes.LOGIN_SCREEN){
                            val authViewModel= AuthViewModel()
                            RegistrationScreen(navController,authViewModel)
                        }
                        composable(Routes.TEACHER_LOGIN_SCREEN){
                            TeacherLogin(navController = navController)
                        }
                        navigation(Routes.HOME_DASHBOARD_TEACHER, route = Routes.TEACHER_SCREEN){
                            composable(Routes.HOME_DASHBOARD_TEACHER) {
                                BottomNavigationLayoutUser(navController = navController) {
                                    HomeDashboardTeacher(navController = navController)
                                }
                            }
                            composable(Routes.STUDENTS_PLACED_TEACHER) {
                                BottomNavigationLayoutUser(navController = navController) {
                                    StudentPlacedTeacher()
                                }
                            }
                            composable(Routes.ALL_STUDENTS_SCREEN) {
                                BottomNavigationLayoutUser(navController = navController) {
                                    AllStudentDetails()
                                }
                            }
                            composable(Routes.COMPANY_INFO_TEACHER){
                                BottomNavigationLayoutUser(navController = navController) {
                                    CompanyInfoTeacher(navController = navController)
                                }
                            }
                            composable(Routes.ADD_NEW_COMPANY_SCREEN){
                                BottomNavigationLayoutUser(navController = navController) {
                                    AddNewCompany(navController)
                                }
                            }
                            composable(Routes.ALL_COMPANIES_SCREEN) {
                                BottomNavigationLayoutUser(navController = navController) {
                                    AllCompaniesScreen()
                                }
                            }

                        }
                        navigation(Routes.HOME_DASHBOARD, route = Routes.HOME_SCREEN){

                            composable(Routes.HOME_DASHBOARD) {
                                BottomNavigationLayout(navController = navController) {
                                    HomeDashboard(navController = navController)
                                }
                            }
                            composable(Routes.COMPANY_INFO) {
                                    BottomNavigationLayout(navController = navController) {
                                        CompanyInfo(navController)
                                    }
                            }
                            composable(Routes.STUDENT_INFO) {
                                val viewModel= HomeScreenViewModel()
                                BottomNavigationLayout(navController = navController) {
                                    StudentInfo(viewModel, navController)
                                }
                            }
                            composable(Routes.OPPORTUNITIES) {
                                BottomNavigationLayout(navController = navController) {
                                    Opportunities()
                                }
                            }
                            composable(Routes.STUDENTS_PLACED) {
                                BottomNavigationLayout(navController = navController) {
                                    StudentPlaced()
                                }
                            }

                        }


                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationLayout(
    navController: NavController,
    content: @Composable () -> Unit
) {
    Scaffold(
        bottomBar = { BottomNavigationMaker(navController) }
    ) {
        Surface (modifier = Modifier.padding(it)){
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationLayoutUser(
    navController: NavController,
    content: @Composable () -> Unit
) {
    Scaffold(
        bottomBar = { BottomNavigationMakerUser(navController) }
    ) {
        Surface (modifier = Modifier.padding(it)){
            content()
        }
    }
}
object Routes {
    const val HOME_SCREEN="home_screen"
    const val TEACHER_SCREEN="teacher_screen"
    const val LOGIN_SCREEN="login_screen"
    const val HOME_DASHBOARD="home_dashboard"
    const val HOME_DASHBOARD_TEACHER="home_dashboard_teacher"
    const val COMPANY_INFO="company_info"
    const val COMPANY_INFO_TEACHER="company_info_teacher"
    const val COMPANY_INFO_HOME="company_info_home"
    const val COMPANY_INFO_DETAILS="company_info_det"
    const val STUDENT_INFO="student_info"
    const val OPPORTUNITIES="Opportunities"
    const val STUDENTS_PLACED="student_placed"
    const val STUDENTS_PLACED_TEACHER="student_placed_TEACHER"
    const val TEACHER_LOGIN_SCREEN="teacher_login_screen"
    const val ALL_COMPANIES_SCREEN="all_companies_screen"
    const val ALL_STUDENTS_SCREEN="all_student_screen"
    const val ADD_NEW_COMPANY_SCREEN="all_new_company_screen"

}

@Composable
inline fun <reified T:ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController):T {
    val navGraphRoute=destination.parent?.route?:return viewModel()
    val parentEntry=remember(this){
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry)
}