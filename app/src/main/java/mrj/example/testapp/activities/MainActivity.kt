package mrj.example.testapp.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import mrj.example.testapp.R
import mrj.example.testapp.adapters.WordAdapter
import mrj.example.testapp.database.DBHelper
import mrj.example.testapp.database.WordDatabase
import mrj.example.testapp.objects.Word
import mrj.example.testapp.utils.Constants
import mrj.example.testapp.utils.WordTable


@Suppress("DEPRECATION", "UNREACHABLE_CODE")
class MainActivity : AppCompatActivity() {

    lateinit var list_word: MutableList<Word>
    lateinit var rv_main: RecyclerView
    lateinit var word_db: DBHelper
    lateinit var readable_db: SQLiteDatabase
    lateinit var fab: FloatingActionButton
    lateinit var mMenu: Menu
    lateinit var searchEditText: EditText
    lateinit var menuItemsearch: MenuItem
    lateinit var txt_download_progress: TextView
    lateinit var dialog_download: Dialog
    lateinit var btn_search: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initialization()

        fillAdapter()
    }

    @SuppressLint("Recycle")
    fun fillAdapter() {

        readable_db.beginTransaction()
        val cursor = readable_db.query(
            WordTable.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null,
            "100"
        )

        if (cursor.count == 0) {
            readable_db.endTransaction()
            return
        }

        fillList(cursor)

        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL)
        rv_main.addItemDecoration(dividerItemDecoration)

        rv_main.adapter = WordAdapter(this, list_word)
        readable_db.endTransaction()

    }

    @SuppressLint("NewApi")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        menuItemsearch = menu!!.findItem(R.id.menu_search)
        showHideHeaders(false)
        mMenu = menu!!
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menu_search -> {
                searchWord()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun searchWord() {
        showHideHeaders(true)
    }

    private fun showHideHeaders(searching: Boolean) {

        if (searching) {
//            searchEditText.visibility = View.VISIBLE
//            txt_appname.visibility = View.GONE
            fab.visibility = View.GONE
            menuItemsearch.setVisible(false)
            searchEditText.requestFocus()

            (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(
                InputMethodManager.SHOW_IMPLICIT,
                0
            )

        } else {
//            searchEditText.visibility = View.GONE
//            txt_appname.visibility = View.VISIBLE
            fab.visibility = View.VISIBLE
            menuItemsearch.setVisible(true)

            (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(
                InputMethodManager.HIDE_IMPLICIT_ONLY,
                0
            )

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

    }

    private fun initialization() {

        fab = findViewById(R.id.fab_search)
        fab.setOnClickListener(View.OnClickListener {
            searchWord()
        })

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
//        val toolbar = (layoutInflater.inflate(R.layout.toolbar, null, false) as Toolbar)
        setSupportActionBar(toolbar)

        searchEditText = findViewById(R.id.searchEditText)
        searchEditText.setOnClickListener {
            searchwithparams(it)
        }
        btn_search = findViewById(R.id.btn_search)
        btn_search.setOnClickListener({ it ->
            searchwithparams(it)
        })



        supportActionBar?.setDisplayShowTitleEnabled(false)

        rv_main = findViewById(R.id.rv_main)
        rv_main.setHasFixedSize(true)

        rv_main.layoutManager = LinearLayoutManager(this)

        list_word = ArrayList()
        word_db = DBHelper(this)
        readable_db = word_db.readableDatabase

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
        }
    }

    @SuppressLint("Recycle")
    private fun fillAdapterSearch() {
        readable_db.beginTransaction()
        val cursor = readable_db.query(
            WordTable.TABLE_NAME,
            null,
            "${WordTable.word_col} LIKE ?",
            arrayOf("%${searchEditText.text.toString()}%"),
            null,
            null,
            null
        )
        fillList(cursor)
        rv_main.adapter!!.notifyDataSetChanged()
        readable_db.endTransaction()
    }

    fun open_word(word: Word) {
        val intent = Intent(this, WordActivty::class.java)
        intent.putExtra(Constants.KEY_EXTRA_WORD, word.name)
        intent.putExtra(Constants.KEY_EXTRA_WORDTYPE, word.wordtype)
        intent.putExtra(Constants.KEY_EXTRA_DESCRIPTION, word.description)
        startActivity(intent)
    }

}