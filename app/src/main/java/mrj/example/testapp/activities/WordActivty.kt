package mrj.example.testapp.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
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

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        fillActivity()

        initDetails()

    }

    private fun initDetails() {
        if (!intent.equals(null)) {
            var word = intent.extras?.getString(Constants.KEY_EXTRA_WORD)
            var wordtype = intent.extras?.getString(Constants.KEY_EXTRA_WORDTYPE)
            var description = intent.extras?.getString(Constants.KEY_EXTRA_DESCRIPTION)

            txt_word.text = resources.getString(R.string.text_word, word)
            txt_wordtype.text = resources.getString(R.string.text_wordtype, wordtype)
            txt_description.text = resources.getString(R.string.text_description, description)
        }
    }

    private fun fillActivity() {

//        val toolbar = (layoutInflater.inflate(R.layout.toolbar, null, false) as Toolbar)
//        setSupportActionBar(toolbar)

        txt_word = findViewById(R.id.txt_word)
        txt_wordtype = findViewById(R.id.txt_wordtype)
        txt_description = findViewById(R.id.txt_description)

        val sharedPref =
            getSharedPreferences(resources.getString(R.string.name_shared_preferences), 0)
        var defaultValue = resources.getInteger(R.integer.text_word_size)
        var textsize = sharedPref.getInt(resources.getString(R.string.key_word_size), defaultValue)

        if (textsize.equals(defaultValue)) {
            setSizeTextView(txt_word, defaultValue)
        } else {
            setSizeTextView(txt_word, textsize)
        }

        defaultValue = resources.getInteger(R.integer.text_wordtype_size)
        textsize = sharedPref.getInt(resources.getString(R.string.key_wordtype_size), defaultValue)

        if (textsize.equals(defaultValue)) {
            setSizeTextView(txt_wordtype, defaultValue)
        } else {
            setSizeTextView(txt_wordtype, textsize)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            android.R.id.home -> finish()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setSizeTextView(txt: TextView, defaultValue: Int) {
        txt.textSize = defaultValue.toFloat()
        txt.text = defaultValue.toString()
    }

}