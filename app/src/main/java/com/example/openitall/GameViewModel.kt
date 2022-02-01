package com.example.openitall

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    private val _level = MutableLiveData(1)
    val level: LiveData<Int> = _level
    private var indexOfCurrentAnswer = 0
    private final val INDEX_FINAL_TILE = 14
    var errors = 0
    var startTime = 0L

    //first Number, second Tile
    private var currentLevelBoard = mutableListOf<Pair<Int, Int>>()

    val tiles = listOf(
        R.id.first_tile,
        R.id.second_tile,
        R.id.third_tile,
        R.id.forth_tile,
        R.id.fifth_tile,
        R.id.sixth_tile,
        R.id.seventh_tile,
        R.id.eighth_tile,
        R.id.ninth_tile,
        R.id.tenth_tile,
        R.id.eleventh_tile,
        R.id.twelfth_tile,
        R.id.thirteenth_tile,
        R.id.fourteenth_tile,
        R.id.fifteenth_tile
    )
    private val numbers = listOf(
        R.drawable.ic_one,
        R.drawable.ic_two,
        R.drawable.ic_three,
        R.drawable.ic_four,
        R.drawable.ic_five,
        R.drawable.ic_six,
        R.drawable.ic_seven,
        R.drawable.ic_eight,
        R.drawable.ic_nine,
        R.drawable.ic_ten,
        R.drawable.ic_eleven,
        R.drawable.ic_twelve,
        R.drawable.ic_thirteen,
        R.drawable.ic_fourteen,
        R.drawable.ic_fifteen
    )

    fun resetGame() {
        _level.value = 1
        indexOfCurrentAnswer = 0
        errors = 0
        startTime = 0L
    }

    fun initializeGameBoard(){
        val quantity: Int = level.value!!.toInt()
        val tilesToReturn = tiles.shuffled().take(quantity)
        val numbersToReturn = numbers.take(quantity)
        val pairTileNumber = mutableListOf<Pair<Int, Int>>().apply {
            for (i in tilesToReturn.indices) {
                add(Pair(numbersToReturn[i], tilesToReturn[i]))
            }
        }
        currentLevelBoard = pairTileNumber
    }

    fun getCurrentGameBoard(): List<Pair<Int, Int>> = currentLevelBoard

    fun isAnswerCorrect(tile: Int): Boolean {
        return indexOfCurrentAnswer < currentLevelBoard.size &&
            currentLevelBoard[indexOfCurrentAnswer].second == tile
    }

    fun levelUP() {
        _level.value = _level.value?.inc()
        indexOfCurrentAnswer = 0
    }

    fun isLevelCompleted(): Boolean = indexOfCurrentAnswer == currentLevelBoard.size

    fun isGameCompleted(): Boolean = indexOfCurrentAnswer == INDEX_FINAL_TILE

    fun correctAnswer(): Pair<Int, Int> = currentLevelBoard[indexOfCurrentAnswer]

    fun nextStep() {
        indexOfCurrentAnswer++
    }

}