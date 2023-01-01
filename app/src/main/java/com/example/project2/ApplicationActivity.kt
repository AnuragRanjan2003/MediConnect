package com.example.project2

import android.os.Bundle
import android.util.Log.d
import android.util.Log.e
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project2.adapters.SurveyRecAdapter
import com.example.project2.databinding.ActivityApplicationBinding
import com.example.project2.models.SurveyItem
import com.example.project2.viewModels.ApplicationActivityViewModel

class ApplicationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityApplicationBinding
    private lateinit var adapter: SurveyRecAdapter
    private lateinit var list: ArrayList<SurveyItem>
    private lateinit var viewModel: ApplicationActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApplicationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        list = ArrayList()
        viewModel = ViewModelProvider(this)[ApplicationActivityViewModel::class.java]
        viewModel.getFeatures()
        viewModel.observeFeatures().observe(this) { d("Feature response", "$it") }
        viewModel.observeSurveyList().observe(this) {
            d("Feature response", "$it")
            list.clear()
            list.addAll(it)
            adapter.notifyDataSetChanged()
        }

//        val item1 = SurveyItem("Heading 1", "content 1")
//        val item2 = SurveyItem("Heading 2", "content 2")
//        list = ArrayList(listOf(item1, item2))

        adapter = SurveyRecAdapter(list , this)
        binding.symptomsRec.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.symptomsRec.hasFixedSize()
        binding.symptomsRec.adapter = this.adapter
        binding.testing.setOnClickListener { e("survey list", "${adapter.getList()}") }
        binding.etSearch.doAfterTextChanged { adapter.filter.filter(it.toString()) }

    }


}