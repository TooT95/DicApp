package mrj.example.testapp.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
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
    lateinit var mMenu:Menu
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

    @SuppressLint("NewApi", "ObsoleteSdkInt")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        mMenu = menu!!

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            //remove old
//            menu!!.removeItem(R.id.menu_search)
//            val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
//            val searchView: SearchView = menu!!.findItem(R.id.menu_search).actionView as SearchView
//            searchView.setSearchableInfo(
//                searchManager.getSearchableInfo(componentName)
//            )
//            searchView.setIconifiedByDefault(false)
//        } else {
//            //remove new
//            menu!!.removeItem(R.id.menu_search)
//        }


        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menu_redownload -> {
                showDialog(DIALOG_ID_REDOWNLOAD)
                return true
            }
            R.id.menu_search -> {
                onSearchRequested()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("NewApi", "ObsoleteSdkInt")
    override fun onSearchRequested(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            val mi: MenuItem = mMenu.findItem(R.id.menu_search)
            if (mi.isActionViewExpanded) {
                mi.collapseActionView()
            } else {
                mi.expandActionView()
            }
        } else {
            //onOptionsItemSelected(mMenu.findItem(R.id.search));
        }
        return super.onSearchRequested()
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