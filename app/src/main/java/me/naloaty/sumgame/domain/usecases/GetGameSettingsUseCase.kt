package me.naloaty.sumgame.domain.usecases

import me.naloaty.sumgame.domain.entity.GameSettings
import me.naloaty.sumgame.domain.entity.Level
import me.naloaty.sumgame.domain.repository.GameRepository

class GetGameSettingsUseCase(
    private val repository: GameRepository
) {

    operator fun invoke(level: Level): GameSettings {
        return repository.getGameSettings(level)
    }
}