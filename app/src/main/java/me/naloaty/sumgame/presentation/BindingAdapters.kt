package me.naloaty.sumgame.presentation

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.card.MaterialCardView
import me.naloaty.sumgame.R
import me.naloaty.sumgame.domain.entity.GameResult

@BindingAdapter("intText")
fun bindRequiredAnswers(textView: TextView, int: Int) {
    textView.text = int.toString()
}

/**
 * GameFinishedFrgament
 */

@BindingAdapter("percentOfRightAnswers")
fun bindPercentOfRightAnswers(textView: TextView, gameResult: GameResult) {
    textView.text = getPercentOfRightAnswers(gameResult).toString()
}

fun getPercentOfRightAnswers(gameResult: GameResult) = with(gameResult) {
    if (countOfQuestions == 0)
        0
    else
        ((countOfRightAnswer.toFloat() / countOfQuestions.toFloat()) * 100).toInt()
}

@BindingAdapter("resultLogo")
fun bindResultLogo(imageView: ImageView, winner: Boolean) {
    imageView.setImageResource(
        if (winner)
            R.drawable.success_logo
        else
            R.drawable.failure_logo
    )
}

/**
 * GameFragment
 */

@BindingAdapter("timeLeft")
fun bindTimeLeft(textView: TextView, secondsLeft: Int) {
    val minutes = secondsLeft / 60
    val seconds = secondsLeft % 60
    textView.text = "%02d:%02d".format(minutes, seconds)
}

@BindingAdapter("progressAnswers")
fun bindProgressAnswers(textView: TextView, progressAnswers: Pair<Int, Int>) {
    val template = textView.context.getString(R.string.game_right_answers_count)
    val (countOfRightAnswers, minCountOfRightAnswers) = progressAnswers
    textView.text = template.format(countOfRightAnswers, minCountOfRightAnswers)
}

interface OptionClickListener {
    fun onClick(option: Int)
}

@BindingAdapter("optionValue", "onOptionClickListener")
fun bindOptionListener(
    mcv: MaterialCardView,
    optionValue: Int,
    onOptionClickListener: OptionClickListener
) {
    mcv.setOnClickListener {
        onOptionClickListener.onClick(optionValue)
    }
}