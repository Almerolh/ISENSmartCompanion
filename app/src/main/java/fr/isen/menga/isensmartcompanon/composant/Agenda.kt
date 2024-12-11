import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Event

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AgendaApp() {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            BottomNavigation {
                val currentDestination = navController.currentDestination?.route
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.DateRange, contentDescription = "Cours") },
                    label = { Text("Cours") },
                    selected = currentDestination == "cours",
                    onClick = {
                        navController.navigate("cours") {
                            launchSingleTop = true
                        }
                    }
                )
                BottomNavigationItem(
                    icon = { Icon(Icons.Filled.Event, contentDescription = "Événements") },
                    label = { Text("Événements") },
                    selected = currentDestination == "evenements",
                    onClick = {
                        navController.navigate("evenements") {
                            launchSingleTop = true
                        }
                    }
                )
            }
        },
        bottomBar = {

        }
    ) {  // Ferme le bloc Scaffold ici
        // Contenu principal de l'AgendaApp, ici vous placez le NavHost
        NavHost(
            navController = navController,
            startDestination = "cours"
        ) {
            composable("cours") { CoursScreen() }
            composable("evenements") { EvenementsScreen() }
        }
    }
}


@Composable
fun CoursScreen() {
    // Implémentez la logique pour afficher les cours de l'étudiant
    Column {
        Text(text = "N'a pas de cours programmé")
        // Ajoutez des éléments pour afficher les détails des cours
    }
}

@Composable
fun EvenementsScreen() {
    // Implémentez la logique pour afficher les événements
    Column {
        Text(text = "N'a pas d'évènement programmé")
        // Ajoutez des éléments pour afficher les détails des événements
    }
}
