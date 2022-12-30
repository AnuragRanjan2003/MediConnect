package com.example.project2.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project2.models.Terms.TermsResponse
import com.example.project2.models.analysis.AnalysisResponse
import com.example.project2.models.features.Feature
import com.example.project2.models.initSession.SessionResponse
import com.example.project2.repo.EndlessMedicalApiInstance
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SurveyActivityViewModel : ViewModel() {
    private val passPhrase =
        "I have read, understood and I accept and agree to comply with the Terms of Use of EndlessMedicalAPI and Endless Medical services. The Terms of Use are available on endlessmedical.com"
    private val status: MutableLiveData<SessionResponse> by lazy { MutableLiveData<SessionResponse>() }
    private val json: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    private val analysisJson: MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    private val postStat: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    private val termsStat: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    private fun getStatus(name: String, value: String) {
        EndlessMedicalApiInstance.api.initSession(
            "8d1c079557mshfad6e967741145dp1026f1jsnf8e1854363fb",
            "endlessmedicalapi1.p.rapidapi.com"
        ).enqueue(object : Callback<SessionResponse?> {
            override fun onResponse(
                call: Call<SessionResponse?>,
                response: Response<SessionResponse?>
            ) {
                if (response.body() != null) {
                    status.value = response.body()!!
                    json.value = Gson().toJson(response.body()!!)
                    acceptTerms(response.body()!!, name = name, value = value)
                }
            }

            override fun onFailure(call: Call<SessionResponse?>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun acceptTerms(sessionResponse: SessionResponse, value: String, name: String) {
        EndlessMedicalApiInstance.api.acceptTerms(
            "8d1c079557mshfad6e967741145dp1026f1jsnf8e1854363fb",
            "endlessmedicalapi1.p.rapidapi.com",
            sessionId = sessionResponse.SessionID, passPhrase = passPhrase
        )
            .enqueue(object : Callback<TermsResponse?> {
                override fun onResponse(
                    call: Call<TermsResponse?>,
                    response: Response<TermsResponse?>
                ) {
                    if (response.body() != null) {
                        termsStat.value = response.body()!!.status
                        postSymptom(
                            Feature(
                                sessionId = status.value!!.SessionID,
                                value = value,
                                featureName = name
                            )
                        )
                    }
                }

                override fun onFailure(call: Call<TermsResponse?>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })


    }

    fun getResult(name: String, value: String) {
        getStatus(name, value)
    }

    private fun postSymptom(feature: Feature) {
        EndlessMedicalApiInstance.api.updateFeatures(
            "8d1c079557mshfad6e967741145dp1026f1jsnf8e1854363fb",
            "endlessmedicalapi1.p.rapidapi.com",
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
                    getAnalysis(status.value!!.SessionID)

                }
            }

            override fun onFailure(call: Call<TermsResponse?>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })

    }

    fun getAnalysis(sessionID: String) {
        EndlessMedicalApiInstance.api.getAnalysis(
            "8d1c079557mshfad6e967741145dp1026f1jsnf8e1854363fb",
            "endlessmedicalapi1.p.rapidapi.com",
            sessionID
        ).enqueue(object : Callback<AnalysisResponse?> {
            override fun onResponse(
                call: Call<AnalysisResponse?>,
                response: Response<AnalysisResponse?>
            ) {
                if (response.body() != null)
                    analysisJson.value = Gson().toJson(response.body())
            }

            override fun onFailure(call: Call<AnalysisResponse?>, t: Throwable) {
                TODO("Not yet implemented")
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
}