package mrj.example.testapp.holders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mrj.example.testapp.R


/**
 * Created by JavohirAI
 */

class WordHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var txt_word: TextView
    var txt_wordtype: TextView

    init {

        txt_word = itemView.findViewById(R.id.txt_word)
        txt_wordtype = itemView.findViewById(R.id.txt_wordtype)

        val context = itemView.context
        val resources = context.resources

        val sharedPref = context.getSharedPreferences(resources.getString(R.string.name_shared_preferences),0)
        var defaultValue = resources.getInteger(R.integer.text_word_size)
        var textsize = sharedPref.getInt(resources.getString(R.string.key_word_size), defaultValue)

        if(textsize.equals(defaultValue)){
            txt_word.textSize = defaultValue.toFloat()
        }else{
            txt_word.textSize = textsize.toFloat()
        }

        defaultValue = resources.getInteger(R.integer.text_wordtype_size)
        textsize = sharedPref.getInt(resources.getString(R.string.key_wordtype_size), defaultValue)

        if(textsize.equals(defaultValue)){
            txt_wordtype.textSize = defaultValue.toFloat()
        }else{
            txt_wordtype.textSize = textsize.toFloat()
        }

    }
}