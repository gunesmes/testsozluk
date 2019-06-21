import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import testrisk.dictionary.Term
import java.lang.Exception


class DBHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    private var CONTENT_NOT_FOUND = "İçerik yüklenemedi internet ayarlarını kontrol ediniz"

    companion object {
        private const val DB_NAME = "DictionaryDB"
        private const val DB_VERSION = 3
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTerms = "CREATE TABLE terms (term TEXT, meaning TEXT)"
        val createShas = "CREATE TABLE shas (file TEXT, sha TEXT)"
        val createPageContent = "CREATE TABLE contents (page TEXT, content TEXT)"

        db?.execSQL(createTerms)
        db?.execSQL(createShas)
        db?.execSQL(createPageContent)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Called when the database needs to be upgraded
        db?.execSQL("DROP TABLE IF EXISTS terms")
        db?.execSQL("DROP TABLE IF EXISTS shas")
        db?.execSQL("DROP TABLE IF EXISTS contents")

        onCreate(db)
    }

    fun deleteRecords(tableName: String) {
        val db = this.writableDatabase

        //Delete all records of table
        db.execSQL("DELETE FROM $tableName")

        // Reset the auto_increment primary key if you needed
        // No need this since the $ID Integer PRIMARY KEY remove for the sake of CPU
        // db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE name=$tableName")

        //For go back free space by shrinking sqlite file
        db.execSQL("VACUUM")
        db.close()
    }

    //Inserting (Creating) data
    fun addTerm(term: Term): Boolean {
        //Create and/or open a database that will be used for reading and writing.
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("term", term.term)
        values.put("meaning", term.meaning)
        val _success = db.insert("terms", null, values)
        db.close()
        Log.v("InsertedID", "$_success")
        return (Integer.parseInt("$_success") != -1)
    }

    //Inserting (Content) data
    fun addPageContent(page: String, content: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("page", page)
        values.put("content", content)
        val _success = db.insert("contents", null, values)
        db.close()
        Log.v("$page content inserted", "$_success")
        return (Integer.parseInt("$_success") != -1)
    }

    fun getTerms(term: String): List<Term> {
        val db = this.writableDatabase
        val cursor = db.rawQuery("SELECT * FROM terms WHERE term LIKE '%$term%'", null)

        val termsLikeSearch = mutableListOf<Term>()

        if (cursor != null) {
            while (cursor.moveToNext()) {
                val term = cursor.getString(cursor.getColumnIndex("term"))
                val meaning = cursor.getString(cursor.getColumnIndex("meaning"))

                termsLikeSearch.add(Term(term, meaning))
            }
        }
        cursor.close()
        db.close()

        return termsLikeSearch
    }

    fun getPageContent(page: String): String {
        val db = this.writableDatabase

        return try {
            val cursor = db.rawQuery("SELECT content FROM contents where page='$page' LIMIT 1;", null)
            var result = mutableListOf<String>()
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    var content = cursor.getString(0)
                    result.add(content)
                }
            }
            result[0]
        } catch (e: Exception) {
            CONTENT_NOT_FOUND
        }
        db.close()
    }

    fun addSha(fileName: String, newSha: String): Boolean {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put("file", fileName)
        values.put("sha", newSha)
        val _success = db.insert("shas", null, values)
        db.close()
        Log.v("$fileName sha inserted", "$_success")
        return (Integer.parseInt("$_success") != -1)
    }

    fun getSha(fileName: String): String {
        val db = this.readableDatabase
        var result = mutableListOf<String>()

        return try {
            val cursor = db.rawQuery("SELECT sha FROM shas where file='$fileName' LIMIT 1;", null)
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    var sha = cursor.getString(0)
                    result.add(sha)
                }
            }
            result[0]
        } catch (e: Exception) {
            "notGetShaFromDB"
        }
        db.close()
    }

    fun updateSha(fileName: String, newSha: String) {
        val db = this.writableDatabase
        db.execSQL("UPDATE shas SET sha='$newSha' WHERE file='$fileName'")
        db.close()
    }
}