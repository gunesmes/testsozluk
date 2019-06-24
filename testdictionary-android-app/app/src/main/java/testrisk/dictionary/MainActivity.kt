package testrisk.dictionary

import DBHelper
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.StrictMode
import android.support.annotation.RequiresApi
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.View.*
import android.view.inputmethod.InputMethodManager
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private lateinit var textMessage: TextView
    private lateinit var textAbout: TextView
    private lateinit var runnable: Runnable

    private var shaTerms = String()
    private var shaLicense = String()
    private var shaReadme = String()

    private var dbHelper: DBHelper? = null

    private var NETWORK_ERROR = "Bağlantı hatası! Lütfen internet ayarlarını kontrol ediniz."
    private var TERM_NOT_FOUND = "Aranan terim bulunamadı! Farklı bir terim arayınız."


    @RequiresApi(Build.VERSION_CODES.N)
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        hideKeyboard(container)

        when (item.itemId) {
            R.id.navigation_home -> {
                showLoadingPanel()

                fetchReadMe()
                navHome.visibility = VISIBLE

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dictionary -> {
                showLoadingPanel()
                etSearch.text.clear()

                fetchTerms()
                navDictionary.visibility = VISIBLE

                etSearch.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        val searchText = etSearch.text.toString()
                        if(searchText.length != 1) showSearchResult(searchText)
                    }
                })

                recyclerView.setOnTouchListener(OnTouchListener { v, event ->
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)

                    false
                })

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_about -> {
                showLoadingPanel()

                fetchAbout()
                navAbout.visibility = VISIBLE

                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun showLoadingPanel() {
        navHome.visibility = GONE
        navDictionary.visibility = GONE
        navAbout.visibility = GONE
        loadingPanel.visibility = VISIBLE
    }


    //you need to keep the handler outside the runnable body to work in kotlin
    private fun runDelayedHandler(timeToWait: Long) {

        //Keep it running
        val handler = Handler()
        handler.postDelayed(runnable, timeToWait)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.v("oncreate called", "oncreate")
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        // init db
        dbHelper = DBHelper(this)

        textMessage = findViewById(R.id.message)
        textAbout = findViewById(R.id.about)

        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        setLatestCommit()
        fetchReadMe()
    }

    private fun showReadMe(text: String) {
        textMessage.movementMethod = LinkMovementMethod.getInstance()
        textMessage.text = Html.fromHtml(parseTextLink(text))
    }

    private fun showTerms(terms: List<Term>) {
        recyclerView.layoutManager = LinearLayoutManager(this)

        if(terms.isEmpty()) {
            var newTerms = mutableListOf<Term>()
            newTerms.add(Term("404-not-found", TERM_NOT_FOUND))
            recyclerView.adapter = TermsAdapter(newTerms)
        } else {
            recyclerView.adapter = TermsAdapter(terms)
        }

    }

    private fun showAbout(text: String) {
        textAbout.movementMethod = LinkMovementMethod.getInstance()
        textAbout.text = removeNewLines(text)
    }

    private fun fetchReadMe(){
        val shaDB = dbHelper?.getSha("readme")

        if (shaReadme != shaDB) {
            addOrUpdateSha("readme", shaReadme, shaDB)

            TermsApi().getReadMe().enqueue(object: Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(applicationContext, NETWORK_ERROR, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    val readMe = response.body()?.string()
                    readMe?.let {
                        showReadMe(it)
                        addPageContentDB("readme", it)
                    }
                }
            })
        } else {
            Log.v("readme: $shaReadme == $shaDB", "DB")
            // get it from DB
            dbHelper?.getPageContent("readme")?.let { showReadMe(it) }
        }

        loadingPanel.visibility = GONE
    }

    private fun fetchTerms() {
        val shaDB = dbHelper?.getSha("terms")

        if (shaTerms != shaDB ) {
            // if the DB doesn't have latest commit update the sha and terms
            addOrUpdateSha("terms", shaTerms, shaDB)

            TermsApi().getTerms().enqueue(object : Callback<List<Term>> {
                override fun onFailure(call: Call<List<Term>>, t: Throwable) {
                    Toast.makeText(applicationContext, NETWORK_ERROR, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<List<Term>>, response: Response<List<Term>>) {
                    val terms = response.body()

                    // delete terms table
                    deleteTableDB("terms")

                    terms?.let {
                        showTerms(it)
                        addTermDB(it)
                    }
                }

            })
        } else {
            Log.v("Terms: $shaTerms == $shaDB", "DB")
            // get them from DB
            showSearchResult("")
        }

        loadingPanel.visibility = GONE
    }


    private fun fetchAbout() {
        val shaDB = dbHelper?.getSha("about")

        if (shaLicense != shaDB) {
            addOrUpdateSha("license", shaLicense, shaDB)

            TermsApi().getLicense().enqueue(object: Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(applicationContext, NETWORK_ERROR, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    val about = response.body()?.string()
                    about?.let {
                        showAbout(it)
                        addPageContentDB("about", it)
                    }
                }
            })
        } else {
            Log.v("License: $shaLicense == $shaDB", "DB")
            // get it from DB
            dbHelper?.getPageContent("about")?.let { showAbout(it) }
        }

        loadingPanel.visibility = GONE
    }

    private fun setLatestCommit() {
        Log.v("setLatestCommit called", "setLatestCommit")
        TermsApi().getLatestCommit().enqueue(object: Callback<ResponseBody>{
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                shaReadme = "notGetItFromGithub"
                shaTerms = "notGetItFromGithub"
                shaLicense = "notGetItFromGithub"
                Toast.makeText(applicationContext, NETWORK_ERROR, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val body = JSONArray(response.body()?.string())
                var shas = arrayListOf("README.md", "LICENSE", "terms.json")

                for (i in 0 until body.length()-1) {
                    var item = body.getJSONObject(i)
                    var name = item.getString("name")
                    if(shas.contains(name)) {
                        when(name) {
                            "README.md" -> {
                                shaReadme = item.getString("sha")
                                shas.remove("README.md")
                            }
                            "LICENSE" -> {
                                shaLicense = item.getString("sha")
                                shas.remove("LICENSE")
                            }
                            "terms.json" -> {
                                shaTerms = item.getString("sha")
                                shas.remove("Terms.json")
                            }
                        }

                    }
                    if(shas.size == 0) break
                }
            }
        })
    }

    private fun addOrUpdateSha(fileName: String, newSha: String, shaOnDb: String?) {
        Log.v("Terms: $newSha != $shaOnDb", "DB")

        if (fileName == "notGetShaFromDB") {
            dbHelper?.addSha(fileName = fileName, newSha = newSha)
        } else {
            dbHelper?.updateSha(fileName = fileName, newSha = newSha)
        }
    }

    private fun addTermDB(terms: List<Term>) {
        val db = DBHelper(this)
        terms.forEach {
            db.addTerm(it)
        }
    }

    private fun addPageContentDB(page: String, content: String) {
        val db = DBHelper(this)
        db.addPageContent(page, content)
    }

    private fun deleteTableDB(tableName: String) {
        DBHelper(this).deleteRecords(tableName)
    }

    private fun showSearchResult(searchText: String) {
        var terms = DBHelper(this).getTerms(searchText)
        showTerms(terms)
    }
}

