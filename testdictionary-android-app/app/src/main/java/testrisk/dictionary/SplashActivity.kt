package testrisk.dictionary

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity: AppCompatActivity() {
    private var shaTerms = String()
    private var shaLicense = String()
    private var shaReadme = String()

    private var NETWORK_ERROR = "Bağlantı hatası! Lütfen internet ayarlarını kontrol ediniz."
    private var TERM_NOT_FOUND = "Aranan terim bulunamadı! Farklı bir terim arayınız."

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent = Intent(this, MainActivity::class.java)

        setLatestCommit(intent)
    }


    private fun setLatestCommit(intent: Intent) {
        Thread.sleep(1000)
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

                intent.putExtra("shaTerms", shaTerms)
                intent.putExtra("shaLicense", shaLicense)
                intent.putExtra("shaReadme", shaReadme)

                startActivity(intent)
                finish()
            }
        })
    }

    private fun setLatestCommitSnyc() {
        Log.v("setLatestCommit called", "setLatestCommit")
        var response = TermsApi().getLatestCommit().execute()
        if(response.isSuccessful) {
            val body = JSONArray(response.body()?.string())
            var shas = arrayListOf("README.md", "LICENSE", "terms.json")

            for (i in 0 until body.length() - 1) {
                var item = body.getJSONObject(i)
                var name = item.getString("name")
                if (shas.contains(name)) {
                    when (name) {
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
                if (shas.size == 0) break
            }
        } else {
            shaReadme = "notGetItFromGithub"
            shaTerms = "notGetItFromGithub"
            shaLicense = "notGetItFromGithub"
            Toast.makeText(applicationContext, NETWORK_ERROR, Toast.LENGTH_LONG).show()
        }
    }


}