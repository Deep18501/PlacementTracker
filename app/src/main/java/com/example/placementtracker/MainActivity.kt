package com.example.placementtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.placementtracker.home_screens.CompanyInfo
import com.example.placementtracker.home_screens.HomeDashboard
import com.example.placementtracker.home_screens.Opportunities
import com.example.placementtracker.home_screens.StudentInfo
import com.example.placementtracker.home_screens.StudentPlaced
import com.example.placementtracker.ui.theme.PlacementTrackerTheme
import com.example.placementtracker.viewmodels.AuthViewModel
import com.example.placementtracker.viewmodels.HomeScreenViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PlacementTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController as NavHostController,
                        startDestination = Routes.LOGIN_SCREEN
                    ) {
                        composable(Routes.LOGIN_SCREEN){
                            val authViewModel= AuthViewModel()
                            RegistrationScreen(navController,authViewModel)
                        }

                        navigation(Routes.HOME_DASHBOARD, route = Routes.HOME_SCREEN){

                            composable(Routes.HOME_DASHBOARD) {
                                BottomNavigationLayout(navController = navController) {
                                    HomeDashboard()
                                }
                            }
                            composable(Routes.COMPANY_INFO) {
                                BottomNavigationLayout(navController = navController) {
                                    CompanyInfo()
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
//    Column {
//        // Content for the home screen
//        Box(
//            modifier = Modifier.weight(1f)
//        ) {
//            content()
//        }
//
//        // Bottom Navigation
//        BottomNavigationMaker(navController)
//    }
    Scaffold(
        bottomBar = { BottomNavigationMaker(navController) }
    ) {
        Surface (modifier = Modifier.padding(it)){
            content()
        }
    }
}


object Routes {
    const val HOME_SCREEN="home_screen"
    const val LOGIN_SCREEN="login_screen"
    const val HOME_DASHBOARD="home_dashboard"
    const val COMPANY_INFO="company_info"
    const val STUDENT_INFO="student_info"
    const val OPPORTUNITIES="Opportunities"
    const val STUDENTS_PLACED="student_placed"

}

@Composable
inline fun <reified T:ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController):T {
    val navGraphRoute=destination.parent?.route?:return viewModel()
    val parentEntry=remember(this){
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry)
}