package mrj.example.testapp.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import mrj.example.testapp.R
import mrj.example.testapp.adapters.WordAdapter
import mrj.example.testapp.asyncreads.ReadWord
import mrj.example.testapp.database.WordDatabase
import mrj.example.testapp.objects.Word
import mrj.example.testapp.utils.Constants
import mrj.example.testapp.utils.WordTable


@Suppress("DEPRECATION", "UNREACHABLE_CODE")
class MainActivity : AppCompatActivity() {

    lateinit var list_word: MutableList<Word>
    lateinit var rv_main: RecyclerView
    lateinit var word_db: WordDatabase
    lateinit var readable_db: SQLiteDatabase
    lateinit var fab: FloatingActionButton
    lateinit var mMenu: Menu
    lateinit var searchEditText: EditText
    lateinit var txt_appname: TextView
    lateinit var menuItemsearch: MenuItem
    lateinit var menu_redownload: MenuItem

    val DIALOG_ID_REDOWNLOAD = 1

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

        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL)
        rv_main.addItemDecoration(dividerItemDecoration)

        rv_main.adapter = WordAdapter(this, list_word)
        readable_db.endTransaction()

    }

    @SuppressLint("NewApi")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        menuItemsearch = menu!!.findItem(R.id.menu_search)
        menu_redownload = menu!!.findItem(R.id.menu_redownload)
        showHideHeaders(false)
        mMenu = menu!!
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menu_redownload -> {
                showDialog(DIALOG_ID_REDOWNLOAD)
                return true
            }
            R.id.menu_search -> {
                searchWord()
//                val intent = Intent(this,SearchableActivity::class.java)
//                startActivity(intent)
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
            searchEditText.visibility = View.VISIBLE
            txt_appname.visibility = View.GONE
            fab.visibility = View.GONE
            menuItemsearch.setVisible(false)
            menu_redownload.setVisible(false)
            searchEditText.requestFocus();
        } else {
            searchEditText.visibility = View.GONE
            txt_appname.visibility = View.VISIBLE
            fab.visibility = View.VISIBLE
            menuItemsearch.setVisible(true)
            menu_redownload.setVisible(true)
        }

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

        fab = findViewById(R.id.fab_search)
        fab.setOnClickListener(View.OnClickListener {
            searchWord()
        })

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        searchEditText = findViewById(R.id.searchEditText)
        searchEditText.setOnFocusChangeListener { view, b ->
            if (b.equals(false)) {
                (view as EditText).text.clear()
                showHideHeaders(false)
            }
        }
        txt_appname = findViewById(R.id.txt_app_name)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        rv_main = findViewById(R.id.rv_main)
        rv_main.setHasFixedSize(true)

        rv_main.layoutManager = LinearLayoutManager(this)

        list_word = ArrayList()
        word_db = WordDatabase(this)
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

    private fun read_from_url() {

        val url = "https://mrjavohirtest.pythonanywhere.com"
        ReadWord(this).execute(url)

    }

    fun open_word(word: Word) {
        val intent = Intent(this, WordActivty::class.java)
        intent.putExtra(Constants.KEY_EXTRA_WORD, word.name)
        intent.putExtra(Constants.KEY_EXTRA_WORDTYPE, word.wordtype)
        intent.putExtra(Constants.KEY_EXTRA_DESCRIPTION, word.description)
        startActivity(intent)
    }

}