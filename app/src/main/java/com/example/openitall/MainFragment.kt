package com.example.openitall

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.openitall.databinding.MainFragmentBinding
import com.example.openitall.databinding.ScorePopupBinding


class MainFragment : Fragment() {
    private var mainFragmentBinding: MainFragmentBinding? = null
    private lateinit var scoreView: View
    private var scoreViewBinding: ScorePopupBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mainFragment = MainFragmentBinding.inflate(inflater, container, false)
        mainFragmentBinding = mainFragment
        scoreView = inflater.inflate(R.layout.score_popup, container, false)
        val popupView = ScorePopupBinding.inflate(inflater, container, false)
        scoreViewBinding = popupView
        return mainFragment.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainFragmentBinding?.mainFragment = this
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mainFragmentBinding = null
        scoreViewBinding = null
    }

    fun goToNextScreen() {
        findNavController().navigate(R.id.action_mainFragment_to_gameFragment)
    }

    fun showScore() {
        if (scoreView.parent != null) {
            val a = scoreView.parent as ViewGroup
            a.removeView(scoreView)
        }
        val popUp = PopupWindow(
            scoreView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
        )
        popUp.showAtLocation(scoreView, Gravity.CENTER, 0, 0)

        setTextForScore()
        scoreView.setOnTouchListener { v, _ ->
            popUp.dismiss()
            v.performClick()
        }
    }

    private fun setTextForScore() {
        val gamePreferences = GameScorePreferences(requireContext())
        scoreView.findViewById<TextView>(R.id.first_score).text = gamePreferences.getScore(0)
        scoreView.findViewById<TextView>(R.id.second_score).text = gamePreferences.getScore(1)
        scoreView.findViewById<TextView>(R.id.third_score).text = gamePreferences.getScore(2)
        scoreView.findViewById<TextView>(R.id.fourth_score).text = gamePreferences.getScore(3)
        scoreView.findViewById<TextView>(R.id.fifth_score).text = gamePreferences.getScore(4)
    }

}