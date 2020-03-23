package com.wictorlyan.webscrapingbot.message

import com.wictorlyan.webscrapingbot.*
import com.wictorlyan.webscrapingbot.dto.AfishaMovieDTO
import com.wictorlyan.webscrapingbot.dto.AfishaMovieScheduleDTO
import com.wictorlyan.webscrapingbot.helper.addEmojiBefore
import me.ivmg.telegram.entities.KeyboardButton
import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun generateMainMenu(): List<List<KeyboardButton>> {
    return listOf(
        listOf(KeyboardButton(BUTTON_TODAY_MOVIES.addEmojiBefore("movie_camera")))
    )
}

fun getButtonsFromMovies(dailyMovies: AfishaMovieScheduleDTO?): List<List<KeyboardButton>> {
    val result = mutableListOf<List<KeyboardButton>>()
    dailyMovies?.result?.forEach {
        result.add(listOf(KeyboardButton("${it.name} (${it.genre})")))
    }
    result.add(listOf(KeyboardButton(MESSAGE_BACK.addEmojiBefore("arrow_left"))))
    return result
}

fun getMovieMessage(movie: AfishaMovieDTO?): Pair<String?, String> {
    return if (movie == null) {
        Pair(null, MESSAGE_MOVIE_NOT_FOUND)
    } else {
        val groupedCinemas = movie.getCinemasGrouped()
        var result = "<strong>${movie.name} (${movie.genre})</strong>   <a href=\"${movie.link}\">$MESSAGE_LINK</a>"
        result += "\n<strong>$MESSAGE_DATE: ${movie.movieDate}</strong>"
        result += "\n\n<u>$MESSAGE_CINEMAS:</u>\n" 
        for ((cinema, movieTimeList) in groupedCinemas) {
            result += "${cinema.name}\n$MESSAGE_TIME: "
            movieTimeList.forEach {
                result += LocalTime.parse(it.time).format(DateTimeFormatter.ofPattern("HH:mm"))
                result += if (it.format == "") " " else "(<i>${it.format}</i>) "
            }
            result += "\n\n"
        }
        Pair(
            movie.image,
            result
        )
    }
}
