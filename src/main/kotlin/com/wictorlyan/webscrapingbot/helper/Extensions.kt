package com.wictorlyan.webscrapingbot.helper

import com.vdurmont.emoji.EmojiParser
import com.wictorlyan.webscrapingbot.handler.StateHandler
import me.ivmg.telegram.Bot
import me.ivmg.telegram.entities.Message
import me.ivmg.telegram.entities.ParseMode
import me.ivmg.telegram.entities.ReplyMarkup
import me.ivmg.telegram.network.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

fun String.convertDate() = LocalDate.parse(this, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    .format(DateTimeFormatter.ofPattern("dd.MM.yyyy")).toString()

fun String.addEmojiBefore(emoji: String) = EmojiParser.parseToUnicode(":$emoji: $this")

fun String.parseEmojis() = EmojiParser.parseToUnicode(this)

fun String.translateMonthAndDay() = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)
    .withLocale(Locale("ru"))
    .format(LocalDate.parse("2020 $this", DateTimeFormatter.ofPattern("yyyy MMM dd")))

fun Bot.sendMessageWithStateHandler(
    chatId: Long,
    text: String,
    parseMode: ParseMode? = null,
    disableWebPagePreview: Boolean? = null,
    disableNotification: Boolean? = null,
    replyToMessageId: Long? = null,
    replyMarkup: ReplyMarkup? = null,
    stateHandler: StateHandler,
    saveMessageId: Boolean = true,
    doReplace: Boolean = true
): Pair<retrofit2.Response<Response<Message>?>?, Exception?> {
    if (doReplace) {
        val lastMessageId = stateHandler.getLastMessageId(chatId)
        if (lastMessageId != null) {
            deleteMessage(chatId, lastMessageId)
        }
    }
    
    val message = sendMessage(
        chatId,
        text,
        parseMode,
        disableWebPagePreview,
        disableNotification,
        replyToMessageId,
        replyMarkup
    )
    val messageId = message.first?.body()?.result?.messageId
    if (messageId != null && saveMessageId) {
        stateHandler.saveLastMessageId(chatId, messageId)
    }
    return message
}

