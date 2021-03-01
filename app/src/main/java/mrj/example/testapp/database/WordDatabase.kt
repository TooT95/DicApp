package mrj.example.testapp.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import mrj.example.testapp.utils.Constants
import mrj.example.testapp.utils.WordTable


/**
 * Created by JavohirAI
 */

class WordDatabase(context: Context?) :
    SQLiteOpenHelper(context, Constants.DB_NAME, null, Constants.DB_VERSION) {


    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_WORDS_TABLE = ("CREATE TABLE " + WordTable.TABLE_NAME + "("
                + WordTable.id_col + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + WordTable.word_col + " TEXT,"
                + WordTable.wordtype_col + " TEXT,"
                + WordTable.description_col + " TEXT" + ")")
        db?.execSQL(CREATE_WORDS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldversion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
}