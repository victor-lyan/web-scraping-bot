package com.wictorlyan.webscrapingbot

const val TELEGRAM_MESSAGE_LIMIT = 4096

const val MESSAGE_START = "Приступим к работе!"
const val MESSAGE_CINEMAS = "Кинотеатры"
const val MESSAGE_DATE = "Дата"
const val MESSAGE_TIME = "Время"
const val MESSAGE_MOVIE_NOT_FOUND = "Фильм не найден"
const val MESSAGE_LINK = "Ссылка"
const val MESSAGE_LINK_TO_AFISHA = "Ссылка на афишу"
const val MESSAGE_TODAY_MOVIES_SCHEDULE = "Расписание кино на сегодня:"
const val MESSAGE_BACK = "Назад"
const val MESSAGE_UNKNOWN_INPUT = "Неизвестная команда!"
const val MESSAGE_UNKNOWN = "Неизвестно"
const val MESSAGE_CORONAVIRUS_SUCKS = "Пока не доступно...:disappointed: Чертов коронавирус:rage:"
const val MESSAGE_WE_ALL_ARE_GONNA_DIE = "Мы все умрем!!! Шутка... Умрем, но не все:ghost:"
const val MESSAGE_CHOOSE_COUNTRY = "Выберите страну:"
const val MESSAGE_COUNTRY = "Страна"
const val MESSAGE_TOTAL_CASES = "Общее количество зараженных"
const val MESSAGE_NEW_CASES = "Количество Новых случаев"
const val MESSAGE_TOTAL_DEATHS = "Общее количество умерших"
const val MESSAGE_NEW_DEATHS = "Количество недавно умерших"
const val MESSAGE_TOTAL_RECOVERED = "Общее количество поправившихся"
const val MESSAGE_ACTIVE_CASES = "Текущее количество зараженных"
const val MESSAGE_SERIOUS_CASES = "Количество тяжело больных"
const val MESSAGE_TOTAL_CASES_BY_MILLION = "Общее количество зараженных на 1 млн. населения"
const val MESSAGE_DEATHS_BY_MILLION = "Количество смертей на 1 млн. населения"
const val MESSAGE_FIRST_CASE_DATE = "Дата первого случая заражения"

const val BUTTON_TODAY_MOVIES = "Показать афишу кино на сегодня"
const val BUTTON_CORONAVIRUS = "Коронавирус"
const val BUTTON_LATEST_NEWS = "Последние новости"
const val BUTTON_CURRENT_DATA = "Текущие данные"

const val COUNTRY_UZBEKISTAN = "uzbekistan"
const val COUNTRY_RUSSIA = "russia"
const val COUNTRY_USA = "usa"
const val COUNTRY_WORLD = "world"

const val KEY_CORONA_COUNTRY = "coronaCountry_"

val COUNTRIES_MAP = mapOf(
    COUNTRY_UZBEKISTAN to "Узбекистан",
    COUNTRY_RUSSIA to "Россия",
    COUNTRY_USA to "США",
    COUNTRY_WORLD to "Мир"
)

val FLAGS_MAP = mapOf(
    COUNTRY_UZBEKISTAN to "uz",
    COUNTRY_RUSSIA to "ru",
    COUNTRY_USA to "us",
    COUNTRY_WORLD to "earth_americas"
)