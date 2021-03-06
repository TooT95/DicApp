package mrj.example.testapp.asyncreads

import android.app.Activity
import android.content.ContentValues
import android.os.AsyncTask
import mrj.example.testapp.R
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

@Suppress("DEPRECATION")
class ReadWord(var contextActivity: Activity) : AsyncTask<Void, Int, String>() {


    override fun doInBackground(vararg p0: Void?): String? {

        var page = 1
        var progress = 0

        while (true) {
            val url = "https://mrjavohirtest.pythonanywhere.com/words?page=${page}"
            var datas = ""
            try {
                datas = URL(url).readText()
            } catch (e: Exception) {
                return ""
            }

            val jsonReader = JSONArray(datas)
            val db = WordDatabase(contextActivity)
            val readable = db.readableDatabase

            if (jsonReader.length() == 0){
                return ""
            }

            var elem_count = 0

            while (elem_count < jsonReader.length()) {

                publishProgress(progress)

                val jsonObject = jsonReader.getJSONObject(elem_count)
                val cv = ContentValues()
                cv.put(WordTable.word_col, jsonObject.getString("word"))
                cv.put(WordTable.wordtype_col, jsonObject.getString("wordtype"))
                cv.put(WordTable.description_col, jsonObject.getString("description"))
                readable.insert(WordTable.TABLE_NAME, null, cv)

                elem_count += 1
                progress += 1
            }
            page += 1
        }

        return ""
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        val mainActivity = (contextActivity as MainActivity)
        mainActivity.fillAdapter()
        if (mainActivity.dialog_download.isShowing) {
            mainActivity.dialog_download.hide()
        }

    }

    override fun onProgressUpdate(vararg values: Int?) {
        super.onProgressUpdate(*values)
        val mainActivity = (contextActivity as MainActivity)
        mainActivity.txt_download_progress.text =
            mainActivity.resources.getString(R.string.txt_downloading_word, values[0].toString())
    }
}