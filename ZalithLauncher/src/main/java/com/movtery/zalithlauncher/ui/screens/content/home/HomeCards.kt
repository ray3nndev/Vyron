/*
 * Zalith Launcher 2
 * Copyright (C) 2025-2026 MovTery <movtery228@qq.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/gpl-3.0.txt>.
 */

package com.movtery.zalithlauncher.ui.screens.content.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.movtery.cardgrid.model.CardLimits
import com.movtery.cardgrid.model.CardType
import com.movtery.zalithlauncher.BuildConfig
import com.movtery.zalithlauncher.BuildKeys
import com.movtery.zalithlauncher.R
import com.movtery.zalithlauncher.game.launch.LastPlayedStore
import com.movtery.zalithlauncher.ui.components.BackgroundCard
import com.movtery.zalithlauncher.ui.screens.content.home.server.ServerCardContent
import com.movtery.zalithlauncher.ui.screens.content.home.library.LibraryCardContent
import com.movtery.zalithlauncher.ui.screens.content.home.ram.RamCardContent
import com.movtery.zalithlauncher.ui.screens.content.home.version.VersionCardContent

/** 系统卡片（不可变更），由启动器自行提供并绘制在网格之外 */
class SystemCard(val id: String, val content: @Composable () -> Unit)

/**
 * 用户卡片图标的圆角
 */
val CardIconShape = RoundedCornerShape(percent = 28)

/**
 * 主页卡片注册表
 */
object HomeCards {
    /** 版本卡片的类型 id */
    const val VERSION_CARD_TYPE_ID = "version_card"

    /** 服务器卡片的类型 id */
    const val SERVER_CARD_TYPE_ID = "server_card"

    /** 游戏库概览卡片的类型 id */
    const val LIBRARY_CARD_TYPE_ID = "library_card"

    /** RAM 使用情况卡片的类型 id */
    const val RAM_CARD_TYPE_ID = "ram_card"

    private val versionCardType = CardType(
        typeId = VERSION_CARD_TYPE_ID,
        defaultSpan = IntOffset(10, 6),
        limits = CardLimits(
            minWidth = 10,
            minHeight = 4,
            maxHeight = 12
        ),
        content = { cardId ->
            VersionCardContent(cardId)
        }
    )

    /** 游戏库概览卡片类型 */
    private val libraryCardType = CardType(
        typeId = LIBRARY_CARD_TYPE_ID,
        defaultSpan = IntOffset(10, 4),
        limits = CardLimits(
            minWidth = 10,
            minHeight = 4,
            maxHeight = 8
        ),
        content = { cardId ->
            LibraryCardContent(cardId)
        }
    )

    /** Last played Minecraft version widget. */
    private val lastPlayedCardType = CardType(
        typeId = "last_played_card",
        defaultSpan = IntOffset(10, 4),
        limits = CardLimits(minWidth = 10, minHeight = 4, maxHeight = 8),
        content = { cardId -> LastPlayedCardContent(cardId) }
    )

    /** Total Minecraft playtime widget. */
    private val playtimeCardType = CardType(
        typeId = "playtime_card",
        defaultSpan = IntOffset(10, 4),
        limits = CardLimits(minWidth = 10, minHeight = 4, maxHeight = 8),
        content = { cardId -> PlaytimeCardContent(cardId) }
    )

    /** RAM 使用情况卡片类型 */
    private val ramCardType = CardType(
        typeId = RAM_CARD_TYPE_ID,
        defaultSpan = IntOffset(10, 4),
        limits = CardLimits(
            minWidth = 10,
            minHeight = 4,
            maxHeight = 8
        ),
        content = { cardId ->
            RamCardContent(cardId)
        }
    )

    /** 服务器卡片类型 */
    private val serverCardType = CardType(
        typeId = SERVER_CARD_TYPE_ID,
        defaultSpan = IntOffset(10, 6),
        limits = CardLimits(
            minWidth = 10,
            minHeight = 4,
            maxHeight = 12
        ),
        content = { cardId ->
            ServerCardContent(cardId)
        }
    )

    /** 版本卡片类型 */
    fun versionCardType(): CardType = versionCardType

    /** 服务器卡片类型 */
    fun serverCardType(): CardType = serverCardType

    /** 游戏库概览卡片类型 */
    fun libraryCardType(): CardType = libraryCardType

    /** RAM 使用情况卡片类型 */
    fun ramCardType(): CardType = ramCardType

    /** Last played widget type */
    fun lastPlayedCardType(): CardType = lastPlayedCardType

    /** Total playtime widget type */
    fun playtimeCardType(): CardType = playtimeCardType

    /** 用户卡片类型注册表 */
    val userCardTypes: List<CardType> = listOf(versionCardType, serverCardType, libraryCardType, ramCardType, lastPlayedCardType, playtimeCardType)

    /** 系统卡片（不可变更） */
    fun systemCards(): List<SystemCard> = buildList {
        // The main hero already carries Vyron identity; avoid a duplicate header.
        if (BuildConfig.DEBUG) {
            add(debugWarningCard())
        }
    }

    /** Editable widget showing the most recently launched Minecraft version. */
    @Composable
    private fun LastPlayedCardContent(cardId: String) {
        val version = LastPlayedStore.versionName
        BackgroundCard(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = "GAME LOGS",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = version.ifBlank { "No games launched yet" },
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = if (version.isBlank()) {
                        "Launch a Minecraft version and it will appear here."
                    } else {
                        "Your most recently launched Minecraft version."
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }

    /** Editable widget showing accumulated time from completed Minecraft sessions. */
    @Composable
    private fun PlaytimeCardContent(cardId: String) {
        val playtime = LastPlayedStore.formattedPlaytime()
        val sessions = LastPlayedStore.sessionCount
        BackgroundCard(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = "PLAYTIME",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = playtime,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "$sessions completed game sessions",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }

    /**
     * debug版本关不掉的警告，防止有人把测试版当正式版用 XD
     */
    private fun debugWarningCard() = SystemCard(id = "system_debug_warning") {
        BackgroundCard(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.generic_warning),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = stringResource(R.string.launcher_version_debug_warning, BuildKeys.LAUNCHER_NAME),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    modifier = Modifier
                        .alpha(0.8f)
                        .align(Alignment.End),
                    text = stringResource(R.string.launcher_version_debug_warning_cant_close),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
