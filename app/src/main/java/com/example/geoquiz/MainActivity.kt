package com.example.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    //set up view model
    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }

    // set up variables for components
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var previousButton: ImageButton
    private lateinit var questionTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // call log message
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        // initialize component variables
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        previousButton = findViewById(R.id.previous_button)
        questionTextView = findViewById(R.id.question_text_view)


        // set up event listener for buttons, toast pop ups for true/false buttons
        trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
        }

        falseButton.setOnClickListener { view: View ->
            // create toast pop up
            checkAnswer(false)
        }

        // set up nextButton listener-- on click, add one to index (so the question bank
        // moves to the next index number and sets the text to the question at that index
        nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }
        updateQuestion()

        // set up previous button -- move back a question
        previousButton.setOnClickListener {
            // check if there are prior questions to go back to, else previous button doesn't function
            quizViewModel.moveToPrevious()
            updateQuestion()
            }

        // set up event listener (for user click) on the text view / question
        questionTextView.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }
    }

    // these will run when each of the android lifecycle functions are called, and print a log
    // message so we can see how/when these functions are called in the process of using apps
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }
    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }


    // update the question from the questionBank list based on the currentIndex #
    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
    }

    // check if user answer is correct or not
    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer

        // check if answer is correct / incorrect, show appropriate pop up
        val messageResId = if (userAnswer == correctAnswer) {
            R. string.correct_toast
        } else {
            R.string.incorrect_toast
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }

}