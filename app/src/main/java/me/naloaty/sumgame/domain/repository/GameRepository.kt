package me.naloaty.sumgame.domain.repository

import me.naloaty.sumgame.domain.entity.GameSettings
import me.naloaty.sumgame.domain.entity.Level
import me.naloaty.sumgame.domain.entity.Question

interface GameRepository {

    fun generateQuestion(
        maxSumValue: Int,
        countOfOptions: Int
    ): Question

    fun getGameSettings(level: Level): GameSettings
}