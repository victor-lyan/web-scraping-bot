package com.wictorlyan.webscrapingbot

import com.wictorlyan.webscrapingbot.client.WssClient
import com.wictorlyan.webscrapingbot.handler.StateHandler
import com.wictorlyan.webscrapingbot.handler.TextHandler
import com.wictorlyan.webscrapingbot.message.generateMainMenu
import me.ivmg.telegram.bot
import me.ivmg.telegram.dispatch
import me.ivmg.telegram.dispatcher.callbackQuery
import me.ivmg.telegram.dispatcher.command
import me.ivmg.telegram.dispatcher.message
import me.ivmg.telegram.entities.KeyboardReplyMarkup
import me.ivmg.telegram.extensions.filters.Filter
import okhttp3.logging.HttpLoggingInterceptor
import redis.clients.jedis.Jedis

fun main(args: Array<String>) {
    val jedis = Jedis(System.getenv("REDIS_HOST") ?: "localhost")
    val stateHandler = StateHandler(jedis)
    val wssClient = WssClient()
    val bot = bot {
        // we pass token as a first parameter for main function
        token = if (args.isNotEmpty()) args[0] else ""
        timeout = 30
        logLevel = HttpLoggingInterceptor.Level.BODY

        dispatch {
            command("start") { bot, update ->
                val chatId = update.message?.chat?.id ?: return@command

                // first we empty state stack
                stateHandler.clearState(chatId)

                val keyboardMarkup = KeyboardReplyMarkup(keyboard = generateMainMenu(), resizeKeyboard = true)
                bot.sendMessage(chatId = chatId, text = MESSAGE_START, replyMarkup = keyboardMarkup)
                stateHandler.saveState(chatId, State.START)
            }

            message(Filter.Text) { bot, update ->
                val messageText = update.message!!.text!!
                val chatId = update.message!!.chat.id
                val textHandler = TextHandler(wssClient, stateHandler, bot, messageText, chatId)

                when (stateHandler.getCurrentState(chatId)) {
                    State.START -> textHandler.startMessages()
                    State.CORONAVIRUS -> textHandler.coronavirusMessages()
                    State.SHOW_MOVIES -> textHandler.showMoviesMessages()
                    State.MOVIE_DETAILS -> textHandler.movieDetailsMessages()
                    State.NOT_FOUND -> {
                        // when state is not found, we give the user the start message
                        val keyboardMarkup = KeyboardReplyMarkup(keyboard = generateMainMenu(), resizeKeyboard = true)
                        bot.sendMessage(chatId = chatId, text = MESSAGE_START, replyMarkup = keyboardMarkup)
                        stateHandler.saveState(chatId, State.START)
                    }
                }
            }
            
            callbackQuery(KEY_CORONA_COUNTRY) {bot, update ->
                update.callbackQuery?.let {
                    val chatId = it.message!!.chat.id
                    val textHandler = TextHandler(
                        wssClient, 
                        stateHandler, 
                        bot, 
                        it.data.substringAfter(KEY_CORONA_COUNTRY), 
                        chatId
                    )
                    textHandler.coronavirusCountryDetails()
                }
                
            }
        }
    }
    bot.startPolling()
}
