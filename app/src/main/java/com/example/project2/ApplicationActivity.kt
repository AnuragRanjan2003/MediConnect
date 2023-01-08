package com.example.project2

import android.content.Intent
import android.os.Bundle
import android.util.Log.d
import android.util.Log.e
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project2.adapters.SurveyRecAdapter
import com.example.project2.databinding.ActivityApplicationBinding
import com.example.project2.models.Disease
import com.example.project2.models.SaveDataModel
import com.example.project2.models.SurveyItem
import com.example.project2.uiComponents.AnimatedButton
import com.example.project2.viewModels.ApplicationActivityViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ApplicationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityApplicationBinding
    private lateinit var adapter: SurveyRecAdapter
    private lateinit var list: ArrayList<SurveyItem>
    private lateinit var viewModel: ApplicationActivityViewModel
    private lateinit var animatedButton: AnimatedButton
    private lateinit var submitButton: View
    private var diseaseList = ArrayList<Disease>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApplicationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        list = ArrayList()
        viewModel = ViewModelProvider(this)[ApplicationActivityViewModel::class.java]
        submitButton = findViewById(R.id.btn_submit)
        animatedButton = AnimatedButton(
            text = "Submit",
            changeText = "please wait..",
            icon = AppCompatResources.getDrawable(this, R.drawable.ic_baseline_done_outline_24)!!,
            view = submitButton,
            textColor = resources.getColor(R.color.md_theme_light_onSurfaceVariant, null),
            completion = object : Completion {
                override fun onComplete(dataModel: SaveDataModel, position: Int,q: String) {
                    onSubmitClick()
                }

                override fun onCancelled(name: String, message: String) {
                    e(name, message)
                }


            }
        )
        binding.placeholder.startShimmer()

        viewModel.getFeatures()
        viewModel.observeFeatures().observe(this) { d("Feature response", "$it") }
        viewModel.observeSurveyList().observe(this) {
            d("Feature response", "$it")
            list.clear()
            list.addAll(it)
            adapter.setFullList(it)
            adapter.notifyDataSetChanged()
            binding.symptomsRec.visibility = View.VISIBLE
            binding.placeholder.stopShimmer()
            binding.placeholder.visibility = View.GONE
            adapter.logLists()
        }

        viewModel.observeDiseases().observe(this) {
            diseaseList.clear()
            diseaseList.addAll(it)
        }



        adapter = SurveyRecAdapter(list, this)
        binding.symptomsRec.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.symptomsRec.hasFixedSize()
        binding.symptomsRec.adapter = this.adapter

        binding.etSearch.doAfterTextChanged {   adapter.filter.filter(it) }

        submitButton.setOnClickListener {
            animatedButton.activate()
        }


    }

    private fun onSubmitClick() {
        e("survey list", "${adapter.getList()}")
        if (adapter.getList().isNotEmpty())
            viewModel.getResult(
                adapter.getList(),
                getString(R.string.passPhrase),
                completion = object : Completion {
                    override fun onComplete(dataModel: SaveDataModel, position: Int,q:String) {
                        processData(diseaseList)
                    }

                    override fun onCancelled(name: String, message: String) {
                        e(name, message)
                        animatedButton.deactivate()
                    }
                }
            )
        else {
            e("Diseases", "no symptoms reported")
            Toast.makeText(this,"no symptoms reported",Toast.LENGTH_SHORT).show()
            animatedButton.deactivate()
        }

    }

    private fun saveData(model: SaveDataModel) {
        val name = makeName()
        Firebase.database.getReference("Reports").child(Firebase.auth.currentUser!!.uid)
            .child(name).setValue(model).addOnSuccessListener {
                val intent = Intent(this@ApplicationActivity, AnalysisActivity::class.java)
                intent.putExtra("name", name)
                startActivity(intent)
                finishAffinity()
            }.addOnFailureListener {
                e("data saving error", it.message.toString())
                animatedButton.deactivate()
            }

    }


    private fun convertToSaveDataModel(list: List<Disease>): SaveDataModel {
        val nameList = ArrayList<String>()
        val probList = ArrayList<String>()
        for (i in 0..2) {
            val item = list[i]
            nameList.add(item.name)
            probList.add(item.probability)
        }
        val date = makeName()
        return SaveDataModel(
            date = date,
            dname1 = nameList[0],
            dname2 = nameList[1],
            dname3 = nameList[2],
            prob1 = probList[0],
            prob2 = probList[1],
            prob3 = probList[2]
        )
    }

    private fun makeName(): String {
        val format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
        return LocalDateTime.now().format(format)
    }

    private fun processData(list: ArrayList<Disease>) {
        e(
            this@ApplicationActivity.localClassName,
            "analysis finished : $diseaseList"
        )
        val model = convertToSaveDataModel(list)
        saveData(model)
    }



}