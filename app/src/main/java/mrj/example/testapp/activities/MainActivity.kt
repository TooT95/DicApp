package mrj.example.testapp.activities

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import mrj.example.testapp.R
import mrj.example.testapp.adapters.WordAdapter
import mrj.example.testapp.database.DBHelper
import mrj.example.testapp.database.HistoryDatabase
import mrj.example.testapp.objects.Word
import mrj.example.testapp.utils.Constants
import mrj.example.testapp.utils.WordTable


@Suppress("DEPRECATION", "UNREACHABLE_CODE")
class MainActivity : AppCompatActivity() {

    lateinit var list_word: MutableList<Word>
    lateinit var rv_main: RecyclerView
    lateinit var word_db: DBHelper
    lateinit var history_db: HistoryDatabase
    lateinit var readable_db: SQLiteDatabase
    lateinit var readable_history_db: SQLiteDatabase
    lateinit var fab: FloatingActionButton
    lateinit var searchEditText: EditText
    lateinit var iv_search: ImageView
    lateinit var iv_search_off: ImageView
    lateinit var txt_list_empty: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initialization()

        fillAdapter()

        showHideHeaders(false)
    }

    @SuppressLint("Recycle")
    fun fillAdapter() {

        readable_history_db.beginTransaction()
        val cursor = readable_history_db.query(
            WordTable.TABLE_HISTORY,
            null,
            null,
            null,
            null,
            null,
            null,
            "100"
        )

        if (cursor.count == 0) {
            showHideElements(true)
            readable_history_db.endTransaction()
            return
        }

        showHideElements(false)

        fillList(cursor)

        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL)
        rv_main.addItemDecoration(dividerItemDecoration)

        rv_main.adapter = WordAdapter(this, list_word)
        readable_history_db.endTransaction()

    }

    private fun searchWord() {
        showHideHeaders(true)
    }

    private fun showHideHeaders(searching: Boolean) {

        fab.visibility = View.GONE

        if (searching) {


            searchEditText.requestFocus()

            (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(
                InputMethodManager.SHOW_IMPLICIT,
                0
            )

        } else {
//            fab.visibility = View.VISIBLE
            iv_search_off.visibility = View.GONE

            (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(
                InputMethodManager.HIDE_IMPLICIT_ONLY,
                0
            )
        }

        if (searchEditText.text.isEmpty()) {
            iv_search_off.visibility = View.GONE
        } else {
            iv_search_off.visibility = View.VISIBLE
        }

    }

    private fun showHideElements(list_empty: Boolean) {
        if (list_empty) {
            txt_list_empty.visibility = View.VISIBLE
            rv_main.visibility = View.GONE
        } else {
            txt_list_empty.visibility = View.GONE
            rv_main.visibility = View.VISIBLE
        }
    }

    private fun fillList(cursor: Cursor) {

        list_word.clear()

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

        showHideElements(list_word.isEmpty())

    }

    private fun initialization() {

        fab = findViewById(R.id.fab_search)
        fab.setOnClickListener(View.OnClickListener {
            searchWord()
        })
        txt_list_empty = findViewById(R.id.txt_list_empty)

        searchEditText = findViewById(R.id.searchEditText)

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if(s.isEmpty()){
                    fillAdapter()
                }else{
                    fillAdapterSearch()
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }
        })

//        searchEditText.setOnClickListener {
//            searchwithparams(it)
//        }

        iv_search = findViewById(R.id.iv_search)
        iv_search.setOnClickListener({ it ->
            searchwithparams(it)
        })

        iv_search_off = findViewById(R.id.iv_search_off)
        iv_search_off.setOnClickListener({
            searchEditText.text.clear()
            searchwithparams(searchEditText)
        })

        supportActionBar?.setDisplayShowTitleEnabled(false)

        rv_main = findViewById(R.id.rv_main)
        rv_main.setHasFixedSize(true)

        rv_main.layoutManager = LinearLayoutManager(this)

        list_word = ArrayList()
        word_db = DBHelper(this)
        readable_db = word_db.readableDatabase

        history_db = HistoryDatabase(this)
        readable_history_db = history_db.readableDatabase

        rv_main.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 || dy < 0 && fab.isShown) fab.hide()
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) fab.show()
                super.onScrollStateChanged(recyclerView, newState)
            }
        })

    }

    private fun searchwithparams(it: View?) {
        val etxt = searchEditText

        if (!etxt.isPressed) {
            showHideHeaders(false)
            if (etxt.text.isEmpty()) {
                fillAdapter()
                return
            }
            fillAdapterSearch()
        } else {

        }
        if (etxt.isFocused) {
            iv_search.visibility = View.VISIBLE
        } else {
            iv_search.visibility = View.GONE
        }

    }

    @SuppressLint("Recycle")
    private fun fillAdapterSearch() {
        readable_db.beginTransaction()
        val cursor = readable_db.query(
            WordTable.TABLE_NAME,
            null,
            "${WordTable.word_col} LIKE ?",
            arrayOf("${searchEditText.text.toString()}%"),
            null,
            null,
            null
        )
        fillList(cursor)
        if (rv_main.adapter == null) {
            rv_main.adapter = WordAdapter(this, list_word)
        } else {
            rv_main.adapter!!.notifyDataSetChanged()
        }
        readable_db.endTransaction()
    }

    fun open_word(word: Word) {

        // check after insert
        val args = arrayOf(word.name, word.wordtype, word.description)
        val cursor = readable_history_db.query(
            WordTable.TABLE_HISTORY, null,
            "${WordTable.word_col} = ? AND ${WordTable.wordtype_col} = ? AND ${WordTable.description_col} = ?",
            args, null, null, null
        )

        if (cursor.count == 0) {
            val cv = ContentValues()
            cv.put(WordTable.word_col, word.name)
            cv.put(WordTable.wordtype_col, word.wordtype)
            cv.put(WordTable.description_col, word.description)
            readable_history_db.insert(WordTable.TABLE_HISTORY, null, cv)
        }


        val intent = Intent(this, WordActivty::class.java)
        intent.putExtra(Constants.KEY_EXTRA_WORD, word.name)
        intent.putExtra(Constants.KEY_EXTRA_WORDTYPE, word.wordtype)
        intent.putExtra(Constants.KEY_EXTRA_DESCRIPTION, word.description)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_clear_history -> clearHistory()
            R.id.menu_settings -> open_settings()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun open_settings() {

    }

    private fun clearHistory() {

        val deleteditems = readable_history_db.delete(WordTable.TABLE_HISTORY, null, null)
        Toast.makeText(applicationContext, "Deleted items: ${deleteditems}", Toast.LENGTH_SHORT)
            .show()

        fillAdapter()

    }
}