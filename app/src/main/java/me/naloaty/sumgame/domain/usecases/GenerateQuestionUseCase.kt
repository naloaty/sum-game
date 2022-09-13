package me.naloaty.sumgame.domain.usecases

import me.naloaty.sumgame.domain.entity.Question
import me.naloaty.sumgame.domain.repository.GameRepository

class GenerateQuestionUseCase(
    private val repository: GameRepository
) {

    operator fun invoke(maxSumValue: Int): Question {
        return repository.generateQuestion(maxSumValue, COUNT_OF_OPTIONS)
    }

    private companion object {
        private const val COUNT_OF_OPTIONS = 6
    }
}