package com.wictorlyan.webscrapingbot.handler

import com.wictorlyan.webscrapingbot.*
import com.wictorlyan.webscrapingbot.client.AfishaClient
import com.wictorlyan.webscrapingbot.helper.addEmojiBefore
import com.wictorlyan.webscrapingbot.message.generateMainMenu
import com.wictorlyan.webscrapingbot.message.getButtonsFromMovies
import com.wictorlyan.webscrapingbot.message.getMovieMessage
import me.ivmg.telegram.Bot
import me.ivmg.telegram.entities.KeyboardReplyMarkup
import me.ivmg.telegram.entities.ParseMode

class TextHandler(
    private val wssClient: AfishaClient,
    private val stateHandler: StateHandler,
    private val bot: Bot,
    private val messageText: String,
    private val chatId: Long    
) {
    fun startMessages() {
        when (messageText) {
            BUTTON_TODAY_MOVIES.addEmojiBefore("movie_camera") -> {
                val dailyMovies = wssClient.queryDailyMovies()
                bot.sendMessage(
                    chatId,
                    text = MESSAGE_TODAY_MOVIES_SCHEDULE,
                    replyMarkup = KeyboardReplyMarkup(getButtonsFromMovies(dailyMovies), true)
                )
                stateHandler.saveState(chatId, State.SHOW_MOVIES)
            }
            else -> unknownMessage(chatId)
        }
    }

    fun showMoviesMessages() {
        when (messageText) {
            MESSAGE_BACK.addEmojiBefore("arrow_left") -> {
                val keyboardMarkup = KeyboardReplyMarkup(keyboard = generateMainMenu(), resizeKeyboard = true)
                bot.sendMessage(chatId = chatId, text = MESSAGE_START, replyMarkup = keyboardMarkup)
                stateHandler.setPreviousState(chatId)
            }
            else -> {
                val movieDetails = wssClient.queryMovieDetails(messageText.substringBefore("(").trim())
                val moviePair = getMovieMessage(movieDetails)
                when {
                    moviePair.second == MESSAGE_MOVIE_NOT_FOUND -> {
                        // movie was not found
                        bot.sendMessage(
                            chatId = chatId,
                            text = moviePair.second,
                            parseMode = ParseMode.HTML
                        )
                    }
                    moviePair.first != null -> {
                        // movie image is not null
                        bot.sendPhoto(
                            chatId = chatId,
                            photo = moviePair.first!!,
                            parseMode = ParseMode.HTML
                        )
                        bot.sendMessage(
                            chatId,
                            text = moviePair.second,
                            parseMode = ParseMode.HTML,
                            disableWebPagePreview = true
                        )
                        //stateHandler.saveState(chatId, State.MOVIE_DETAILS)
                    }
                    else -> {
                        // movie image is null
                        bot.sendMessage(
                            chatId,
                            text = moviePair.second,
                            parseMode = ParseMode.HTML
                        )
                        //stateHandler.saveState(chatId, State.MOVIE_DETAILS)
                    }
                }
            }
        }
    }

    fun movieDetailsMessages() {
        TODO("Not yet implemented")
    }
    
    private fun unknownMessage(chatId: Long) {
        bot.sendMessage(chatId = chatId, text= MESSAGE_UNKNOWN_INPUT)
    }
}