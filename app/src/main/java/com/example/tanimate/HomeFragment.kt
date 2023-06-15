package com.example.tanimate

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tanimate.data.local.berita.berita
import com.example.tanimate.databinding.FragmentHomeBinding
import com.example.tanimate.databinding.FragmentNotesBinding


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val list = ArrayList<berita>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment


        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        list.addAll(getListBerita())
        showRecyclerView()
        return root
    }

    private fun showRecyclerView() {
        binding.rvBeritaHome.layoutManager = LinearLayoutManager(context)
        val listBeritaAdapter = BeritaAdapter(list)
        binding.rvBeritaHome.adapter = listBeritaAdapter

        listBeritaAdapter.setOnItemClickCallback(object : BeritaAdapter.OnItemClickCallback{
            override fun onItemClicked(data: berita){
                showSelectedBerita(data)
            }
        })
    }

    private fun showSelectedBerita(Berita: berita) {
        var url = ""
        when(Berita.id){
            "no1" -> {
                url = "https://akcdn.detik.net.id/community/media/visual/2022/05/08/jamur-tiram-untuk-bakwan_169.jpeg?w=700&q=90"
            }
            "no2" -> {
                url = "https://akcdn.detik.net.id/community/media/visual/2022/05/08/jamur-tiram-untuk-bakwan_169.jpeg?w=700&q=90"
            }
            "no3" -> {
                url = "https://akcdn.detik.net.id/community/media/visual/2022/05/08/jamur-tiram-untuk-bakwan_169.jpeg?w=700&q=90"
            }
            "no4" -> {
                url = "https://akcdn.detik.net.id/community/media/visual/2022/05/08/jamur-tiram-untuk-bakwan_169.jpeg?w=700&q=90"
            }
        }
        goToBerita(url)
    }

    private fun goToBerita(url: String) {
        val openURL = Intent(Intent.ACTION_VIEW)
        openURL.data = Uri.parse(url)
        startActivity(openURL)
    }

    private fun getListBerita(): ArrayList<berita> {
        val dataPhoto = resources.obtainTypedArray(R.array.photo)
        val dataJudul = resources.getStringArray(R.array.judul_berita)
        val dataId = resources.getStringArray(R.array.id)
        val listBerita = ArrayList<berita>()
        for (i in dataJudul.indices){
            val berita = berita(dataId[i],dataPhoto.getResourceId(i,-1),dataJudul[i])
            listBerita.add(berita)
        }
        return listBerita
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}