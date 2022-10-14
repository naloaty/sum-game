package me.naloaty.sumgame.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.card.MaterialCardView
import me.naloaty.sumgame.R
import me.naloaty.sumgame.databinding.FragmentGameBinding
import me.naloaty.sumgame.domain.entity.GameResult
import me.naloaty.sumgame.domain.entity.Level
import java.lang.RuntimeException


class GameFragment : Fragment() {

    private lateinit var level: Level
    private lateinit var viewModel: GameViewModel

    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException("FragmentGameBinding is null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[GameViewModel::class.java]
        observeViewModel()
        viewModel.initGame(level)
        viewModel.startGame()
    }

    private fun observeViewModel() {
        observeGameTimer()
        observeQuestion()
        observePercentOfRightAnswers()
        observeProgressAnswers()
        observeGameFinished()
    }

    @SuppressLint("SetTextI18n")
    private fun observeGameTimer() {
        viewModel.gameTimer.observe(viewLifecycleOwner) {
            val minutes = it / 60
            val seconds = it % 60
            binding.tvGameTimer.text = "%02d:%02d".format(minutes, seconds)
        }
    }

    private fun observeQuestion() {
        with(binding) {
            viewModel.question.observe(viewLifecycleOwner) {
                tvSum.text = it.sum.toString()
                tvVisibleNumber.text = it.visibleNumber.toString()

                bindAnswerOption(tvOption1, mcvOption1, it.options[0])
                bindAnswerOption(tvOption2, mcvOption2, it.options[1])
                bindAnswerOption(tvOption3, mcvOption3, it.options[2])
                bindAnswerOption(tvOption4, mcvOption4, it.options[3])
                bindAnswerOption(tvOption5, mcvOption5, it.options[4])
                bindAnswerOption(tvOption6, mcvOption6, it.options[5])
            }
        }
    }

    private fun bindAnswerOption(tvOption: TextView, mcvOption: MaterialCardView, option: Int) {
        tvOption.text = option.toString()
        mcvOption.setOnClickListener {
            viewModel.registerAnswer(option)
        }
    }

    private fun observePercentOfRightAnswers() {
        viewModel.percentOfRightAnswers.observe(viewLifecycleOwner) {
            binding.progressBar.progress = it
        }
    }

    private fun observeProgressAnswers() {
        val template = resources.getString(R.string.game_right_answers_count)

        viewModel.progressAnswers.observe(viewLifecycleOwner) {
            val (countOfRightAnswers, minCountOfRightAnswers) = it
            val formatted = template.format(countOfRightAnswers, minCountOfRightAnswers)
            binding.tvRightAnswersCount.text = formatted
        }
    }

    private fun observeGameFinished() {
        viewModel.gameFinished.observe(viewLifecycleOwner) {
            launchGameFinishedFragment(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun launchGameFinishedFragment(gameResult: GameResult) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, GameFinishedFragment.newInstance(gameResult))
            .addToBackStack(null)
            .commit()
    }

    private fun parseArgs() {
        level = requireArguments().getSerializable(ARG_LEVEL) as Level
    }

    companion object {

        const val NAME = "game_fragment"
        private const val ARG_LEVEL = "level"

        fun newInstance(level: Level): GameFragment {
            return GameFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_LEVEL, level)
                }
            }
        }
    }
}