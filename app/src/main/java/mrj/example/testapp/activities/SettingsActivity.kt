package mrj.example.testapp.activities

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import mrj.example.testapp.R


/**
 * Created by JavohirAI
 */

class SettingsActivity : AppCompatActivity() {

    lateinit var txt_word: TextView
    lateinit var txt_wordtype: TextView
    lateinit var iv_word_up: ImageView
    lateinit var iv_word_down: ImageView
    lateinit var iv_wordtype_up: ImageView
    lateinit var iv_wordtype_down: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        initUI()

        fillData()
    }

    private fun initUI() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        txt_word = findViewById(R.id.txt_word_size)
        txt_wordtype = findViewById(R.id.txt_wordtype_size)

        iv_word_up = findViewById(R.id.iv_word_up)
        iv_word_down = findViewById(R.id.iv_word_down)
        iv_wordtype_up = findViewById(R.id.iv_wordtype_up)
        iv_wordtype_down = findViewById(R.id.iv_wordtype_down)

    }

    private fun fillData() {
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

    private fun setSizeTextView(txt: TextView, defaultValue: Int) {
        txt.textSize = defaultValue.toFloat()
        txt.text = defaultValue.toString()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val sharedPref =
            getSharedPreferences(resources.getString(R.string.name_shared_preferences), 0)



        when (item.itemId) {
            android.R.id.home -> {
                val edit = sharedPref.edit()
                edit.putInt(resources.getString(R.string.key_word_size),txt_word.text.toString().toInt())
                edit.putInt(resources.getString(R.string.key_wordtype_size),txt_wordtype.text.toString().toInt())
                edit.apply()

                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun onChangeSize(view: View) {

        when (view) {
            iv_word_down -> {
                changeSize(txt_word,false)
            }
            iv_word_up -> {
                changeSize(txt_word,true)
            }
            iv_wordtype_down -> {
                changeSize(txt_wordtype,false)
            }
            iv_wordtype_up -> {
                changeSize(txt_wordtype,true)
            }
        }
    }

    private fun changeSize(txt:TextView,sign:Boolean) {
        var size = txt.text.toString().toInt()
        if(sign){
            size += 1
        }else{
            size -= 1
        }
        setSizeTextView(txt, size)
    }


}