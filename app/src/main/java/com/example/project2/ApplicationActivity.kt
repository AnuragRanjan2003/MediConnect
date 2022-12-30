package com.example.project2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.e
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project2.adapters.SurveyRecAdapter
import com.example.project2.databinding.ActivityApplicationBinding
import com.example.project2.models.SurveyItem

class ApplicationActivity : AppCompatActivity() {
    private lateinit var binding:ActivityApplicationBinding
    private lateinit var adapter : SurveyRecAdapter
    private lateinit var list : List<SurveyItem>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApplicationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        makeDropDown()

        val item1 = SurveyItem("Heading 1","content 1")
        val item2 = SurveyItem("Heading 2","content 2")
        list = listOf(item1,item2)
        adapter = SurveyRecAdapter(list,this)
        binding.symptomsRec.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        binding.symptomsRec.hasFixedSize()
        binding.symptomsRec.adapter = this.adapter
        binding.testing.setOnClickListener { e("survey list","${adapter.getList()}") }



    }

    private fun makeDropDown(){
        val list = resources.getStringArray(R.array.menu_items)
        val adapter = ArrayAdapter(this,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,list)
        binding.etCategory.setAdapter(adapter)
    }
}