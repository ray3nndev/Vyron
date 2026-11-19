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

    private val storage: MMKV by lazy { MMKV.mmkvWithID("game_activity") }

    var versionName by mutableStateOf(storage.decodeString(KEY_VERSION, "").orEmpty())
        private set

    var playedAt by mutableStateOf(storage.decodeLong(KEY_TIME, 0L))
        private set

    fun recordLaunch(version: String) {
        versionName = version
        playedAt = System.currentTimeMillis()
        storage.encode(KEY_VERSION, versionName)
        storage.encode(KEY_TIME, playedAt)
    }
}
