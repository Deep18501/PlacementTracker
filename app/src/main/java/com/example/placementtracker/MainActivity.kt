package com.example.placementtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.placementtracker.ui.theme.PlacementTrackerTheme
import com.example.placementtracker.viewmodels.AuthViewModel

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
                        composable(Routes.HOME_SCREEN) {
                               HomeScreen()
                        }
                    }
                }
            }
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
    const val SETTINGS="settings"

}