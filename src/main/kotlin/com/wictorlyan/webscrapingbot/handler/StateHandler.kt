package com.wictorlyan.webscrapingbot.handler

import com.wictorlyan.webscrapingbot.State
import redis.clients.jedis.Jedis

class StateHandler(private val jedis: Jedis) {
    private val STATE_KEY = "state_stack#"
    private val LAST_MESSAGE_ID_KEY = "lastMessageId#"
    
    fun saveState(chatId: Long, state: State) {
        jedis.lpush("$STATE_KEY$chatId", state.toString());
    }

    fun getCurrentState(chatId: Long): State {
        val state = jedis.lindex("$STATE_KEY$chatId", 0)
        return if (state != null) State.valueOf(state) else State.NOT_FOUND
    }

    fun setPreviousState(chatId: Long) {
        val state = jedis.lpop("$STATE_KEY$chatId")
        if (state != null) State.valueOf(state) else State.START
    }

    fun clearState(chatId: Long) {
        jedis.del("$STATE_KEY$chatId")
    }
    
    fun saveLastMessageId(chatId: Long, messageId: Long) {
        jedis.set("$LAST_MESSAGE_ID_KEY$chatId", messageId.toString())
    }
    
    fun getLastMessageId(chatId: Long): Long? {
        return jedis.get("$LAST_MESSAGE_ID_KEY$chatId")?.toLong()
    }
}