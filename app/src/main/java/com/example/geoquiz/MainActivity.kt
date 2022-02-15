package com.example.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    // set up variables for components
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var previousButton: ImageButton
    private lateinit var questionTextView: TextView

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    // set up a variable to keep track of where the current index # is at
    private var currentIndex = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }
        updateQuestion()

        // set up previous button -- move back a question
        previousButton.setOnClickListener {
            // check if there are prior questions to go back to, else previous button doesn't function
            if (currentIndex < 1) {
                Toast.makeText(this, "No previous questions.", Toast.LENGTH_SHORT).show()
            } else {
                currentIndex = (currentIndex - 1) % questionBank.size
                updateQuestion()
            }
        }

        // set up event listener (for user click) on the text view / question
        questionTextView.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }

    }


    // update the question from the questionBank list based on the currentIndex #
    private fun updateQuestion() {
        val questionTextResId = questionBank[currentIndex].textResId
        questionTextView.setText(questionTextResId)
    }

    // check if user answer is correct or not
    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = questionBank[currentIndex].answer

        // check if answer is correct / incorrect, show appropriate pop up
        val messageResId = if (userAnswer == correctAnswer) {
            R. string.correct_toast
        } else {
            R.string.incorrect_toast
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }

}