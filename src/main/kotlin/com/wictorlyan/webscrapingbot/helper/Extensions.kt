package com.wictorlyan.webscrapingbot.helper

import com.vdurmont.emoji.EmojiParser
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