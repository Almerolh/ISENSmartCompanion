package fr.isen.menga.isensmartcompanon.composant

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import fr.isen.menga.isensmartcompanon.API.model
import fr.isen.menga.isensmartcompanon.room.User
import fr.isen.menga.isensmartcompanon.room.UserRepository
import kotlinx.coroutines.launch
import fr.isen.menga.isensmartcompanon.room.UserViewModel
import java.util.Date


@Composable
fun TitrePage(userViewModel: UserViewModel) {
    val users by userViewModel.users.collectAsState()
    var text by remember { mutableStateOf("") }
    var displayedText by remember { mutableStateOf("") }  // État pour stocker le texte affiché en haut
    val questionsAndResponses = remember { mutableListOf<Pair<String, String>>() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val currentTimeMillis = System.currentTimeMillis()
    val date = Date(currentTimeMillis)

    QuestionResponseList(questionsAndResponses)


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp)
    ) {
        // Texte en tant que titre
        Text(
            text = "ISEN",
            fontSize = 64.sp,  // Taille du texte
            color = Color.Red,
            fontWeight = FontWeight.Bold,  // Mettre en gras
        )
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(110.dp)
    ) {
        // Texte en tant que titre
        Text(
            text = "Smart Companion",
            fontSize = 16.sp,  // Taille du texte
            fontWeight = FontWeight.Bold,  // Mettre en gras

        )
        /*Text(
            text = displayedText,
            modifier = Modifier.fillMaxWidth()
        )*/
    }
    Box(modifier = Modifier.fillMaxSize()) {

        TextField(
            value = displayedText, // L'état du texte dans le TextField
            onValueChange = { displayedText = it }, // Mise à jour de l'état lors de la saisieerhger
            placeholder = { Text("Entrez du texte ici") }, // Placeholder
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(0.dp, 110.dp)
        )

        Button(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(0.dp, 115.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            onClick = {
                if (displayedText.isNotBlank()) {
                    scope.launch {
                        try {
                            // Utilisation de la fonction generateText pour obtenir la réponse de Gemini
                            val geminiResponse = generateText(displayedText)
                            questionsAndResponses.add(
                                Pair("Vous : $displayedText", "IA : $geminiResponse")
                            )
                            Toast.makeText(context, "Message Envoyé", Toast.LENGTH_SHORT)
                                .show()

                            val interaction =
                                User(question = displayedText, reponse = "$geminiResponse", Date = "$date")
                            userViewModel.addUser(interaction)

                        } catch (e: Exception) {
                            Toast.makeText(context, "Erreur : ${e.message}", Toast.LENGTH_SHORT)
                                .show()
                        } finally {
                            displayedText = "" // Réinitialiser le champ de saisie après envoi
                        }

                    }
                }
                else {
                    Toast.makeText(context, "Veuillez entrer une question.", Toast.LENGTH_SHORT)
                        .show()
                }
            }) {
            Text(
                text = "->",
                fontSize = 25.sp
            )

        }
    }
}

suspend fun generateText(prompt: String): String {
    return try {
        // Appel à une méthode de génération sur l'objet 'model'
        val result =
            model.generateContent(prompt)  // Assurez-vous d'avoir une méthode valide pour la génération de texte
        result.text ?: "Aucune réponse générée"
    } catch (e: Exception) {
        "Erreur : ${e.message}"
    }

}

@Composable
fun QuestionResponseList(questionsAndResponses: List<Pair<String, String>>) {
    LazyColumn (modifier = Modifier.fillMaxSize().padding(0.dp, 150.dp)){
        items(questionsAndResponses) { pair ->
            Column(modifier = Modifier.padding(16.dp).fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "${pair.first}")
                Spacer(modifier = Modifier.padding(5.dp))
                Text(text = "${pair.second}")
            }
        }
    }
}



