package com.wictorlyan.webscrapingbot.handler

import com.wictorlyan.webscrapingbot.*
import com.wictorlyan.webscrapingbot.client.WssClient
import com.wictorlyan.webscrapingbot.helper.addEmojiBefore
import com.wictorlyan.webscrapingbot.helper.parseEmojis
import com.wictorlyan.webscrapingbot.helper.sendMessageWithStateHandler
import com.wictorlyan.webscrapingbot.message.*
import me.ivmg.telegram.Bot
import me.ivmg.telegram.entities.InlineKeyboardMarkup
import me.ivmg.telegram.entities.KeyboardReplyMarkup
import me.ivmg.telegram.entities.ParseMode

class TextHandler(
    private val wssClient: WssClient,
    private val stateHandler: StateHandler,
    private val bot: Bot,
    private val messageText: String,
    private val chatId: Long
) {
    fun startMessages() {
        when (messageText) {
            BUTTON_CORONAVIRUS.addEmojiBefore("mask") -> {
                stateHandler.saveState(chatId, State.CORONAVIRUS)
                bot.sendMessage(
                    chatId,
                    text = MESSAGE_WE_ALL_ARE_GONNA_DIE.parseEmojis(),
                    replyMarkup = KeyboardReplyMarkup(getCoronavirusMenuItems(), true)
                )
            }
            BUTTON_TODAY_MOVIES.addEmojiBefore("movie_camera") -> {
                /*val dailyMovies = wssClient.queryDailyMovies()
                bot.sendMessage(
                    chatId,
                    text = MESSAGE_TODAY_MOVIES_SCHEDULE,
                    replyMarkup = KeyboardReplyMarkup(getButtonsFromMovies(dailyMovies), true)
                )
                stateHandler.saveState(chatId, State.SHOW_MOVIES)*/
                bot.sendMessage(
                    chatId,
                    text = MESSAGE_CORONAVIRUS_SUCKS.parseEmojis()
                )
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
        bot.sendMessage(chatId = chatId, text = MESSAGE_UNKNOWN_INPUT)
    }

    fun coronavirusMessages() {
        when (messageText) {
            BUTTON_LATEST_NEWS.addEmojiBefore("newspaper") -> {
                val currentNews = wssClient.queryCurrentCoronavirusNews()
                val newsMessages = getCoronavirusNewsMessages(currentNews)
                newsMessages.forEach {
                    bot.sendMessage(
                        chatId,
                        text = it,
                        parseMode = ParseMode.HTML,
                        disableWebPagePreview = true
                    )
                }
            }
            BUTTON_CURRENT_DATA.addEmojiBefore("bar_chart") -> {
                bot.sendMessageWithStateHandler(
                    chatId = chatId,
                    text = MESSAGE_CHOOSE_COUNTRY,
                    replyMarkup = InlineKeyboardMarkup(getCoronavirusCountriesMessage()),
                    stateHandler = stateHandler,
                    doReplace = false
                )
            }
            MESSAGE_BACK.addEmojiBefore("arrow_left") -> {
                val keyboardMarkup = KeyboardReplyMarkup(keyboard = generateMainMenu(), resizeKeyboard = true)
                bot.sendMessage(chatId = chatId, text = MESSAGE_START, replyMarkup = keyboardMarkup)
                stateHandler.setPreviousState(chatId)
            }
            else -> unknownMessage(chatId)
        }
    }

    fun coronavirusCountryDetails() {
        val currentStats = wssClient.queryCurrentCoronavirusStatsForCountry(messageText)
        bot.sendMessageWithStateHandler(
            chatId,
            text = getCoronavirusStatsMessage(currentStats),
            parseMode = ParseMode.HTML,
            stateHandler = stateHandler
        )
    }
}