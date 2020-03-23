package com.wictorlyan.webscrapingbot.client

import com.google.gson.Gson
import com.wictorlyan.webscrapingbot.dto.AfishaMovieDTO
import com.wictorlyan.webscrapingbot.dto.AfishaMovieScheduleDTO
import okhttp3.OkHttpClient
import okhttp3.Request

class AfishaClient {
    private val BASE_URL = "http://localhost:9096"
    private val client = OkHttpClient()
    private val gson = Gson()
    
    fun queryDailyMovies(): AfishaMovieScheduleDTO? {
        val request = Request.Builder()
            .url("$BASE_URL/afisha-movie/daily-schedule?short=true")
            .build()

        val response = client.newCall(request).execute()
        return gson.fromJson(response.body()?.string(), AfishaMovieScheduleDTO::class.java)
    }

    fun queryMovieDetails(movieId: String): AfishaMovieDTO? {
        val request = Request.Builder()
            .url("$BASE_URL/afisha-movie/get-by-name/$movieId")
            .build()

        val response = client.newCall(request).execute()
        return if (response.code() == 404) {
            null
        } else {
            gson.fromJson(response.body()?.string(), AfishaMovieDTO::class.java)
        }
    }
}