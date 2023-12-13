package com.example.placementtracker

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.placementtracker.ui.theme.Bluecolor

sealed class BottomNavItem(var title: String, var icon: Int, var screen_route: String) {

    object Home : BottomNavItem("Home", R.drawable.ic_home, Routes.HOME_DASHBOARD)
    object HomeTeacher : BottomNavItem("Home", R.drawable.ic_home, Routes.HOME_DASHBOARD_TEACHER)
    object cmpInfo : BottomNavItem("Company Info", R.drawable.ic_dept, Routes.COMPANY_INFO)
    object cmpInfoTeacher : BottomNavItem("Company Info", R.drawable.ic_dept, Routes.COMPANY_INFO_TEACHER)
    object stdInfo : BottomNavItem("Student Info", R.drawable.ic_person, Routes.STUDENT_INFO)
    object studentApplications : BottomNavItem("Opportunities", R.drawable.ic_tasks, Routes.OPPORTUNITIES)
    object allPlacements :
        BottomNavItem("Student Placed", R.drawable.ic_person_done, Routes.STUDENTS_PLACED)
    object allPlacementsTeacher :
        BottomNavItem("Student Placed", R.drawable.ic_person_done, Routes.STUDENTS_PLACED_TEACHER)
    object AllCompanies :
        BottomNavItem("All Companies", R.drawable.ic_all_comp, Routes.ALL_COMPANIES_SCREEN)
    object AllApplications : BottomNavItem("All Applications", R.drawable.ic_tasks, Routes.ALL_STUDENTS_SCREEN)
    object addNewCompany : BottomNavItem("Add New Company", R.drawable.ic_add_comp, Routes.ADD_NEW_COMPANY_SCREEN)


}



@Composable
fun BottomNavigationMaker(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.cmpInfo,
        BottomNavItem.allPlacements,
        BottomNavItem.studentApplications,
        BottomNavItem.stdInfo,
    )
    NavigationBar(
        modifier = Modifier
            .clip(
                RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = 0.dp,
                    bottomEnd = 0.dp
                )
            )
            .height(70.dp),
        containerColor = Bluecolor,
        contentColor = androidx.compose.ui.graphics.Color.Black
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        Spacer(modifier = Modifier.height(20.dp))
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Image(
                        painterResource(id = item.icon),
                        contentDescription = item.title,
                        modifier = Modifier
                            .padding(0.dp, 10.dp)
                            .size(26.dp),
                    )
                },
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

@Composable
fun BottomNavigationMakerUser(navController: NavController) {
    val items = listOf(
        BottomNavItem.HomeTeacher,
        BottomNavItem.cmpInfoTeacher,
        BottomNavItem.allPlacementsTeacher,
        BottomNavItem.AllCompanies,
        BottomNavItem.AllApplications,
        BottomNavItem.addNewCompany

    )
    NavigationBar(
        modifier = Modifier
            .clip(
                RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = 0.dp,
                    bottomEnd = 0.dp
                )
            )
            .height(70.dp),
        containerColor = Bluecolor,
        contentColor = Color.Black

    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        Spacer(modifier = Modifier.height(20.dp))
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Image(
                        painterResource(id = item.icon),
                        contentDescription = item.title,
                        modifier = Modifier
                            .padding(0.dp, 10.dp)
                            .size(26.dp),
                    )
                },
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