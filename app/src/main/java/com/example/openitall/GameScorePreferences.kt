package com.example.openitall

import android.content.Context

class GameScorePreferences(private val context: Context) {
    private val PREFS_NAME = "score"
    private val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val prefEditor = sharedPreferences.edit()

    fun savePreference(result: String) {
        val allPrefs = sharedPreferences.all
        if (allPrefs == null) {
            with(prefEditor) {
                putString("0", result)
                apply()
            }
        } else updatePrefs(result)
    }

    private fun updatePrefs(newResult: String) {
        val parsedValues = mutableListOf<List<Int>>()
        sharedPreferences.all.values.forEach { item ->
            val value = item as String
            parsedValues.add(value.split(" ").map { it.toInt() })
        }
        parsedValues.add(newResult.split(" ").map { it.toInt() })
        val sortedValues = parsedValues.sortedWith(compareBy<List<Int>> { it[0] }.thenByDescending { it[1] }.thenByDescending { it[2] })
        val descendingSorted = sortedValues.reversed()
        for (i in descendingSorted.indices) {
            if (i > 4) break
            val line = descendingSorted[i]
            val stringToPut = "${line[0]} ${line[1]} ${line[2]}"
            prefEditor.putString(i.toString(), stringToPut)
            prefEditor.apply()
        }
    }

    //[0] == level, [1] == quantity of mistakes, [2] == time
    fun getScore(numberOfScore: Int): String {
        val score = sharedPreferences.getString(numberOfScore.toString(), null)
        val parsed = score?.split(" ")?.map { it.toInt() } ?: listOf(0, 0, 0)
        return "Level:${parsed[0]}  Errors:${parsed[1]}  Time:${parsed[2]} sec"
    }
}