package com.boss.numberguess

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.random.Random

class SharedNumberGuessViewModel : ViewModel() {

    private val _answer = MutableStateFlow("??")
    val answer: StateFlow<String> = _answer
    val isGameOver = MutableStateFlow(false)
    val firstSentence = MutableStateFlow("Guess 1 to 25")
    val tries = MutableStateFlow(0)

    fun generateAnswer() {
        _answer.value = Random.nextInt(1,25).toString()
    }

    fun startOver() {
        firstSentence.value = "Guess 1 to 25"
        isGameOver.value = false
        tries.value = 0
        generateAnswer()
    }


}