package com.example.gamelayouttest

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import java.util.Random

class GameActivity : ComponentActivity() {
    lateinit var builder: AlertDialog.Builder
    var guess: String = ""
    lateinit var buttons: Array<Button>
    lateinit var word: String
    var score=0
    lateinit var guessbox: TextView
    lateinit var clue: String
    lateinit var reset: Button
    lateinit var hint: ImageButton
    lateinit var check:Button
    var life=3
    lateinit var lifepic: Array<ImageView>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_layout)
        builder = AlertDialog.Builder(this).setCancelable(false)
        guessbox = findViewById(R.id.guessbox)
        reset = findViewById(R.id.reset_button)
        hint = findViewById(R.id.hint)
        check = findViewById(R.id.check_button)
        word = intent.getStringExtra("word").toString().trim()
        clue = intent.getStringExtra("clue").toString().trim()
        lifepic=arrayOf(findViewById(R.id.life1),findViewById(R.id.life2),findViewById(R.id.life3))


        //button declaration
        buttons = Array<Button>(16) { i ->
            findViewById<Button>(
                resources.getIdentifier(
                    "click${i + 1}",
                    "id",
                    "com.example.gamelayouttest"
                )
            )
        }

        for (but in buttons) {
            but.setOnClickListener() { clickbehave(but) }
        }
        jumble()
        check.setOnClickListener() {
            if (guess.isEmpty()){Toast.makeText(this,"Guess is empty",Toast.LENGTH_SHORT).show()}
            else{checkClick()}
        }
        reset.setOnClickListener() { resetter() }
        hint.setOnClickListener { clueOpener() }
    }

    override fun onBackPressed() {
        moveTaskToBack(false)
    }

    private fun clickbehave(click: Button) {
        click.setBackgroundResource(R.drawable.g_press)
        click.setTextColor(ContextCompat.getColor(this,R.color.white))

        click.isEnabled = false
        guess += click.text.toString()
        guessbox.text = guess

    }
    private fun checkClick() {
        if (guessbox.text.toString().equals(word)) {
            gameEnd()

        }
        else{jumble()

            for (but in buttons) {
                but.setBackgroundResource(R.drawable.g_notpress)
                but.isEnabled = true
                but.setTextColor(ContextCompat.getColor(this,R.color.black))
            }

            guess = ""
            guessbox.text = guess
            life -= 1
            Toast.makeText(this, "Incorrect Guess! Rem life=$life", Toast.LENGTH_SHORT).show()
            lifepic[life].setColorFilter(R.color.grey)
            if(life==0){
                gameEnd()}


        }
    }


    private fun clueOpener() {
        var clueOpen = View.inflate(this, R.layout.clue_dialog_layout, null)
        var clueTextView = clueOpen.findViewById<TextView>(R.id.clue_show)
        var close: Button = clueOpen.findViewById(R.id.okay)
        clueTextView.text = clue
        clueTextView.visibility = View.VISIBLE


        builder.setView(clueOpen)

        var clueDialog = builder.create()
        clueDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        clueDialog.show()

        close.setOnClickListener { clueDialog.dismiss() }//                                  close the clue


    }


    private fun resetter() {
        Toast.makeText(this, "Input Reset", Toast.LENGTH_SHORT).show()
        for (but in buttons) {
            but.setBackgroundResource(R.drawable.g_notpress)
            but.isEnabled = true
            but.setTextColor(ContextCompat.getColor(this,R.color.black))
        }

        guess = ""
        guessbox.text = guess
    }

    private fun jumble() {
        val jumbleButton =buttons.toMutableList().apply { shuffle(Random(System.currentTimeMillis())) }

        var len = word.length
        for (i in 0 until len) {
            jumbleButton[i].text = word[i].toString()
        }
        for (i in len until buttons.size) {
            jumbleButton[i].text = ('A'..'Z').random().toString()

        }
    }

    private fun gameEnd() {
        Toast.makeText(this, "Game Over", Toast.LENGTH_SHORT).show()
        var gameOverOpen = View.inflate(this, R.layout.gameover_layout, null)
        var scorebox: TextView = gameOverOpen.findViewById(R.id.score)
        gameOverOpen.visibility = View.VISIBLE
        score = life * 100
        var scoretext = "Your score=$score"
        scorebox.text = scoretext
        builder.setView(gameOverOpen)
        var gameOverDialog = builder.create()
        gameOverDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        gameOverDialog.show()
        var hom: Button = gameOverOpen.findViewById(R.id.home)
        var play: Button = gameOverOpen.findViewById(R.id.play)
        datastore(score)

        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        hom.setOnClickListener {
            startActivity(intent)
        }
        play.setOnClickListener {
            startActivity(intent)        }
    }


    private fun datastore(score:Int) {

        val sharedPref = getSharedPreferences("highscore", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        var exist:Int=sharedPref.getInt("bestscore",0)
        if(score>exist){
        editor.putInt("bestscore", score)
        editor.apply()}
    }


}

