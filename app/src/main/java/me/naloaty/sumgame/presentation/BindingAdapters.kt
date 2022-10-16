package me.naloaty.sumgame.presentation

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import me.naloaty.sumgame.R
import me.naloaty.sumgame.domain.entity.GameResult

@BindingAdapter("intText")
fun bindRequiredAnswers(textView: TextView, int: Int) {
    textView.text = int.toString()
}

@BindingAdapter("percentOfRightAnswers")
fun bindPercentOfRightAnswers(textView: TextView, gameResult: GameResult) = with(gameResult) {
    textView.text = getPercentOfRightAnswers(gameResult).toString()
}

fun getPercentOfRightAnswers(gr: GameResult) = with(gr) {
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