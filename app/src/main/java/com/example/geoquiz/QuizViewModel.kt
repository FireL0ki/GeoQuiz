package com.example.geoquiz

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"

class QuizViewModel : ViewModel() {

    // set up a variable to keep track of where the current index # is at
    private var currentIndex = 0

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToPrevious() {
        if (currentIndex < 1) {
//            Toast.makeText(this, "No previous questions.", Toast.LENGTH_SHORT).show()
        } else {
            currentIndex = (currentIndex - 1) % questionBank.size
        }
    }


}