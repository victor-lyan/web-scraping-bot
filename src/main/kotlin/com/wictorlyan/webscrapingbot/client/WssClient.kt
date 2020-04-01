package com.wictorlyan.webscrapingbot.client

import com.google.gson.Gson
import com.wictorlyan.webscrapingbot.dto.AfishaMovieDTO
import com.wictorlyan.webscrapingbot.dto.AfishaMovieScheduleDTO
import com.wictorlyan.webscrapingbot.dto.AfishaNewsDTO
import com.wictorlyan.webscrapingbot.dto.CoronavirusStatsDTO
import okhttp3.OkHttpClient
import okhttp3.Request

class WssClient {
    private val BASE_URL = "http://${System.getenv("WSS_HOST") ?: "localhost:9096"}"
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

    fun queryCurrentCoronavirusNews(): AfishaNewsDTO {
        val request = Request.Builder()
            .url("$BASE_URL/afisha-news/latest")
            .build()

        val response = client.newCall(request).execute()
        return gson.fromJson(response.body()?.string(), AfishaNewsDTO::class.java)
    }
    
    fun queryCurrentCoronavirusStatsForCountry(country: String): CoronavirusStatsDTO {
        val request = Request.Builder()
            .url("$BASE_URL/coronavirus/country-data/$country")
            .build()

        val response = client.newCall(request).execute()
        return gson.fromJson(response.body()?.string(), CoronavirusStatsDTO::class.java)
    }
}