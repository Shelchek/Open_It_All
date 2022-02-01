package com.example.openitall

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.openitall.databinding.GameFragmentBinding

class GameFragment : Fragment() {
    private var binding: GameFragmentBinding? = null
    private val gameViewModel: GameViewModel by activityViewModels()
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            returnToMainFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = GameFragmentBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.apply {
            viewModel = gameViewModel
            gameFragment = this@GameFragment
            lifecycleOwner = viewLifecycleOwner
        }
        allTilesClickability(false)
        gameViewModel.startTime = System.currentTimeMillis()

        handler.postDelayed({ startGame() }, 1000)
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private fun showNumbers() {
        var delayMillis = 0L
        allTilesClickability(false)
        for (i in gameViewModel.getCurrentGameBoard()) {
            val button = requireActivity().findViewById(i.second) as ImageButton
            button.postDelayed({ button.setImageResource(i.first) }, delayMillis)
            delayMillis += 1000
        }
        delayMillis += 2000

        for (i in gameViewModel.getCurrentGameBoard().indices.reversed()) {
            delayMillis += 1000
            val image = gameViewModel.getCurrentGameBoard()[i].second
            val button = requireActivity().findViewById(image) as ImageButton
            button.postDelayed({ button.setImageResource(0) }, delayMillis)
        }
        handler.postDelayed({ allTilesClickability(true) }, delayMillis)
    }

    private fun allTilesClickability(clickable: Boolean) {
        for (i in gameViewModel.tiles) {
            val button = requireActivity().findViewById(i) as ImageButton
            button.isClickable = clickable
        }
    }

    fun tileClickEvent(tileId: Int) {
        with(gameViewModel) {
            val button: ImageButton = requireActivity().findViewById(tileId) as ImageButton
            if (isAnswerCorrect(tileId)) {
                button.setImageResource(correctAnswer().first)
                if (isGameCompleted()) {
                    Toast.makeText(requireContext(), "The Game is finished!", Toast.LENGTH_LONG)
                        .show()
                    saveScore()
                } else {
                    button.isClickable = false
                    nextStep()
                    if (isLevelCompleted()) {
                        saveScore()
                        nextLevelSequence()
                    } else{ }
                }
            } else {
                errors++
                view?.background?.setTint(ContextCompat.getColor(requireContext(), R.color.red))
                handler.postDelayed(
                    {
                        view?.background?.setTint(ContextCompat.getColor(requireContext(), R.color.white))
                    },200
                )
            }
        }
    }

    private fun startGame() {
        gameViewModel.initializeGameBoard()
        showNumbers()
    }

    private fun nextLevelSequence() {
        with(gameViewModel) {
            Toast.makeText(
                requireContext(),
                "Level: ${level.value} is completed!",
                Toast.LENGTH_LONG
            ).show()
            levelUP()
            initializeGameBoard()
            allTilesClickability(false)
            handler.postDelayed({
                showNumbers()
                for (i in gameViewModel.tiles) {
                    val tile = requireActivity().findViewById(i) as ImageButton
                    tile.setImageResource(0)
                    level.value
                }
            }, 3500)
        }
    }

    private fun saveScore() {
        val resultTime = (System.currentTimeMillis() - gameViewModel.startTime) / 1000
        GameScorePreferences(requireContext()).savePreference("${gameViewModel.level.value} ${gameViewModel.errors} $resultTime")
    }

    fun showBoard() {
        for (i in gameViewModel.getCurrentGameBoard()) {
            val button = requireActivity().findViewById(i.second) as ImageButton
            button.setImageResource(i.first)
        }
    }

    fun returnToMainFragment() {
        gameViewModel.resetGame()
        handler.removeCallbacksAndMessages(null)
        findNavController().navigate(R.id.action_gameFragment_to_mainFragment)
    }
}