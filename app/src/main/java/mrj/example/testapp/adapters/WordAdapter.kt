package mrj.example.testapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mrj.example.testapp.holders.WordHolder
import mrj.example.testapp.objects.Word
import mrj.example.testapp.R


/**
 * Created by JavohirAI
 */

class WordAdapter : RecyclerView.Adapter<WordHolder> {

    var list_word: List<Word>

    constructor(list_word: List<Word>) : super() {
        this.list_word = list_word
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.itemrv_main_activity, parent, false)

        return WordHolder(view)
    }

    override fun onBindViewHolder(holder: WordHolder, position: Int) {

        val word = list_word.get(position)

        val resources = holder.itemView.resources
        holder.txt_word.text = resources.getString(R.string.text_word,word.name)
        holder.txt_wordtype.text = resources.getString(R.string.text_wordtype,word.wordtype)
        holder.txt_description.text = resources.getString(R.string.text_description,word.description)

    }

    override fun getItemCount(): Int {
        return list_word.size
    }
}