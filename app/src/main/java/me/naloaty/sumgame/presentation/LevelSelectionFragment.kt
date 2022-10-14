package me.naloaty.sumgame.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.naloaty.sumgame.R
import me.naloaty.sumgame.databinding.FragmentLevelSelectionBinding
import me.naloaty.sumgame.domain.entity.Level
import java.lang.RuntimeException

class LevelSelectionFragment : Fragment() {

    private var _binding: FragmentLevelSelectionBinding? = null
    private val binding: FragmentLevelSelectionBinding
        get() = _binding ?: throw RuntimeException("FragmentLevelSelectionBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLevelSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnLevelEasy.setOnClickListener {
                launchGameFragment(Level.EASY)
            }
            btnLevelMiddle.setOnClickListener {
                launchGameFragment(Level.MIDDLE)
            }
            btnLevelHard.setOnClickListener {
                launchGameFragment(Level.HARD)
            }
            btnLevelTest.setOnClickListener {
                launchGameFragment(Level.TEST)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun launchGameFragment(level: Level) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, GameFragment.newInstance(level))
            .addToBackStack(GameFragment.NAME)
            .commit()
    }

    companion object {

        const val NAME = "level_selection_fragment"

        fun newInstance(): LevelSelectionFragment {
            return LevelSelectionFragment()
        }
    }
}