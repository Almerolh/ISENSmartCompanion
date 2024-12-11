package fr.isen.menga.isensmartcompanon.composant

import android.content.Context
import android.content.Intent
import android.media.RouteListingPreference
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import fr.isen.menga.isensmartcompanon.EventDetailActivity
import fr.isen.menga.isensmartcompanon.room.AppDatabase
import fr.isen.menga.isensmartcompanon.room.User
import fr.isen.menga.isensmartcompanon.room.UserRepository
import fr.isen.menga.isensmartcompanon.room.UserViewModel
import fr.isen.menga.isensmartcompanon.room.UserViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun UserListScreen(userViewModel: UserViewModel) {
    val users by userViewModel.users.collectAsState()
    val user = remember { mutableStateListOf<User>() }

    // Données statiques que tu veux ajouter


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Affichage de la liste des utilisateurs
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(users) { user ->
                Text("DATE : ${user.Date}")
                Spacer(modifier = Modifier.padding(6.dp))
                Text("QUESTION : ${user.question}")
                Spacer(modifier = Modifier.padding(6.dp))
                Text("REPONSE : ${user.reponse} ")
            }

        }
        Box(modifier = Modifier.fillMaxSize()){
        }

    }
    Box(modifier = Modifier){ Button(
        modifier = Modifier

            .padding(8.dp),colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
        onClick = {  userViewModel.deleteUsers()  }){
        Text (
            text = "Tout supprimer"
        )
    }
    }
}

@Preview(showBackground = true)
@Composable
fun Historique() {
    val context = LocalContext.current
    val userViewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(UserRepository(context))  // Utilisation d'un ViewModelFactory
    )
    UserListScreen(userViewModel = userViewModel)
    userViewModel.fetchUsers()
}

