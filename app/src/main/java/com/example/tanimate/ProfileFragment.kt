package com.example.tanimate

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.tanimate.data.local.UserModel
import com.example.tanimate.data.local.UserSession
import com.example.tanimate.viewmodel.HomeViewModel
import com.example.tanimate.viewmodel.ProfileViewModel
import com.example.tanimate.viewmodel.ViewModelFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "Setting")
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var dataStore : DataStore<Preferences>
    private var user : UserModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        dataStore = requireContext().dataStore
        val pref = UserSession.getInstance(dataStore)
        profileViewModel = ViewModelProvider(this, ViewModelFactory(requireContext(), pref))[ProfileViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
   //     return inflater.inflate(R.layout.fragment_profile, container, false)


        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        view?.findViewById<Button>(R.id.bt_logout)?.setOnClickListener {
            AlertDialog.Builder(activity).apply {
                setTitle("CONFIRMATION")
                setMessage("Logout of your account?")
                setPositiveButton("Yes") {_,_ ->
                    profileViewModel.logout()
                    activity?.finish()
                }
                setNegativeButton("No") {dialog,_ -> dialog.cancel()}
                create()
                show()
            }
        }

        var tv_username = view?.findViewById<TextView>(R.id.tv_NamaProfile)
        tv_username?.setText(username)

//        view?.findViewById<TextView>(R.id.tv_NamaProfile)?.text =

        return view
    }

    companion object {
        var username = ""

//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            ProfileFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
    }
}