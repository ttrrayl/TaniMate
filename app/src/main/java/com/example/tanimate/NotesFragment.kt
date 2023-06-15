package com.example.tanimate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tanimate.databinding.FragmentNotesBinding
import com.example.tanimate.ui.NoteAddUpdateActivity
import com.example.tanimate.viewmodel.NotesViewModel
import com.example.tanimate.viewmodel.ViewModelFactory
import com.example.tanimate.viewmodel.ViewModelFactoryNote

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "Setting")
class NotesFragment : Fragment() {
    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!
    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var notesViewModel: NotesViewModel
    private lateinit var adapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataStore = requireActivity().dataStore
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setViewModel()

        return root
    }

    private fun setViewModel() {
        notesViewModel = obtainViewModel(requireActivity() as AppCompatActivity)
        notesViewModel.getAllNotes().observe(requireActivity()) { noteList ->
            if (noteList != null) {
                adapter.setListNotes(noteList)
            }
        }
        adapter = NoteAdapter()
        binding.rvTextsms.layoutManager = LinearLayoutManager(context)
        binding.rvTextsms.setHasFixedSize(true)
        binding.rvTextsms.adapter = adapter

        binding.fabTambah.setOnClickListener{
            val intent = Intent(context, NoteAddUpdateActivity::class.java)
            startActivity(intent)
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity) : NotesViewModel{
        val factory = ViewModelFactoryNote.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[NotesViewModel::class.java]
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}