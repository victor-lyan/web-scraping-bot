package com.wictorlyan.webscrapingbot.message

import com.wictorlyan.webscrapingbot.*
import com.wictorlyan.webscrapingbot.dto.AfishaNewsDTO
import com.wictorlyan.webscrapingbot.dto.CoronavirusStatsDTO
import com.wictorlyan.webscrapingbot.helper.addEmojiBefore
import com.wictorlyan.webscrapingbot.helper.parseEmojis
import com.wictorlyan.webscrapingbot.helper.translateMonthAndDay
import me.ivmg.telegram.entities.InlineKeyboardButton
import me.ivmg.telegram.entities.KeyboardButton

fun getCoronavirusMenuItems(): List<List<KeyboardButton>> {
    return listOf(
        listOf(KeyboardButton(BUTTON_LATEST_NEWS.addEmojiBefore("newspaper"))),
        listOf(KeyboardButton(BUTTON_CURRENT_DATA.addEmojiBefore("bar_chart"))),
        listOf(KeyboardButton(MESSAGE_BACK.addEmojiBefore("arrow_left")))
    )
}

fun getCoronavirusNewsMessages(news: AfishaNewsDTO): List<String> {
    val result: MutableList<String> = mutableListOf()
    val messageBuffer = StringBuffer()
    news.result.forEach {
        var article = "<a href=\"${it.link}\"><strong>${it.title}</strong></a>\n"
        article += "${it.date}\n"
        article += "<i>${it.description}</i>\n\n"
        
        if (messageBuffer.length + article.length > TELEGRAM_MESSAGE_LIMIT) {
            result.add(messageBuffer.toString())
            messageBuffer.delete(0, messageBuffer.length)
        }
        messageBuffer.append(article)
    }
    result.add(messageBuffer.toString())
    return result
}

fun getCoronavirusCountriesMessage(): List<List<InlineKeyboardButton>> {
    val result = mutableListOf<List<InlineKeyboardButton>>()
    for ((country, flag) in FLAGS_MAP) {
        result.add(listOf(InlineKeyboardButton(
            text = COUNTRIES_MAP[country]?.addEmojiBefore(flag) ?: "", 
            callbackData = "${KEY_CORONA_COUNTRY}$country"
        )))
    }
    return result
}

fun getCoronavirusStatsMessage(stats: CoronavirusStatsDTO): String {
    var result = "$MESSAGE_COUNTRY: <strong>${COUNTRIES_MAP[stats.country.toLowerCase()]}</strong>\n"
    result += "$MESSAGE_TOTAL_CASES: <strong>${stats.totalCases}</strong>\n"
    result += "$MESSAGE_NEW_CASES: <strong>${stats.newCases}</strong>\n"
    result += "$MESSAGE_TOTAL_DEATHS: <strong>${stats.totalDeaths}</strong>\n"
    result += "$MESSAGE_NEW_DEATHS: <strong>${stats.newDeaths}</strong>\n"
    result += "$MESSAGE_TOTAL_RECOVERED: <strong>${stats.totalRecovered}</strong>\n"
    result += "$MESSAGE_ACTIVE_CASES: <strong>${stats.activeCases}</strong>\n"
    result += "$MESSAGE_SERIOUS_CASES: <strong>${stats.seriousCases}</strong>\n"
    result += "$MESSAGE_TOTAL_CASES_BY_MILLION: <strong>${stats.totalCasesByMillion}</strong>\n"
    result += "$MESSAGE_DEATHS_BY_MILLION: <strong>${stats.deathsByMillion}</strong>\n"
    result += "$MESSAGE_FIRST_CASE_DATE: <strong>${stats.firstCaseDate?.translateMonthAndDay() ?: MESSAGE_UNKNOWN}</strong>\n"

    return result
}
