package me.naloaty.sumgame.presentation

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.naloaty.sumgame.data.GameRepositoryImpl
import me.naloaty.sumgame.domain.entity.GameResult
import me.naloaty.sumgame.domain.entity.GameSettings
import me.naloaty.sumgame.domain.entity.Level
import me.naloaty.sumgame.domain.entity.Question
import me.naloaty.sumgame.domain.usecases.GenerateQuestionUseCase
import me.naloaty.sumgame.domain.usecases.GetGameSettingsUseCase

class GameViewModel: ViewModel() {

    private val repository = GameRepositoryImpl

    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)
    private val getGameSettingsUseCase = GetGameSettingsUseCase(repository)

    private val _gameTimer = MutableLiveData<Int>()
    val gameTimer: LiveData<Int>
        get() = _gameTimer

    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question>
        get() = _question

    private val _percentOfRightAnswers = MutableLiveData<Int>()
    val percentOfRightAnswers: LiveData<Int>
        get() = _percentOfRightAnswers

    private val _progressAnswers = MutableLiveData<Pair<Int, Int>>()
    val progressAnswers: LiveData<Pair<Int, Int>>
        get() = _progressAnswers

    private val _gameFinished = MutableLiveData<GameResult>()
    val gameFinished: LiveData<GameResult>
        get() = _gameFinished

    private lateinit var gameSettings: GameSettings
    private lateinit var timer: CountDownTimer

    private var countOfRightAnswers = 0
    private var countOfQuestions = 0

    fun initGame(level: Level) {
        gameSettings = getGameSettingsUseCase(level)
        countOfQuestions = 0
        countOfRightAnswers = 0
    }

    fun startGame() {
        startTimer()
        generateQuestion()
        updateProgressAnswers()
        updatePercentOfRightAnswers()
    }

    fun registerAnswer(answer: Int) {
        val rightAnswer = _question.value?.rightAnswer()

        if (rightAnswer == answer) {
            countOfRightAnswers++
            updateProgressAnswers()
        }

        updatePercentOfRightAnswers()
        generateQuestion()
    }

    private fun startTimer() {
        timer = object : CountDownTimer(
            gameSettings.gameTimeInSeconds * 1000L,
            1000
        ) {
            override fun onTick(timeLeft: Long) {
                _gameTimer.value = (timeLeft / 1000).toInt()
            }

            override fun onFinish() {
                gameFinished()
            }

        }

        timer.start()
    }

    private fun updatePercentOfRightAnswers() {
        val coefficient = countOfRightAnswers.toFloat() / countOfQuestions.toFloat()
        _percentOfRightAnswers.value = (coefficient * 100).toInt()
    }

    private fun updateProgressAnswers() {
        _progressAnswers.value = Pair(
            countOfRightAnswers,
            gameSettings.minCountOfRightAnswers
        )
    }

    private fun generateQuestion() {
        _question.value = generateQuestionUseCase(
            gameSettings.maxSumValue
        )
        countOfQuestions++
    }

    private fun gameFinished() {
        _gameFinished.value = GameResult(
            isWinner(),
            countOfRightAnswers,
            countOfQuestions - 1,
            gameSettings
        )
    }

    private fun isWinner(): Boolean {
        val percentOfRightAnswers = percentOfRightAnswers.value ?: 0
        return countOfRightAnswers >= gameSettings.minCountOfRightAnswers
                && percentOfRightAnswers >= gameSettings.minPercentOfRightAnswers
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }

}