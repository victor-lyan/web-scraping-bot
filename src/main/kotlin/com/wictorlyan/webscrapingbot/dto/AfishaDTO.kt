package com.wictorlyan.webscrapingbot.dto

import com.wictorlyan.webscrapingbot.helper.convertDate

data class AfishaCinemaMovieDTO(
    val movie: AfishaMovieDTO,
    val cinema: AfishaCinemaDTO,
    val date: String,
    val time: String,
    val format: String?
)

data class AfishaCinemaDTO(
    val id: Int,
    val name: String,
    val linkAfisha: String,
    val linkAbout: String
)

data class AfishaMovieDTO(
    val id: Int,
    val name: String,
    val genre: String,
    val link: String,
    val image: String?,
    val cinemas: MutableList<AfishaCinemaMovieDTO> = mutableListOf()
) {
    var movieDate: String? = null
        get() = field?.convertDate()

    fun getCinemasGrouped(): Map<AfishaCinemaDTO, List<MovieTime>> {
        val result = mutableMapOf<AfishaCinemaDTO, MutableList<MovieTime>>()
        cinemas.forEach {
            if (movieDate == null) {
                movieDate = it.date
            }
            val currentList = result[it.cinema]
            if (currentList == null) {
                result[it.cinema] = mutableListOf(MovieTime(it.date, it.time, it.format))
            } else {
                currentList.add(MovieTime(it.date, it.time, it.format))
            }
        }
        return result
    }
    
    class MovieTime(var date: String, var time: String, var format: String?)
}

data class AfishaMovieSmallDTO(val id: Int, val name: String, val genre: String)

data class AfishaMovieScheduleDTO(val result: List<AfishaMovieSmallDTO>)