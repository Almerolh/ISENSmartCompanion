package fr.isen.menga.isensmartcompanon.API

import androidx.compose.runtime.Composable
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.converter.gson.GsonConverterFactory


interface GitHubService {
    @GET("events.json")
    fun getEvents(): Call<List<Event>>
}




