package com.example.project2

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log.e
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.project2.adapters.ArchiveRecAdapter
import com.example.project2.databinding.FragmentProfileBinding
import com.example.project2.models.SaveDataModel
import com.example.project2.models.User
import com.example.project2.uiComponents.AnimatedButton
import com.example.project2.uiComponents.InfoCard
import com.example.project2.viewModels.MainActivityViewModel
import com.example.project2.viewModels.ProfileFragmentViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileFragmentViewModel
    private val sharedViewModel: MainActivityViewModel by activityViewModels()
    private lateinit var animatedButton: AnimatedButton
    private lateinit var adapter: ArchiveRecAdapter
    private lateinit var infoCard: InfoCard
    private lateinit var infoCardView: View
    private val list = ArrayList<SaveDataModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentProfileBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[ProfileFragmentViewModel::class.java]
        adapter = ArchiveRecAdapter(list,
            requireActivity().baseContext,
            object : Completion {
                override fun onComplete(dataModel: SaveDataModel, position: Int) {
                    unArchive(position, dataModel)
                }

                override fun onCancelled(name: String, message: String) {
                   
                }
            },
            object : Completion {
                override fun onComplete(dataModel: SaveDataModel, position: Int) {
                    val intent = Intent(activity, AnalysisActivity::class.java)
                    intent.putExtra("name", dataModel.date)
                    requireActivity().startActivity(intent)
                }

                override fun onCancelled(name: String, message: String) {}
            })

        infoCardView = binding.root.findViewById<View>(R.id.info_card)

        val button = binding.root.findViewById<View>(R.id.log_out)

        animatedButton = AnimatedButton(
            "log out",
            "please wait..",
            requireActivity().resources.getDrawable(R.drawable.ic_baseline_logout_24, null),
            button,
            textColor = requireActivity().resources.getColor(
                R.color.md_theme_dark_background,
                null
            ),
            completion = object : Completion {
                override fun onComplete(dataModel: SaveDataModel, position: Int) {
                    Firebase.auth.signOut()
                    requireActivity().startActivity(Intent(activity, LoginActivity::class.java))
                    requireActivity().finishAffinity()
                }

                override fun onCancelled(name: String, message: String) {
                    Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
                }
            }
        )

        setInfoCard()

        button.setOnClickListener {
            animatedButton.activate()
        }

        viewModel.getData()

        viewModel.observeUser().observe(viewLifecycleOwner) { putValues(it) }

        sharedViewModel.getList().observe(viewLifecycleOwner) {
            putValues(it)
        }

        sharedViewModel.getFilteredList().observe(viewLifecycleOwner) { putValuesInRec(it) }

        binding.recArchive.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recArchive.hasFixedSize()
        binding.recArchive.adapter = this.adapter



        return binding.root
    }

    private fun putValuesInRec(it: List<SaveDataModel>) {
        list.clear()
        list.addAll(it)
        adapter.notifyDataSetChanged()
    }

    private fun unArchive(position: Int, dataModel: SaveDataModel) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Firebase.database.getReference("Reports")
                    .child(Firebase.auth.currentUser!!.uid)
                    .child(dataModel.date)
                    .child("archive")
                    .setValue(false)
                    .await()
            } catch (e: java.lang.Exception) {
                e(tag, e.message.toString())
            } finally {
                launch(Dispatchers.Main) {
                    list.remove(dataModel)
                    adapter.notifyItemRemoved(position)
                    Snackbar.make(binding.root, "unarchived", Snackbar.LENGTH_LONG).show()
                }
            }

        }
    }

    private fun setInfoCard() {
        infoCard = InfoCard(infoCardView)
        infoCard.setHead("Tests", "last Test")
        infoCard.setIcons(
            requireActivity().resources.getDrawable(R.drawable.ic_baseline_numbers_24, null),
            requireActivity().resources.getDrawable(R.drawable.web_svgrepo_com___copy, null)
        )


    }

    private fun putValues(it: User) {
        binding.user = it
        Glide.with(this).load(it.photoUrl).into(binding.img)
    }

    private fun putValues(it: List<SaveDataModel>) {
        if (it.isEmpty()) {
            infoCard.setTexts("0","unavailable")
            return
        } else {
            val item = it[it.lastIndex].date
            infoCard.setTexts(it.size.toString(), getTime(item))
        }
    }

    private fun getTime(dateString: String?): String {
        val date = SimpleDateFormat("dd-MM-yyyy HH:mm").parse(dateString)
        val format1 = SimpleDateFormat("dd MM yyyy")
        val format2 = SimpleDateFormat("HH:mm")
        return "${format1.format(date)}\n${format2.format(date)}"
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}