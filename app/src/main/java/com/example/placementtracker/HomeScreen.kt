package com.example.placementtracker

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationDrawerItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.placementtracker.home_screens.CompanyInfo
import com.example.placementtracker.home_screens.HomeDashboard
import com.example.placementtracker.home_screens.Opportunities
import com.example.placementtracker.home_screens.Settings
import com.example.placementtracker.home_screens.StudentInfo
import com.example.placementtracker.home_screens.StudentPlaced

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val navControllerInner=rememberNavController()
    Surface (
        modifier = Modifier.fillMaxSize()
    ){
        Scaffold(
            bottomBar = { BottomNavigationMaker(navController = navControllerInner) }
        ) {

            NavigationGraph(navController = navControllerInner,it)
        }
    }

}

sealed class BottomNavItem(var title:String, var icon:Int, var screen_route:String){

    object Home : BottomNavItem("Home", R.drawable.ic_home,Routes.HOME_DASHBOARD)
    object cmpInfo: BottomNavItem("Company Info",R.drawable.ic_dept,Routes.COMPANY_INFO)
    object stdInfo: BottomNavItem("Student Info",R.drawable.ic_person,Routes.STUDENT_INFO)
    object opts: BottomNavItem("Opportunities",R.drawable.ic_tasks,Routes.OPPORTUNITIES)
    object stdPlaced: BottomNavItem("Student Placed",R.drawable.ic_person_done,Routes.STUDENTS_PLACED)
    object setting: BottomNavItem("Settings",R.drawable.ic_settings,Routes.SETTINGS)
}

@Composable
fun NavigationGraph(navController: NavHostController, paddingValues: PaddingValues) {
    NavHost(navController, startDestination = BottomNavItem.Home.screen_route) {
        composable(Routes.HOME_DASHBOARD) {
            HomeDashboard()
        }
        composable(Routes.COMPANY_INFO) {
            CompanyInfo()
        }
        composable(Routes.STUDENT_INFO) {
            StudentInfo()
        }
        composable(Routes.OPPORTUNITIES) {
            Opportunities()
        }
        composable(Routes.STUDENTS_PLACED) {
            StudentPlaced()
        }
        composable(Routes.SETTINGS){
            Settings()
        }
    }
}

@Composable
fun BottomNavigationMaker(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.cmpInfo,
        BottomNavItem.stdInfo,
        BottomNavItem.opts,
        BottomNavItem.stdPlaced,
        BottomNavItem.setting
    )
    NavigationBar(
        containerColor = colorResource(id = R.color.teal_200),
        contentColor = androidx.compose.ui.graphics.Color.Black
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = { Image(painterResource(id = item.icon), contentDescription = item.title) },
                alwaysShowLabel = true,
                selected = currentRoute == item.screen_route,
                onClick = {
                    navController.navigate(item.screen_route) {

                        navController.graph.startDestinationRoute?.let { screen_route ->
                            popUpTo(screen_route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}