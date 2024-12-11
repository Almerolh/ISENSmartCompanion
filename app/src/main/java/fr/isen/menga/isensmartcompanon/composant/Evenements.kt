package fr.isen.menga.isensmartcompanon.composant

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import fr.isen.menga.isensmartcompanon.API.Event
import fr.isen.menga.isensmartcompanon.API.GitHubService
import fr.isen.menga.isensmartcompanon.EventDetailActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


@Composable
fun EventListScreen() {
    val events = remember { mutableStateOf<List<Event>>(emptyList()) }
    val isLoading = remember { mutableStateOf(true) }

    // Faire l'appel Retrofit lorsque l'écran est lancé
    LaunchedEffect(Unit) {
        fetchEvents(events, isLoading)
    }

    // Affichage des événements avec LazyColumn
    if (isLoading.value) {
        // Afficher un indicateur de chargement pendant que les événements sont récupérés
        LoadingIndicator()
    } else {
        EventList(events.value)
    }
}

@Composable
fun LoadingIndicator() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun EventList(events: List<Event>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(events) { event ->
            EventItem(event)
        }
    }
}

@Composable
fun EventItem(event: Event) {
    val context = LocalContext.current
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),colors = ButtonDefaults.buttonColors(containerColor = Color.Red), onClick = { val intent = Intent(context, EventDetailActivity::class.java)
            intent.putExtra("event_id", "${event.id}")
            intent.putExtra("event_Title", "${event.title}")
            intent.putExtra("event_description", "${event.description}")
            intent.putExtra("event_date", "${event.date}")
            intent.putExtra("event_location", "${event.location}")
            intent.putExtra("event_category", "${event.category}")
            context.startActivity(intent)     }

    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "${event.title}", style = MaterialTheme.typography.titleMedium)
        }
    }
}

// Fonction pour récupérer les événements via Retrofit

fun fetchEvents(events: MutableState<List<Event>>, isLoading: MutableState<Boolean>) {
    // Setup Retrofit
    val retrofit = Retrofit.Builder()
        .baseUrl("https://isen-smart-companion-default-rtdb.europe-west1.firebasedatabase.app/") // Base URL of your API
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(GitHubService::class.java)
    val call = apiService.getEvents()

    // Make the API call asynchronously
    call.enqueue(object : Callback<List<Event>> {
        override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>) {
            if (response.isSuccessful) {
                // If successful, update the list of events
                events.value = response.body() ?: emptyList()
                isLoading.value = false
            } else {
                // Show error if the response is not successful
                Toast.makeText(
                    /*context*/ null, // Replace with actual context if needed
                    "Error: ${response.code()}",
                    Toast.LENGTH_SHORT
                ).show()
                isLoading.value = false
            }
        }

        override fun onFailure(call: Call<List<Event>>, t: Throwable) {
            // Log and display error message when request fails
            Log.e("RetrofitError", "Request failed", t)
            Toast.makeText(
                /*context*/ null, // Replace with actual context if needed
                "Request failed: ${t.message}",
                Toast.LENGTH_SHORT
            ).show()
            isLoading.value = false
        }
    })
}


