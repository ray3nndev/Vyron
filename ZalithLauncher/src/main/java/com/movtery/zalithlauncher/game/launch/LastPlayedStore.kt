/*
 * Vyron
 * Copyright (C) 2026 Vyron contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package com.movtery.zalithlauncher.game.launch

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tencent.mmkv.MMKV

/** Persists the most recently launched Minecraft version for the homepage. */
object LastPlayedStore {
    private const val KEY_VERSION = "lastPlayedVersion"
    private const val KEY_TIME = "lastPlayedAt"
    private const val KEY_TOTAL_PLAYTIME = "totalPlaytimeMs"
    private const val KEY_SESSION_COUNT = "sessionCount"

    private val storage: MMKV by lazy { MMKV.mmkvWithID("game_activity") }

    var versionName by mutableStateOf(storage.decodeString(KEY_VERSION, "").orEmpty())
        private set

    var playedAt by mutableStateOf(storage.decodeLong(KEY_TIME, 0L))
        private set

    var totalPlaytimeMs by mutableStateOf(storage.decodeLong(KEY_TOTAL_PLAYTIME, 0L))
        private set

    var sessionCount by mutableStateOf(storage.decodeLong(KEY_SESSION_COUNT, 0L))
        private set

    fun recordSessionDuration(durationMs: Long) {
        if (durationMs <= 0L) return
        totalPlaytimeMs += durationMs
        sessionCount += 1L
        storage.encode(KEY_TOTAL_PLAYTIME, totalPlaytimeMs)
        storage.encode(KEY_SESSION_COUNT, sessionCount)
    }

    fun formattedPlaytime(): String {
        val totalMinutes = totalPlaytimeMs / 60_000L
        val hours = totalMinutes / 60L
        val minutes = totalMinutes % 60L
        return when {
            hours > 0L -> "${hours}h ${minutes}m"
            totalMinutes > 0L -> "${totalMinutes}m"
            else -> "0m"
        }
    }

    fun recordLaunch(version: String) {
        versionName = version
        playedAt = System.currentTimeMillis()
        storage.encode(KEY_VERSION, versionName)
        storage.encode(KEY_TIME, playedAt)
    }
}
