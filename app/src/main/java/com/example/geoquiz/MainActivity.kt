package com.example.geoquiz

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
private const val REQUEST_CODE_CHEAT = 0

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

    //set up new cheat button for connecting new activity with cheat screen
    private lateinit var cheatButton: Button

    // dealing with minimum API level 23 error for animation code
    @SuppressLint("RestrictedApi")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // call log message
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        // check for this value (from line 101 // **gamma)
        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex


        // initialize component variables
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        previousButton = findViewById(R.id.previous_button)
        questionTextView = findViewById(R.id.question_text_view)

        // connect cheatButton for new cheatActivity
        cheatButton = findViewById(R.id.cheat_button)


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
            val currentIndex = quizViewModel.currentIndex
            if (currentIndex < 1) {
            Toast.makeText(this, "No previous questions.", Toast.LENGTH_SHORT).show()
            } else {
                quizViewModel.moveToPrevious()
                updateQuestion()
            }
        }

        // set up event listener (for user click) on the text view / question
        questionTextView.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }

        // set up event listener for cheatButton to start up new activity when clicked
        cheatButton.setOnClickListener { view ->
            // start CheatActivity
//            val intent = Intent(this, CheatActivity::class.java)
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)

            // add reveal animation when CheatActivity comes on screen
            // check first if minimum SDK is in use for this animation
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val options = ActivityOptions
                .makeClipRevealAnimation(view, 0, 0, view.width, view.height)
//            startActivity(intent)
            // modify cheatButton to call startActivityForResult(Intent, int)
            startActivityForResult(intent, REQUEST_CODE_CHEAT) // deprecated
            } else {
                startActivityForResult(intent, REQUEST_CODE_CHEAT)
        }
        }
        updateQuestion()
    }

    // override onActivityResult() to grab the value from the result sent from CheatActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // if the resultCode does not equal the predetermined code RESULT_OK
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            quizViewModel.isCheater =
                data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
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

    // **gamma -- override onSaveInstanceState(Bundle) to write the value of currentIndex
    // to the bundle with the constant as its key
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
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
        // for testing
//        Log.d(TAG, "Updating question text", Exception())
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
    }

    // check if user answer is correct or not
    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer
//        // check if answer is correct / incorrect, show appropriate pop up
//        val messageResId = if (userAnswer == correctAnswer) {
//            R. string.correct_toast
//        } else {
//            R.string.incorrect_toast
//        }

        // modify checkAnswer(Boolean) function to check whether user cheated, & respond
        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }

}