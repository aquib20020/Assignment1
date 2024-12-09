package com.example.scoresteal

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var scoreTextView: TextView
    private lateinit var winningMessageTextView: TextView
    private lateinit var incrementButton: Button
    private lateinit var decrementButton: Button
    private lateinit var resetButton: Button

    private var currentScore = 0
    private var winSoundPlayer: MediaPlayer? = null
    private var clickSoundPlayer: MediaPlayer? = null

    companion object {
        private const val TAG = "ScoreSteal"
        private const val MAX_SCORE = 15
        private const val CURRENT_SCORE_KEY = "CURRENT_SCORE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.i(TAG, "Activity created")

        // Initialize views
        scoreTextView = findViewById(R.id.display)
        winningMessageTextView = findViewById(R.id.tv_winning_message)
        incrementButton = findViewById(R.id.inc)
        decrementButton = findViewById(R.id.dec)
        resetButton = findViewById(R.id.res)

        // Restore saved score
        currentScore = savedInstanceState?.getInt(CURRENT_SCORE_KEY, 0) ?: 0
        updateUI()

        // Initialize sounds
        winSoundPlayer = MediaPlayer.create(this, R.raw.win)
        clickSoundPlayer = MediaPlayer.create(this, R.raw.click)

        // Set up button listeners
        setupButtonListeners()
    }

    private fun setupButtonListeners() {
        incrementButton.setOnClickListener { handleScoreChange(1) }
        decrementButton.setOnClickListener { handleScoreChange(-1) }
        resetButton.setOnClickListener { resetScore() }
    }

    private fun handleScoreChange(delta: Int) {
        currentScore += delta
        currentScore = currentScore.coerceIn(0, MAX_SCORE) // Ensure score stays within valid bounds

        if (currentScore == MAX_SCORE) {
            Log.i(TAG, "Max score reached. Playing win sound.")
            playWinSound()
            scoreTextView.setTextColor(Color.GREEN)
            decrementButton.isEnabled = false
            winningMessageTextView.text = getString(R.string.winning_message) // Display "That's a Wrap"
        } else {
            Log.i(TAG, "Score updated to $currentScore")
            resetUIForNormalPlay()
        }
        updateUI()
    }

    private fun resetScore() {
        currentScore = 0
        Log.i(TAG, "Score reset to 0.")
        resetUIForNormalPlay()
        updateUI()
    }

    private fun resetUIForNormalPlay() {
        scoreTextView.setTextColor(Color.BLACK)
        decrementButton.isEnabled = true
        winningMessageTextView.text = "" // Clear winning message
    }

    private fun updateUI() {
        scoreTextView.text = currentScore.toString()
    }

    private fun playWinSound() {
        winSoundPlayer?.start()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(CURRENT_SCORE_KEY, currentScore)
        Log.d(TAG, "Score saved: $currentScore")
    }

    override fun onDestroy() {
        super.onDestroy()
        winSoundPlayer?.release()
        clickSoundPlayer?.release()
        Log.i(TAG, "Media players released.")
    }
}
