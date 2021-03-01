package mrj.example.testapp.activities

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import mrj.example.testapp.R
import mrj.example.testapp.utils.Constants


/**
 * Created by JavohirAI
 */

class WordActivty : AppCompatActivity() {

    lateinit var txt_word: TextView
    lateinit var txt_wordtype: TextView
    lateinit var txt_description: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_word)

        fillActivity()

        if (!intent.equals(null)) {
            var word = intent.extras?.getString(Constants.KEY_EXTRA_WORD)
            var wordtype = intent.extras?.getString(Constants.KEY_EXTRA_WORDTYPE)
            var description = intent.extras?.getString(Constants.KEY_EXTRA_DESCRIPTION)

            txt_word.text = word
            txt_wordtype.text = wordtype
            txt_description.text = description
        }

    }

    private fun fillActivity() {
        txt_word = findViewById(R.id.txt_word)
        txt_wordtype = findViewById(R.id.txt_wordtype)
        txt_description = findViewById(R.id.txt_description)
    }
}