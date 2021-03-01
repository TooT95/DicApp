package mrj.example.testapp.activities

import android.annotation.SuppressLint
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import mrj.example.testapp.adapters.WordAdapter
import mrj.example.testapp.objects.Word
import mrj.example.testapp.R
import mrj.example.testapp.asyncreads.ReadWord
import mrj.example.testapp.database.WordDatabase
import mrj.example.testapp.utils.WordTable

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    lateinit var list_word: MutableList<Word>
    lateinit var rv_main: RecyclerView
    lateinit var word_db: WordDatabase
    lateinit var readable_db: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initialization()

        fillAdapter()

    }

    @SuppressLint("Recycle")
    fun fillAdapter() {

        readable_db.beginTransaction()
        val cursor = readable_db.query(WordTable.TABLE_NAME, null, null, null, null, null, null)

        if (cursor.count == 0) {
            readable_db.endTransaction()
            read_from_url()
            return
        }

        fillList(cursor)

        rv_main.adapter = WordAdapter(list_word)
        readable_db.endTransaction()

    }

    private fun fillList(cursor: Cursor) {

        while (cursor.moveToNext()) {

            val word_index_col = cursor.getColumnIndex(WordTable.word_col)
            val wordtype_index_col = cursor.getColumnIndex(WordTable.wordtype_col)
            val description_index_col = cursor.getColumnIndex(WordTable.description_col)

            val word_value = cursor.getString(word_index_col)
            val wordtype_value = cursor.getString(wordtype_index_col)
            val description_value = cursor.getString(description_index_col)

            val word = Word(word_value, wordtype_value, description_value)
            list_word.add(word)
        }

    }

    private fun initialization() {

        rv_main = findViewById(R.id.rv_main)
        rv_main.layoutManager = LinearLayoutManager(this)

        list_word = ArrayList()
        word_db = WordDatabase(this)
        readable_db = word_db.readableDatabase
    }

    private fun read_from_url(){
        val url = "https://mrjavohirtest.pythonanywhere.com"
        ReadWord(this).execute(url)
    }

}