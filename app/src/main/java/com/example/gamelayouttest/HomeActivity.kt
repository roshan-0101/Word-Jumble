package com.example.gamelayouttest

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.os.Bundle
import android.widget.Toast

class HomeActivity : Activity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_layout)
        var word_in: EditText = findViewById(R.id.word_in)
        var clue_in: EditText = findViewById(R.id.clue_in)
        var start: Button = findViewById(R.id.start_button)
        var highscore: TextView = findViewById(R.id.highscore)
        highscore.text = "High Score=${loaddata()}"
//Game starts
        start.setOnClickListener {

            var word = word_in.text.toString().uppercase()
            var clue = clue_in.text.toString()
            //game starts if no errors are found
            if (word.isNotEmpty() && word.length <= 16 && clue.isNotEmpty()) {
                if (valid(word)) {
                    val intent = Intent(this, GameActivity::class.java)
                    intent.putExtra("word", word)
                    intent.putExtra("clue", clue)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "⚠Word Contains Invalid Characters", Toast.LENGTH_SHORT).show()
                }
            }
            //error response
            else {
                if (clue.isEmpty()) {
                    Toast.makeText(this, "⚠Clue Empty! Please Fill It ", Toast.LENGTH_SHORT).show()
                }
                if (word.isEmpty()) {
                    Toast.makeText(this, "⚠Word Empty! Please Fill It ", Toast.LENGTH_SHORT).show()
                }
                if (word.length > 16) {
                    Toast.makeText(this, "⚠Word More Than 16 Letters  ", Toast.LENGTH_SHORT).show()

                }
            }
        }
    }

    private fun loaddata(): Int {
        val prefs = getSharedPreferences("highscore", Context.MODE_PRIVATE)
        return prefs.getInt("bestscore", 0)
    }

    fun valid(str: String): Boolean {
        for (i in str) {
            if (!i.isLetter()) {
                return false
            }
        }
        return true
    }
}
