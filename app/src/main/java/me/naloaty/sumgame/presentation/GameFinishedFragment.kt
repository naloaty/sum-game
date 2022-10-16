package me.naloaty.sumgame.presentation

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import me.naloaty.sumgame.R
import me.naloaty.sumgame.databinding.FragmentGameBinding
import me.naloaty.sumgame.databinding.FragmentGameFinishedBinding
import me.naloaty.sumgame.domain.entity.GameResult
import java.lang.RuntimeException

class GameFinishedFragment : Fragment() {

    private lateinit var gameResult: GameResult

    private var _binding: FragmentGameFinishedBinding? = null
    private val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw RuntimeException("FragmentGameBinding is null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
        bindViews()
    }

    private fun setupClickListeners() {
        binding.btnTryAgain.setOnClickListener {
            retryGame()
        }
    }

    private fun bindViews() {
        with(binding) {
            with(gameResult) {
                ivLogoGameResult.setImageResource(getLogoRes())
                tvScore.text = countOfRightAnswer.toString()
                tvPercentOfRightAnswers.text = getPercentOfRightAnswers().toString()
            }
            with (gameResult.gameSettings) {
                tvRequiredRightAnswers.text = minCountOfRightAnswers.toString()
                tvRequiredPercentOfRightAnswers.text = minPercentOfRightAnswers.toString()
            }
        }
    }

    private fun getLogoRes() = with(gameResult) {
        if (winner) {
            R.drawable.success_logo
        } else {
            R.drawable.failure_logo
        }
    }

    private fun getPercentOfRightAnswers() = with(gameResult) {
        if (countOfQuestions == 0) {
            0
        } else {
            val coefficient = countOfRightAnswer.toFloat() / countOfQuestions.toFloat()
            (coefficient * 100).toInt()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseArgs() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            gameResult = requireArguments().getParcelable(ARG_GAME_RESULT, GameResult::class.java)!!
        } else {
            gameResult = requireArguments().getParcelable<GameResult>(ARG_GAME_RESULT)!!
        }
    }

    private fun retryGame() {
        findNavController().popBackStack()
    }

    companion object {
        const val ARG_GAME_RESULT = "game_result"

        fun newInstance(gameResult: GameResult): GameFinishedFragment {
            return GameFinishedFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_GAME_RESULT, gameResult)
                }
            }
        }
    }
}