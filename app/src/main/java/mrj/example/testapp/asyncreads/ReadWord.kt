package mrj.example.testapp.asyncreads

import android.app.Activity
import android.content.ContentValues
import android.os.AsyncTask
import mrj.example.testapp.activities.MainActivity
import mrj.example.testapp.database.WordDatabase
import mrj.example.testapp.objects.Word
import mrj.example.testapp.utils.WordTable
import org.json.JSONArray
import java.lang.Exception
import java.net.URL


/**
* Created by JavohirAI
*/

class ReadWord(var contextActivity: Activity) : AsyncTask<String, String, String>() {


    override fun doInBackground(vararg args: String?): String? {
        val url = args[0]
        var datas = ""
        try {
            datas = URL(url).readText()
        }catch (e:Exception){
            return ""
        }

        val jsonReader = JSONArray(datas)
        val db = WordDatabase(contextActivity)
        val readable = db.readableDatabase
        var elem_count = 0

        while(elem_count<jsonReader.length()){

            val jsonObject = jsonReader.getJSONObject(elem_count)
            val cv = ContentValues()
            cv.put(WordTable.word_col,jsonObject.getString("word"))
            cv.put(WordTable.wordtype_col,jsonObject.getString("wordtype"))
            cv.put(WordTable.description_col,jsonObject.getString("description"))
            readable.insert(WordTable.TABLE_NAME,null,cv)

            elem_count += 1
        }

        return ""
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        (contextActivity as MainActivity).fillAdapter()
    }
}