package com.example.project2

import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.util.Log.e
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.project2.databinding.ActivityAnalysisBinding
import com.example.project2.models.SaveDataModel
import com.example.project2.models.wiki.WikiResult
import com.example.project2.uiComponents.ExpandableCard
import com.example.project2.viewModels.AnalysisActivityViewModel
import kotlin.math.floor

class AnalysisActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAnalysisBinding
    private lateinit var viewModel: AnalysisActivityViewModel
    private lateinit var card1: ExpandableCard
    private lateinit var card2: ExpandableCard
    private lateinit var card3: ExpandableCard
    private lateinit var viewCard1: View
    private lateinit var viewCard2: View
    private lateinit var viewCard3: View
    private var qList = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnalysisBinding.inflate(layoutInflater)
        setContentView(binding.root)


        viewCard1 = findViewById(R.id.item1)
        viewCard2 = findViewById(R.id.item2)
        viewCard3 = findViewById(R.id.item3)

        card1 = ExpandableCard(view = viewCard1)
        card2 = ExpandableCard(view = viewCard2)
        card3 = ExpandableCard(view = viewCard3)

        viewCard1.setOnClickListener { card1.transition() }
        viewCard2.setOnClickListener { card2.transition() }
        viewCard3.setOnClickListener { card3.transition() }

        val name = intent.getStringExtra("name")

        viewModel = ViewModelProvider(this)[AnalysisActivityViewModel::class.java]

        val comp = object : Completion {
            override fun onComplete() {}

            override fun onCancelled(name: String, message: String) {
                e(name, message)
            }
        }

        viewModel.getResults(name!!, comp)
        viewModel.observeData().observe(this) {
            e(this.localClassName, "$it")
            putValue(it)
        }

        viewModel.observeDetail1().observe(this) {
            e(this.localClassName,"result1: $it")
            card1.setDetail(getDescription(it))
        }
        viewModel.observeDetail2().observe(this) {
            e(this.localClassName,"result2: $it")
            card2.setDetail(getDescription(it))
        }
        viewModel.observeDetail3().observe(this) {
            e(this.localClassName,"result3: $it")
            card3.setDetail(getDescription(it))
        }


    }

    private fun putValue(saveDataModel: SaveDataModel) {
        card1.setValue(h = saveDataModel.dname1!!, v = parseToValue(saveDataModel.prob1!!))
        card2.setValue(h = saveDataModel.dname2!!, v = parseToValue(saveDataModel.prob2!!))
        card3.setValue(h = saveDataModel.dname3!!, v = parseToValue(saveDataModel.prob3!!))
        qList.clear()
        qList.addAll(
            listOfNotNull(
                saveDataModel.dname1,
                saveDataModel.dname2,
                saveDataModel.dname3
            )
        )
        e(this.localClassName, "QList $qList")
        viewModel.getDetails(qList)
    }

    private fun parseToValue(prob: String): Int {
        return floor(prob.toDouble() * 100).toInt()
    }

    private fun getDescription(wikiResult: WikiResult): String {
        return "${wikiResult.pages[0].description} ${getExcerpt(wikiResult)}"
    }

    private fun getExcerpt(wikiResult: WikiResult): Spanned? {
        val body = wikiResult.pages[0].excerpt
        return Html.fromHtml(body, Html.FROM_HTML_MODE_LEGACY)
    }




}