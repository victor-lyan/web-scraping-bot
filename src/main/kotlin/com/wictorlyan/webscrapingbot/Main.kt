package com.wictorlyan.webscrapingbot

import com.wictorlyan.webscrapingbot.client.AfishaClient
import com.wictorlyan.webscrapingbot.handler.StateHandler
import com.wictorlyan.webscrapingbot.handler.TextHandler
import com.wictorlyan.webscrapingbot.message.generateMainMenu
import me.ivmg.telegram.bot
import me.ivmg.telegram.dispatch
import me.ivmg.telegram.dispatcher.command
import me.ivmg.telegram.dispatcher.message
import me.ivmg.telegram.entities.KeyboardReplyMarkup
import me.ivmg.telegram.extensions.filters.Filter
import okhttp3.logging.HttpLoggingInterceptor
import redis.clients.jedis.Jedis

const val MESSAGE_START = "Приступим к работе!"
const val MESSAGE_CINEMAS = "Кинотеатры"
const val MESSAGE_DATE = "Дата"
const val MESSAGE_TIME = "Время"
const val MESSAGE_MOVIE_NOT_FOUND = "Фильм не найден"
const val MESSAGE_LINK = "Ссылка"
const val MESSAGE_LINK_TO_AFISHA = "Ссылка на афишу"
const val MESSAGE_TODAY_MOVIES_SCHEDULE = "Расписание кино на сегодня:"
const val MESSAGE_BACK = "Назад"
const val BUTTON_TODAY_MOVIES = "Показать афишу кино на сегодня"
const val MESSAGE_UNKNOWN_INPUT = "Неизвестная команда!"

fun main(args: Array<String>) {
    val jedis = Jedis(System.getenv("REDIS_HOST") ?: "localhost")
    val stateHandler = StateHandler(jedis)
    val wssClient = AfishaClient()
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
                    State.SHOW_MOVIES -> textHandler.showMoviesMessages()
                    State.MOVIE_DETAILS -> textHandler.movieDetailsMessages()
                }
            }
        }
    }
    bot.startPolling()
}
