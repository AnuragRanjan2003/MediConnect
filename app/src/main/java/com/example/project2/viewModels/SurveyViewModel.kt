package com.example.project2.viewModels

import android.util.Log.e
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project2.models.Disease
import com.example.project2.models.Symptom
import com.example.project2.models.Terms.TermsResponse
import com.example.project2.models.features.Feature
import com.example.project2.models.initSession.SessionResponse
import com.example.project2.repo.EndlessMedicalApiInstance
import com.google.gson.Gson
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val API_KEY = "8d1c079557mshfad6e967741145dp1026f1jsnf8e1854363fb"
const val API_HOST = "endlessmedicalapi1.p.rapidapi.com"

class SurveyViewModel : ViewModel() {

    private val status: MutableLiveData<SessionResponse> by lazy { MutableLiveData<SessionResponse>() }
    private val json: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    private val analysisJson: MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    private val postStat: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    private val termsStat: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val len: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    private val diseaseList: MutableLiveData<ArrayList<Disease>> by lazy { MutableLiveData<ArrayList<Disease>>() }

    private fun getStatus(symptomList: List<Symptom>, passPhrase: String) {
        EndlessMedicalApiInstance.api.initSession(
            API_KEY,
            API_HOST
        ).enqueue(object : Callback<SessionResponse?> {
            override fun onResponse(
                call: Call<SessionResponse?>,
                response: Response<SessionResponse?>
            ) {
                if (response.body() != null) {
                    status.value = response.body()!!
                    json.value = Gson().toJson(response.body()!!)
                    acceptTerms(
                        response.body()!!,
                        symptomList,
                        passPhrase = passPhrase
                    )
                }
            }

            override fun onFailure(call: Call<SessionResponse?>, t: Throwable) {
                e("init status error", t.message.toString())
            }
        })
    }

    private fun acceptTerms(
        sessionResponse: SessionResponse,
        symptomList: List<Symptom>,
        passPhrase: String
    ) {
        EndlessMedicalApiInstance.api.acceptTerms(
            API_KEY,
            API_HOST,
            sessionId = sessionResponse.SessionID, passPhrase = passPhrase
        )
            .enqueue(object : Callback<TermsResponse?> {
                override fun onResponse(
                    call: Call<TermsResponse?>,
                    response: Response<TermsResponse?>
                ) {
                    if (response.body() != null) {
                        termsStat.value = response.body()!!.status
                        postSymptomList(symptomList = symptomList)
                    }
                }

                override fun onFailure(call: Call<TermsResponse?>, t: Throwable) {
                    e("accept terms", t.message.toString())
                }
            })


    }

    private fun postSymptomList(symptomList: List<Symptom>) {
        for (symptom in symptomList) {
            val r = postSymptom(Feature(status.value!!.SessionID, symptom.value, symptom.name))
            if (r != "ok") {
                e("post error", r)
                return
            }
        }
        getAnalysis(status.value!!.SessionID)
    }

    fun getResult(symptomList: List<Symptom>, passPhrase: String) {
        getStatus(symptomList, passPhrase)
    }

    private fun postSymptom(feature: Feature): String {
        var ret = "ok"
        EndlessMedicalApiInstance.api.updateFeatures(
            API_KEY,
            API_HOST,
            feature.sessionId,
            feature.value,
            feature.featureName
        ).enqueue(object : Callback<TermsResponse?> {
            override fun onResponse(
                call: Call<TermsResponse?>,
                response: Response<TermsResponse?>
            ) {
                if (response.body() != null) {
                    postStat.value = response.body()!!.status
                    ret = postStat.value!!
                }
            }

            override fun onFailure(call: Call<TermsResponse?>, t: Throwable) {
                e("error post symp", t.message.toString())
                ret = t.message.toString()
            }
        })
        return ret
    }

    private fun getAnalysis(sessionID: String) {
        EndlessMedicalApiInstance.api.getAnalysis(
            API_KEY,
            API_HOST,
            sessionID
        ).enqueue(object : Callback<Any?> {
            override fun onResponse(call: Call<Any?>, response: Response<Any?>) {
                if (response.isSuccessful) {
                    analysisJson.value = Gson().toJson(response.body())
                    len.value = convertToJSONObject(analysisJson.value!!)
                    getDiseases(analysisJson.value!!)
                }
            }

            override fun onFailure(call: Call<Any?>, t: Throwable) {
                e("error analysis", t.message.toString())
            }
        })
    }

    fun observeStatus(): LiveData<SessionResponse> {
        return status
    }

    fun observeTerms(): LiveData<String> {
        return termsStat
    }

    fun observeJson(): LiveData<String> {
        return json
    }

    fun observeAnalysisJson(): LiveData<String> {
        return analysisJson
    }

    fun observePostStat(): LiveData<String> {
        return postStat
    }

    fun convertToJSONObject(jsonString: String): String {
        val jsonObj = JSONObject(jsonString)
        val jsonArray = jsonObj.getJSONArray("Diseases")
        val keys = jsonArray.getJSONObject(0).keys()
        return keys.next().toString()
    }

    fun getDiseases(jsonString: String) {
        val list: ArrayList<Disease> = ArrayList()
        val jsonObject = JSONObject(jsonString)
        val jsonArray = jsonObject.getJSONArray("Diseases")
        var len = jsonArray.length()
        if (len != 0) {
            len -= 1
            for (i in 0..len) {
                val obj = jsonArray.getJSONObject(i)
                val key = obj.keys().next()
                val disease = Disease(key, obj.getString(key))
                list.add(disease)
            }
        }
        diseaseList.value = list
    }

    fun observeDiseases(): LiveData<ArrayList<Disease>> {
        return diseaseList
    }
}