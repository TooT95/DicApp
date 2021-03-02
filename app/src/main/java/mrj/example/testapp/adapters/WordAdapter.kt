package mrj.example.testapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import mrj.example.testapp.holders.WordHolder
import mrj.example.testapp.objects.Word
import mrj.example.testapp.R
import mrj.example.testapp.activities.MainActivity


/**
 * Created by JavohirAI
 */

class WordAdapter : RecyclerView.Adapter<WordHolder>, View.OnClickListener {

    var list_word: List<Word>
    var context: Context

    constructor(context: Context, list_word: List<Word>) : super() {
        this.list_word = list_word
        this.context = context
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.itemrv_main_activity, parent, false)

        view.setOnClickListener(this)

        return WordHolder(view)
    }

    override fun onBindViewHolder(holder: WordHolder, position: Int) {

        val word = list_word.get(position)

        val resources = holder.itemView.resources
        holder.itemView.tag = position
        holder.txt_word.text = resources.getString(R.string.text_word, word.name)
        holder.txt_wordtype.text = resources.getString(R.string.text_wordtype, word.wordtype)

    }

    override fun getItemCount(): Int {
        return list_word.size
    }

    override fun onClick(view: View?) {
        val position = (view?.tag) as Int

        val word = list_word.get(position)
        (context as MainActivity).open_word(word)
    }
}