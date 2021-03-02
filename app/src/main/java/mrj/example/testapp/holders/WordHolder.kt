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
    }
}