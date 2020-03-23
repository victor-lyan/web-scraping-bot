package com.wictorlyan.webscrapingbot.helper

import com.vdurmont.emoji.EmojiParser
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun String.convertDate() = LocalDate.parse(this, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    .format(DateTimeFormatter.ofPattern("dd.MM.yyyy")).toString()

fun String.addEmojiBefore(emoji: String) = EmojiParser.parseToUnicode(":$emoji: $this")