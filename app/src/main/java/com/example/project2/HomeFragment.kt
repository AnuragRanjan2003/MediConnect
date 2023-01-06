package com.example.project2

import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log.e
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project2.adapters.HistoryRecAdapter
import com.example.project2.databinding.FragmentHomeBinding
import com.example.project2.models.SaveDataModel
import com.example.project2.viewModels.HistoryFragmentViewModel
import com.example.project2.viewModels.MainActivityViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var viewModel: HistoryFragmentViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: HistoryRecAdapter
    private val list = ArrayList<SaveDataModel>()
    private val sharedViewModel: MainActivityViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[HistoryFragmentViewModel::class.java]
        adapter = HistoryRecAdapter(list, requireActivity().baseContext, object : Completion {
            override fun onComplete(dataModel: SaveDataModel,pos: Int,q:String) {
                val intent = Intent(activity, AnalysisActivity::class.java)
                intent.putExtra("name", dataModel.date)
                activity?.startActivity(intent)
            }

            override fun onCancelled(name: String, message: String) {
                TODO("Not yet implemented")
            }
        })
        viewModel.getData()

        viewModel.observeData().observe(viewLifecycleOwner) { sharedViewModel.setList(it) }

        viewModel.observeFilteredList().observe(viewLifecycleOwner) {
            list.clear()
            list.addAll(it)
            adapter.endLoading()
            adapter.notifyDataSetChanged()
        }

        viewModel.observeUnFilteredList().observe(viewLifecycleOwner) {
            sharedViewModel.setFilteredList(it)
        }

        binding.hisRec.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.hisRec.hasFixedSize()
        binding.hisRec.adapter = this.adapter

        val simpleCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                when (direction) {
                    ItemTouchHelper.LEFT -> deleteItem(pos)
                    else -> archiveItem(pos)
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                RecyclerViewSwipeDecorator.Builder(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                    .addSwipeLeftBackgroundColor(
                        activity!!.resources.getColor(
                            R.color.md_theme_light_error,
                            null
                        )
                    )
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_24)
                    .setSwipeLeftActionIconTint(
                        activity!!.resources.getColor(
                            R.color.md_theme_light_onError,
                            null
                        )
                    )
                    .addSwipeRightBackgroundColor(
                        activity!!.resources.getColor(
                            R.color.green_deep,
                            null
                        )
                    )
                    .addSwipeRightActionIcon(R.drawable.ic_baseline_archive_24)
                    .setActionIconTint(
                        activity!!.resources.getColor(
                            R.color.md_theme_light_onPrimary,
                            null
                        )
                    )
                    .create()
                    .decorate()
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )

            }
        }
        val touchHelper = ItemTouchHelper(simpleCallback)
        touchHelper.attachToRecyclerView(binding.hisRec)

        return binding.root
    }

    private fun deleteItem(pos: Int) {
        val item = list[pos]
        list.remove(item)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                Firebase.database.getReference("Reports").child(Firebase.auth.currentUser!!.uid)
                    .child(item.date).removeValue().await()
                launch(Dispatchers.Main) {
                    adapter.notifyItemRemoved(pos)
                    Snackbar.make(binding.root, "deleted", Snackbar.LENGTH_LONG).show()
                }
            } catch (e: java.lang.Exception) {
                e(this@HomeFragment.tag, e.message.toString())
            }


        }


    }

    private fun archiveItem(pos: Int) {
        val item = list[pos]
        list.remove(item)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                Firebase.database.getReference("Reports").child(Firebase.auth.currentUser!!.uid)
                    .child(item.date)
                    .child("archive")
                    .setValue(true)
                    .await()
            } catch (e: java.lang.Exception) {
                e(this@HomeFragment.tag, e.message.toString())
            }
            launch(Dispatchers.Main) {
                adapter.notifyItemRemoved(pos)
                Snackbar.make(binding.root, "archived", Snackbar.LENGTH_LONG).show()
            }
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}