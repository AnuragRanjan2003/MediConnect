package com.example.project2.viewModels

import android.util.Log.e
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project2.models.SurveyItem
import com.example.project2.models.features.FeatureResponse
import com.example.project2.repo.EndlessMedicalApiInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApplicationActivityViewModel : ViewModel() {
    private val TAG = "Feature response"
    private val featuresList: MutableLiveData<List<String>> by lazy { MutableLiveData<List<String>>() }
    private val featuresAsSurveyItem : MutableLiveData<ArrayList<SurveyItem>> by lazy{ MutableLiveData<ArrayList<SurveyItem>>()}

    fun getFeatures() {
        EndlessMedicalApiInstance.api.getFeatures(apiKey = API_KEY, hostKey = API_HOST)
            .enqueue(object : Callback<FeatureResponse?> {
                override fun onResponse(
                    call: Call<FeatureResponse?>,
                    response: Response<FeatureResponse?>
                ) {
                    if (response.body() != null) {
                        if (response.body()!!.status == "ok") {
                            featuresList.value = response.body()!!.data
                            populateList(featuresList.value!!)
                        }else
                            e(TAG, response.body()!!.status)

                    } else {
                        e(TAG, "Feature response empty")
                    }
                }

                override fun onFailure(call: Call<FeatureResponse?>, t: Throwable) {
                    e(TAG, t.message.toString())
                }
            })
    }

    private fun populateList(list: List<String>) {
        val tempList = ArrayList<SurveyItem>()
        for( data in list){
            tempList.add(SurveyItem(heading = data, content = " content of $data"))
        }
        featuresAsSurveyItem.value = tempList
    }

    fun observeFeatures(): LiveData<List<String>> {
        return featuresList
    }

    fun observeSurveyList():LiveData<ArrayList<SurveyItem>>{
        return featuresAsSurveyItem
    }
}